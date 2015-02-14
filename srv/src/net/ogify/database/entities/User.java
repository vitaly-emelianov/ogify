package net.ogify.database.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by melges.morgen on 14.02.15.
 */
@Entity
@Table(name = "users")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "User.getById", query = "select user from User user where user.id = :id"),
        @NamedQuery(name = "User.getByIdAndSession", query = "select user from User user " +
                "where user.id = :userId " +
                "and user = (select session.owner " +
                "from UserSession session where session.sessionSecret = :sessionSecret)")
})
public class User {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, unique = true)
    @XmlElement(name = "user_id", nillable = false, required = true)
    Long id;

    @Column(name = "facebook_id", nullable = true)
    Long facebookId;

    @Column(name = "vk_id", nullable = true)
    Long vkId;

    @Column(name = "fullName", nullable = false, unique = false)
    @XmlElement(name = "fullName",required = true, nillable = false)
    String fullname;

    @Column(name = "photo_uri", nullable = false, unique = false)
    @XmlElement(name = "photoUri",required = true, nillable = false)
    String photoUri;

    @Column(name = "rating_as_customer", nullable = false, unique = false)
    @XmlElement(name = "rating_as_customer", nillable = true, required = false)
    Double ratingAsCustomer = 3.5;

    @XmlElement(name = "rating_as_customer", nillable = true, required = false)
    Double ratingAsExecutor = 3.5;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapKeyColumn(name = "session_secret", updatable = false, insertable = false, nullable = false)
    private Map<String, UserSession> sessions = new HashMap<String, UserSession>();

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

    }

    public void addAuthToken(String token, SocialNetwork socialNetwork) {


    }
}
