package net.ogify.database.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by melges.morgen on 14.02.15.
 */
@Entity
@Table(name = "feedbacks")
public class Feedback {
    @XmlType(name = "feedback-type")
    @XmlEnum
    public enum FeedbackAbout{
        @XmlEnumValue("executor") Executor,
        @XmlEnumValue("customer") Customer
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id", nullable = false)
    Long id;

    String comment;

    @ManyToOne
    User who;

    @ManyToOne
    User whom;
}
