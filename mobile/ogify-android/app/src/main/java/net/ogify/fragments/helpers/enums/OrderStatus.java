package net.ogify.fragments.helpers.enums;

public enum OrderStatus {
    NEW("New"),
    RUNNING("Running"),
    COMPLETE("Complete");

    private String label;

    OrderStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
