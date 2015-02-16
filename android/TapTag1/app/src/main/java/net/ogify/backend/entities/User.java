package net.ogify.backend.entities;

import net.ogify.backend.elements.vkapi.UserSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    Long id;

    Long facebookId;

    Long vkId;

    String fullname;

    String photoUri;

    Double ratingAsCustomer = 3.5;

    Double ratingAsExecutor = 3.5;

    private Map<String, UserSession> sessions = new HashMap<String, UserSession>();

    List<SocialToken> tokens = new ArrayList<SocialToken>();

    List<Order> orders = new ArrayList<Order>();

    List<Order> tasks = new ArrayList<Order>();

    List<Feedback> usersFeedbacks = new ArrayList<Feedback>();

    List<Feedback> feedbackAboutUser = new ArrayList<Feedback>();

    public User() {

    }

    public User(String fullname, String photoUri) {
        this.fullname = fullname;
        this.photoUri = photoUri;
    }

    public Long getId() {
        return id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public Double getRatingAsCustomer() {
        return ratingAsCustomer;
    }

    public void setRatingAsCustomer(Double ratingAsCustomer) {
        this.ratingAsCustomer = ratingAsCustomer;
    }

    public Double getRatingAsExecutor() {
        return ratingAsExecutor;
    }

    public void setRatingAsExecutor(Double ratingAsExecutor) {
        this.ratingAsExecutor = ratingAsExecutor;
    }

    public Long getVkId() {
        return vkId;
    }

    public void setVkId(Long vkId) {
        this.vkId = vkId;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void addSession(String sessionSecret, Long expireIn) {
        if(sessions.containsKey(sessionSecret))
            return; // Current user already have provided session
        UserSession session = new UserSession(sessionSecret, expireIn, this);
        sessions.put(session.getSessionSecret(), session);
    }

    public void addAuthToken(String token, SocialNetwork socialNetwork, Long expireIn) {
        SocialToken authToken = new SocialToken(token, expireIn, socialNetwork, this);
        tokens.add(authToken);
    }
}
