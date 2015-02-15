package net.ogify.rest.elements;

import net.ogify.database.entities.SocialNetwork;

/**
 * Created by melges.morgen on 15.02.15.
 */
public class SocialNetworkParam {
    private SocialNetwork sn;

    public SocialNetworkParam() {

    }

    public SocialNetworkParam(String value) {
        if(value.equalsIgnoreCase("vk"))
            sn = SocialNetwork.Vk;
        else if(value.equalsIgnoreCase("facebook"))
            sn = SocialNetwork.FaceBook;
        else
            sn = SocialNetwork.Other;
    }

    public SocialNetwork getValue() {
        return sn;
    }
}
