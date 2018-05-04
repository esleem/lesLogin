package com.les.app.les_login.oauth;

import android.content.Context;
import android.content.Intent;

import com.les.app.les_login.base.listener.SelectListItemListener;

public class SignInFacebook extends SignIn{

    protected SignInFacebook(Context context, AuthDefine.LoginCallBack loginCallBack) {
        super(context);
    }

    @Override
    public void logout(SelectListItemListener itemListener) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    @Override
    public void signIn() {

    }
}
