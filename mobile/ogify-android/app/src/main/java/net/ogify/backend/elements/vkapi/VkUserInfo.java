package net.ogify.backend.elements.vkapi;

/**
 * Class represent vk answer about one user (for example in users.get method).
 *
 * May be serialized to JSON or XML.
 *
 * @author Morgen Matvey
 */
public class VkUserInfo {
    /**
     * User if (vkId)
     */
    private Long id;

    /**
     * First name in subjective case.
     */
    private String firstName;

    /**
     * Last name in subjective case.
     */
    private String lastName;

    /**
     * Url to users photo
     */
    private String photoUri;

    /**
     * Default constructor
     */
    public VkUserInfo() {
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    /**
     * Method create full user name from first and last name.
     * @return full user name.
     */
    public String getFullName() {
        return String.format("%s %s", lastName, firstName);
    }
}
