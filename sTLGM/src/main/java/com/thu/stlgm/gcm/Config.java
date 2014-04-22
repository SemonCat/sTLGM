package com.thu.stlgm.gcm;

import com.thu.stlgm.util.ConstantUtil;

/**
 * Created by SemonCat on 2014/4/21.
 */
public interface Config {

    // GCM server using java
     static final String APP_SERVER_URL =
            ConstantUtil.ServerIP+"/GCMNotification?shareRegId=1";

    // Google Project Number
    static final String GOOGLE_PROJECT_ID = "512218038480";
    static final String MESSAGE_KEY = "message";

}
