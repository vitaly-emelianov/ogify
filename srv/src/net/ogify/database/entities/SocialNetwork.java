package net.ogify.database.entities;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
* Created by melges.morgen on 15.02.15.
*/
@XmlType(name = "social_network")
@XmlEnum
public enum SocialNetwork {
    @XmlEnumValue("Facebook") FaceBook,
    @XmlEnumValue("Vk") Vk
}
