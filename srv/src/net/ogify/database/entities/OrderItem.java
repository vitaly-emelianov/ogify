package net.ogify.database.entities;

import javax.persistence.*;

/**
 * Created by melges.morgen on 14.02.15.
 */
@Entity
@Table(name = "orders_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    Long id;

    @Column(name = "expected_cost")
    Double expectedCost;

    @Column(name = "item_comment")
    String comment;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_order", nullable = false)
    Order order;

    public void setOrder(Order order) {
        this.order = order;
    }
}
