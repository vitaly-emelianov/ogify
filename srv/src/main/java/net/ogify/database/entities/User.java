package net.ogify.database.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.*;

/**
 * Created by melges.morgen on 14.02.15.
 */
@Entity
@Table(name = "users")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "User.getAllIds", query = "select user.id from User user"),
        @NamedQuery(name = "User.getById", query = "select user from User user where user.id = :id"),
        @NamedQuery(name = "User.getByIdAndSession", query = "select user from User user, UserSession session " +
                "where user.id = :userId " +
                "and user = session.owner " +
                "and session.sessionSecret = :sessionSecret"),
        @NamedQuery(name = "User.getUserByVkId", query = "select user from User user where user.vkId = :vkId"),
        @NamedQuery(name = "User.getUsersByVkIds", query = "select user from User user where user.vkId IN :vkIds"),
        @NamedQuery(name = "User.getUserByFbId", query = "select user from User user where user.facebookId = :fbId"),
        @NamedQuery(name = "User.getUsersByIds", query = "select user from User user where user.id IN :ids")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @XmlElement(name = "userId", nillable = false, required = true)
    Long id;

    @Column(name = "facebook_id", nullable = true, unique = true)
    Long facebookId;

    @Column(name = "vk_id", nullable = true, unique = true)
    Long vkId;

    @Column(name = "fullName", nullable = false, unique = false)
    @XmlElement(name = "fullName",required = true, nillable = false)
    String fullName;

    @Column(name = "photo_uri", nullable = false, unique = false)
    @XmlElement(name = "photoUri",required = true, nillable = false)
    String photoUri;

    @Column(name = "rating_as_customer", nullable = false, unique = false)
    @XmlElement(name = "ratingAsCustomer", nillable = true, required = false)
    Double ratingAsCustomer = 3.5;

    @Column(name = "rating_as_executor", nullable = false, unique = false)
    @XmlElement(name = "ratingAsExecutor", nillable = true, required = false)
    Double ratingAsExecutor = 3.5;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapKeyColumn(name = "session_secret",unique = true, updatable = false, insertable = false)
    private Map<String, UserSession> sessions = new HashMap<String, UserSession>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<SocialToken> tokens = new ArrayList<SocialToken>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Order> orders = new ArrayList<Order>();

    @OneToMany(mappedBy = "executor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Order> tasks = new ArrayList<Order>();

    @OneToMany(mappedBy = "who")
    List<Feedback> usersFeedbacks = new ArrayList<Feedback>();

    @OneToMany(mappedBy = "whom")
    List<Feedback> feedbackAboutUser = new ArrayList<Feedback>();

    public User() {

    }

    public User(String fullName, String photoUri) {
        this.fullName = fullName;
        this.photoUri = photoUri;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
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

    public void setFullName(String fullname) {
        this.fullName = fullname;
    }

    @XmlTransient
    public SocialToken getVkToken() {
        tokens.sort(new Comparator<SocialToken>() {
            @Override
            public int compare(SocialToken o1, SocialToken o2) {
                return o1.getExpireIn().compareTo(o2.getExpireIn());
            }
        });

        return tokens.get(0);
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

    public void addOrder(Order order) {
        orders.add(order);
        order.setOwner(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        User user = (User) o;

        return !(id != null ? !id.equals(user.id) : user.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
