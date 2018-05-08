package com.les.app.les_login.oauth;

import android.support.annotation.Keep;

public class AuthDefine {

    public interface LoginCallBack{
        void onLoginResult(AuthDefine.LOGIN_CALLBACK_STATUS var1);
    }

    @Keep
    public static enum LOGIN_CALLBACK_STATUS {

        NOT_SIGNIN,
        LOGOUT,
        LOGIN_SUCCESS,
        LOGIN_FAIL,
        TOKEN_EXPIRED,
        UNEXPECTED_ERROR;

        private LOGIN_CALLBACK_STATUS(){}

    }

    @Keep
    public static enum AUTH_TYPE {

        GOOGLE("GOOGLE"),
        FACEBOOK("FACEBOOK"),
        WECHAT("GOOWECHATGLE"),
        KAKAO("KAKAO"),
        NO_SIGNUP("NO_SIGNUP");

        AUTH_TYPE(String authType){ }
    }
}
