package net.ogify.engine.secure;

import net.ogify.database.BetaKeyController;
import net.ogify.database.UserController;
import net.ogify.database.entities.BetaKey;
import net.ogify.database.entities.SocialNetwork;
import net.ogify.database.entities.User;
import net.ogify.engine.secure.exceptions.ForbiddenException;
import net.ogify.engine.vkapi.VkAuth;
import net.ogify.engine.vkapi.VkUsers;
import net.ogify.engine.vkapi.elements.VkAccessResponse;
import net.ogify.engine.vkapi.elements.VkUserInfo;
import net.ogify.engine.vkapi.exceptions.VkSideError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Class contain methods for work with user authentication.
 */
@Component
public class AuthController {
    public static final String SESSION_COOKIE_NAME = "ogifySessionSecret";

    public static final String USER_ID_COOKIE_NAME = "sId";

    @Autowired
    VkAuth vkAuth;

    @Autowired
    UserController userController;

    @Autowired
    BetaKeyController betaKeyController;

    /**
     * Method generate secure random number which should be used as session secret
     * @return random generated string
     */
    public static String generateSessionSecret() {
        Random randomGenerator = new SecureRandom();
        byte[] sessionSecretAsBytes = new byte[16];
        randomGenerator.nextBytes(sessionSecretAsBytes);

        return DatatypeConverter.printHexBinary(sessionSecretAsBytes);
    }


    /**
     * Method check data provided by client for correctness and session validity.
     * @param userId vkId of user, provided by client.
     * @param sessionSecret session id provided by client (session secret).
     * @return true if clint give correct data, or false if client is liar or his session expired.
     */
    public boolean isSessionCorrect(Long userId, String sessionSecret) {
        return userController.getUserByIdAndSession(userId, sessionSecret) != null;
    }

    /**
     * Method authorize client by provided data using vk oauth protocol. If user client authenticate at first time,
     * new user will be created. In other cases we find who is authenticate and create new session.
     *
     * @param code code provided by client (client must request it from vk first).
     * @param redirectUrl redirect uri which client provide to vk client and which client call for invoke auth.
     * @param sessionSecret client session secret.
     * @param betaKey special key used for beta programs.
     * @return vk id returned from
     * @throws VkSideError if vk say about error, or we have a trouble when connecting to vk.
     */
    public Long auth(String code, String redirectUrl, String sessionSecret, String betaKey)
            throws VkSideError {
        BetaKey betaKeyInternal = checkBetaKey(betaKey);

        betaKeyInternal.incrementUsedTime();
        betaKeyController.saveOrUpdate(betaKeyInternal);

        betaKeyController.saveOrUpdate(betaKeyInternal);

        return authVk(code, redirectUrl, sessionSecret);
    }

    public Long authVk(String code, String redirectUrl, String sessionSecret) throws VkSideError {
        VkAccessResponse response = vkAuth.auth(code, redirectUrl);
        User user = userController.getUserByVkId(response.getUserId());
        if(user == null) {
            VkUserInfo vkUserInfo = VkUsers.get(response.getUserId(), response.getAccessToken());
            user = new User(vkUserInfo.getFullName(), vkUserInfo.getPhotoUri());
            user.setVkId(vkUserInfo.getId());
        }

        user.addSession(sessionSecret, response.getExpiresIn());
        user.addAuthToken(response.getAccessToken(), SocialNetwork.Vk, response.getExpiresIn());

        userController.saveOrUpdate(user);

        return user.getId();
    }

    public Long authFb(String code, String redirectUrl, String sessionSecret) throws VkSideError {
        return null;
    }

    public BetaKey checkBetaKey(String key) {
        BetaKey betaKeyInternal = betaKeyController.getByKey(key);
        if(betaKeyInternal == null)
            throw new ForbiddenException("Sorry there is no provided key");
        if(betaKeyInternal.getUsedTime() > 5)
            throw new ForbiddenException("You have used your key too many times");

        return betaKeyInternal;
    }
}
