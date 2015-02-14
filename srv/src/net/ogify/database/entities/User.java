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
@NamedQueries(
        @NamedQuery(name = "User.getById", query = "select user from User user where user.id = :id")
)
public class User {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, unique = true)
    @XmlElement(name = "user_id", nillable = false, required = true)
    Long id;

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
}
