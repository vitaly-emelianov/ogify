package net.ogify.backend.entities;

public class Feedback {
    public enum FeedbackAbout{
        Executor,
        Customer
    }

    Long id;

    String comment;

    User who;

    User whom;
}
