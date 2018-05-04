package com.les.app.les_login.oauth;

import android.content.Context;
import android.content.Intent;

public class SignInWeChat extends SignIn{

    protected SignInWeChat(Context context, AuthDefine.LoginCallBack loginCallBack) {
        super(context);
    }

    @Override
    public void logout() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void signIn() {

    }
}
