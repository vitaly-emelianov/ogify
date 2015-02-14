package net.ogify.database.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.Date;

/**
 * Created by melges.morgen on 14.02.15.
 */
@Entity
@Table(name = "users_sessions")
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE) // Deny session transfer to client
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id", unique = true, nullable = false)
    Long id;

    @Column(name = "session_secret", nullable = false, unique = false, length = 32, updatable = false)
    private String sessionSecret;

    @Temporal(TemporalType.TIMESTAMP)
    Date expireIn;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner", nullable = false, unique = false, updatable = false)
    User owner;
}
