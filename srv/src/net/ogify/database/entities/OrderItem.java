package net.ogify.database.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by melges.morgen on 14.02.15.
 */
@Entity
@Table(name = "orders_items")
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    @XmlElement
    Long id;

    @Column(name = "expected_cost")
    @XmlElement(required = true, nillable = false)
    Double expectedCost;

    @Column(name = "item_comment")
    @XmlElement
    String comment;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_order", nullable = false)
    Order order;

    public void setOrder(Order order) {
        this.order = order;
    }
}
