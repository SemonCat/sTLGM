package com.thu.stlgm.bean;

import com.facebook.AccessToken;
import com.facebook.model.GraphUser;

/**
 * Created by SemonCat on 2014/1/29.
 */
public class AccountBean {

    private String id;
    private String AccessToken;

    private GraphUser mGraphUser;


    public AccountBean() {

    }

    public AccountBean(String AccessToken) {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String accessToken) {
        this.AccessToken = accessToken;
    }


    public GraphUser getGraphUser() {
        return mGraphUser;
    }

    public void setGraphUser(GraphUser mGraphUser) {
        this.mGraphUser = mGraphUser;
    }

    @Override
    public boolean equals(Object o) {

        return ((AccountBean)o).getAccessToken()==this.getAccessToken();
    }
}
