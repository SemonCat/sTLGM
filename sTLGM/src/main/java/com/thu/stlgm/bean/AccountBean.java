package com.thu.stlgm.bean;

import android.text.TextUtils;
import android.util.Log;

import com.facebook.model.GraphUser;

import java.io.Serializable;

/**
 * Created by SemonCat on 2014/1/29.
 */
public class AccountBean implements Serializable {


    private String id;
    private String AccessToken;

    private String Name;

    public AccountBean() {

    }

    public AccountBean(String AccessToken) {
        setAccessToken(AccessToken);
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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


    public boolean isLogin() {
        return !TextUtils.isEmpty(getAccessToken());
    }

    @Override
    public int hashCode() {

        return getId().hashCode();
    }

    @Override
    public boolean equals(Object o) {


        String token1 = ((AccountBean) o).getAccessToken();
        String token2 = this.getAccessToken();

        if (token1==null || token2==null)
            return false;
        return token1.equals(token2);
    }

}
