package com.thu.stlgm.bean;

import com.facebook.AccessToken;

/**
 * Created by SemonCat on 2014/1/29.
 */
public class AccountBean {

    private String id;
    private String fb_id;
    private String AccessToken;


    public AccountBean() {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String accessToken) {
        this.AccessToken = accessToken;
    }


}
