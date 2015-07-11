package net.ogify.engine.secure;

import net.ogify.database.UserController;
import net.ogify.database.entities.SocialNetwork;
import net.ogify.database.entities.User;
import net.ogify.engine.vkapi.VkAuth;
import net.ogify.engine.vkapi.VkUsers;
import net.ogify.engine.vkapi.elements.VkAccessResponse;
import net.ogify.engine.vkapi.elements.VkUserInfo;
import net.ogify.engine.vkapi.exceptions.VkSideError;

import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Class contain methods for work with user authentication.
 */
public class AuthController {
    public static final String SESSION_COOKIE_NAME = "ogifySessionSecret";

    public static final String USER_ID_COOKIE_NAME = "sId";

    /**
     * Method check data provided by client for correctness and session validity.
     * @param userId vkId of user, provided by client.
     * @param sessionSecret session id provided by client (session secret).
     * @return true if clint give correct data, or false if client is liar or his session expired.
     */
    public static boolean isSessionCorrect(Long userId, String sessionSecret) {
        return UserController.getUserByIdAndSession(userId, sessionSecret) != null;
    }

    /**
     * Method authorize client by provided data using vk oauth protocol. If user client authenticate at first time,
     * new user will be created. In other cases we find who is authenticate and create new session.
     *
     * @param code code provided by client (client must request it from vk first).
     * @param redirectUrl redirect uri which client provide to vk client and which client call for invoke auth.
     * @param sessionSecret client session secret.
     * @return vk id returned from
     * @throws VkSideError if vk say about error, or we have a trouble when connecting to vk.
     */
    public static Long auth(String code, String redirectUrl, String sessionSecret, SocialNetwork socialNetwork)
            throws VkSideError {
        Long userId;
        switch(socialNetwork) {
            case Vk:
                userId = authVk(code, redirectUrl, sessionSecret);
                break;
            case FaceBook:
                userId = authFb(code, redirectUrl, sessionSecret);
                break;
            default:
                throw new RuntimeException("Argument of type social network have unsupported value");
        }

        return userId;
    }

    public static Long authVk(String code, String redirectUrl, String sessionSecret) throws VkSideError {
        VkAccessResponse response = VkAuth.auth(code, redirectUrl);
        User user = UserController.getUserByVkId(response.getUserId());
        if(user == null) {
            VkUserInfo vkUserInfo = VkUsers.get(response.getUserId(), response.getAccessToken());
            user = new User(vkUserInfo.getFullName(), vkUserInfo.getPhotoUri());
            user.setVkId(vkUserInfo.getId());
        }

        user.addSession(sessionSecret, response.getExpiresIn());
        user.addAuthToken(response.getAccessToken(), SocialNetwork.Vk, response.getExpiresIn());

        UserController.saveOrUpdate(user);

        return user.getId();
    }

    public static Long authFb(String code, String redirectUrl, String sessionSecret) throws VkSideError {
        return null;
    }

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
}
