package net.ogify.database.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by melges.morgen on 14.02.15.
 */
@Entity
@Table(name = "orders")
@XmlRootElement
public class Order {
    @XmlType(name = "order_status")
    @XmlEnum
    public enum OrderStatus {
        @XmlEnumValue("new") New,
        @XmlEnumValue("running") Running,
        @XmlEnumValue("completed") Completed
    }

    @XmlType(name = "order_namespace")
    @XmlEnum
    public enum OrderNamespace {
        @XmlEnumValue("private") Private,
        @XmlEnumValue("friends") Friends,
        @XmlEnumValue("friends-of-friends") FriendsOfFriends,
        @XmlEnumValue("all") All,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    @XmlElement(nillable = false, required = true)
    Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_owner", nullable = false)
    @XmlElement(nillable = false, required = true)
    User owner;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_executor", nullable = false)
    @XmlElement(nillable = false, required = true)
    User executor;

    @Column(name = "order_status", nullable = false)
    @XmlElement(nillable = false, required = true)
    OrderStatus status;

    @Column(name = "order_namespace", nullable = false)
    @XmlElement(nillable = false, required = true)
    OrderNamespace namespace;

    @Column(name = "order_description")
    @XmlElement
    String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    @XmlElement
    Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "done_at", nullable = false)
    @XmlElement
    Date doneAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expire_in")
    @XmlElement
    Date expireIn;

    @OneToMany(mappedBy = "order")
    List<OrderItem> items = new ArrayList<OrderItem>();
}
