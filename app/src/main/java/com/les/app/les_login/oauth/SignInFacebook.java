package com.les.app.les_login.oauth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.les.app.les_login.base.listener.SelectListItemListener;
import com.les.app.les_login.utils.CommonUtils;

import java.util.Arrays;
import java.util.Collections;

public class SignInFacebook extends SignIn{

    private Context mContext;
    private AuthDefine.LoginCallBack mLoginCallBack; // 로그인 콜백리스너

    private CallbackManager callbackManager;
    private LoginManager loginManager;

    protected SignInFacebook(Context context, AuthDefine.LoginCallBack loginCallBack) {
        super(context);

        mContext = context;
        mLoginCallBack = loginCallBack;

        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                //----------
                // 로그인 성공
                //----------
                CommonUtils.log("Facebook - onSuccess");
                setSelectedOauth(AuthDefine.AUTH_TYPE.FACEBOOK);
                mLoginCallBack.onLoginResult(AuthDefine.LOGIN_CALLBACK_STATUS.LOGIN_SUCCESS);

            }

            @Override
            public void onCancel() {

                CommonUtils.log("Facebook - onCancel");
                mLoginCallBack.onLoginResult(AuthDefine.LOGIN_CALLBACK_STATUS.LOGIN_FAIL);

            }

            @Override
            public void onError(FacebookException error) {

                CommonUtils.log("Facebook - error : " + error.getMessage());
                mLoginCallBack.onLoginResult(AuthDefine.LOGIN_CALLBACK_STATUS.LOGIN_FAIL);
            }
        });

    }

    @Override
    public void logout(SelectListItemListener itemListener) {

        CommonUtils.log("Facebook - logout()");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        CommonUtils.log("Facebook - onActivityResult()");
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void signIn() {

        CommonUtils.log("Facebook - signIn()");
        loginManager.logInWithReadPermissions((Activity) mContext, Arrays.asList("public_profile", "email", "user_birthday"));

    }
}
