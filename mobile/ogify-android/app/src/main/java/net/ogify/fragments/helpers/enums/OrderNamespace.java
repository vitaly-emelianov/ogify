package net.ogify.fragments.helpers.enums;

public enum OrderNamespace {
    MY("My"),
    ASSIGNED_TO_ME("Assigned To Me"),
    PRIVATE("Private"),
    FRIENDS("Friends"),
    FRIENDS_OF_FRIENDS("Friends Of Friends"),
    ALL("All");

    private String label;

    OrderNamespace(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
