package net.ogify.database.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by melges.morgen on 14.02.15.
 */
@Entity
@Table(name = "orders")
@NamedQueries({@NamedQuery(name = "Order.getNearestOrder", query = "select orders from Order orders " +
        "where orders.latitude > (:latitude - 0.07) and orders.latitude < (:latitude + 0.07) " +
        "and orders.longitude > (:longitude - 0.07) and orders.longitude < (:longitude + 0.07)" +
        "and orders.expireIn > CURRENT_TIMESTAMP and orders.status = :orderStatus " +
        "and orders.namespace = :orderNamespace")})
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
    @XmlElement(nillable = true, required = false)
    Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_owner", nullable = false)
    @XmlElement(nillable = false, required = true)
    User owner;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_executor", nullable = true)
    @XmlElement(nillable = true, required = true)
    User executor;

    @NotNull
    @Column(name = "order_status", nullable = false)
    @XmlElement(nillable = false, required = true)
    OrderStatus status;

    @NotNull
    @Column(name = "order_namespace", nullable = false)
    @XmlElement(nillable = false, required = true)
    OrderNamespace namespace;

    @NotNull
    @Column(name = "latitude", nullable = false)
    @XmlElement(required = true, nillable = false)
    Double latitude;

    @NotNull
    @Column(name = "longitude", nullable = false)
    @XmlElement(required = true, nillable = false)
    Double longitude;

    @Column(name = "address")
    @XmlElement(name = "address")
    String address;

    @Column(name = "reward")
    @XmlElement(name = "reward")
    String reward;

    @Column(name = "order_description")
    @XmlElement
    String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    @XmlElement
    Date createdAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "done_at", nullable = true)
    @XmlElement
    Date doneAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expire_in")
    @XmlElement
    Date expireIn;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @XmlElement(name = "items")
    List<OrderItem> items = new ArrayList<OrderItem>();

    public List<OrderItem> getItems() {
        return items;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getOwner() {
        return owner;
    }
}
