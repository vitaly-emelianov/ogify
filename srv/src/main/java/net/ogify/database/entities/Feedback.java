package net.ogify.database.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by melges.morgen on 14.02.15.
 */
@Entity
@Table(name = "feedbacks",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_who", "user_whom", "which_order"}))
@NamedQueries({
        @NamedQuery(name = "Feedback.getFeedbackRate", query =
                "select feedback.rate from Feedback feedback where " +
                "feedback.which.id = :whichOrderId and feedback.who.id = :whoRateId"),
        @NamedQuery(name = "Feedback.isFeedbackRated", query =
                "select feedback from Feedback feedback where " +
                "feedback.which = :whichOrder")})
public class Feedback {
    @XmlType(name = "feedback-type")
    @XmlEnum
    public enum FeedbackAbout{
        @XmlEnumValue("executor") Executor,
        @XmlEnumValue("customer") Customer
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    Long id;

    String comment;

    @JoinColumn(name = "user_who", nullable = false)
    @ManyToOne
    User who;

    @JoinColumn(name = "user_whom", nullable = false)
    @ManyToOne
    User whom;

    @JoinColumn(name = "which_order", nullable = false)
    @ManyToOne
    Order which;

    Integer rate;

    public Feedback() {
    }

    public Feedback(String comment, User who, User whom, Order which, Integer rate) {
        this.comment = comment;
        this.who = who;
        this.whom = whom;
        this.which = which;
        this.rate = rate;
    }
}
