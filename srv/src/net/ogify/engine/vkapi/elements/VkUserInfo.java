package net.ogify.engine.vkapi.elements;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.ogify.engine.vkapi.marshals.VkDateDeserializer;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Class represent vk answer about one user (for example in users.get method).
 *
 * May be serialized to JSON or XML.
 *
 * @author Morgen Matvey
 */
@XmlRootElement
public class VkUserInfo {
    /**
     * User if (vkId)
     */
    @NotNull
    @XmlElement(name = "id", required = true)
    private Long id;

    /**
     * First name in subjective case.
     */
    @NotNull
    @XmlElement(name = "first_name")
    private String firstName;

    /**
     * Last name in subjective case.
     */
    @NotNull
    @XmlElement(name = "last_name")
    private String lastName;

    /**
     * Users birth date without time, in format dd.MM.yyyy. For parse this field used special
     * deserializer {@link net.ogify.engine.vkapi.marshals.VkDateDeserializer}
     */
    @XmlElement(name = "bdate")
    @JsonDeserialize(using = VkDateDeserializer.class)
    private Date birthDate;

    /**
     * Url to users photo
     */
    @NotNull
    @XmlElement(name = "photo_max", required = true)
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

    public Date getBirthDate() {
        return birthDate;
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
