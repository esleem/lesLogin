package com.les.app.les_login.oauth;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.les.app.les_login.base.listener.SelectListItemListener;
import com.les.app.les_login.utils.CommonUtils;

public abstract class SignIn {

    protected SharedPreferences mSettings;
    public static SignIn mSignin;

    protected static final String SETTINGS_SELECTED_OAUTH = "selected_oauth";
    private static final String PREFS_NAME = "oauth_kokoa";

    protected SignIn(Context context){

        mSettings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static SignIn getSignInstance(AuthDefine.AUTH_TYPE authType, Context context, AuthDefine.LoginCallBack loginCallBack){

        switch (authType){

            case GOOGLE:

                mSignin = new SignInGoogle(context, loginCallBack);
                break;

            case FACEBOOK:

                mSignin = new SignInFacebook(context, loginCallBack);
                break;

            case WECHAT:

                mSignin = new SignInWeChat(context, loginCallBack);
                break;

            case KAKAO:

                mSignin = new SignInKakao(context, loginCallBack);
                break;

            case NO_SIGNUP:

                mSignin = null;
                break;
        }

        return mSignin;
    }

    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);
    public abstract void silentLogin();
    public abstract void signIn();
    public abstract void logout(SelectListItemListener itemListener);
    public abstract String getUserName();
    public abstract String getToken();
    public void onDestroy(){}

    //--------------------------------------------------
    // 소셜로그인 타입 가져오기
    //--------------------------------------------------
    public static AuthDefine.AUTH_TYPE getSelectedSignIn(Context context) {

        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        String selectedOauthStr = settings.getString(SETTINGS_SELECTED_OAUTH, AuthDefine.AUTH_TYPE.NO_SIGNUP.toString());
        AuthDefine.AUTH_TYPE selectedOauth = AuthDefine.AUTH_TYPE.valueOf(selectedOauthStr.toUpperCase());

        CommonUtils.log("getSelectedSignIn() mSelectedOauth = " + selectedOauth);

        return selectedOauth;
    }

    //--------------------------------------------------
    // 소셜로그인 타입 저장
    //--------------------------------------------------
    void setSelectedOauth(AuthDefine.AUTH_TYPE value){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(SETTINGS_SELECTED_OAUTH, value.toString());
        editor.commit();
    }

}
