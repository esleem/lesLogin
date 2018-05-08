package com.les.app.les_login.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.les.app.les_login.R;
import com.les.app.les_login.oauth.AuthDefine;
import com.les.app.les_login.oauth.SignIn;
import com.les.app.les_login.utils.CommonUtils;

public class LoginActivity extends AppCompatActivity {

    //--------------------
    //
    //--------------------
    private Button btn_login_A;             // 구글 로그인
    private Button btn_login_B;             // 페북 로그인
    private Button btn_login_C;             // 위쳇 로그인
    private Button btn_login_D;             // 카톡 로그인

    //--------------------
    //
    //--------------------
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    public SignIn mSignin;                                      //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_login);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            }
        };
        mAuth.addAuthStateListener(mAuthListener);

        //--------------------
        // 로그인 - 구글
        //--------------------
        btn_login_A = (Button) findViewById(R.id.btn_login_A);
        btn_login_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mSignin = SignIn.getSignInstance(AuthDefine.AUTH_TYPE.GOOGLE, LoginActivity.this, mLoginCallBack);
                mSignin.signIn();
            }
        });

        //--------------------
        // 로그인 - 페이스북
        //--------------------
        btn_login_B = (Button) findViewById(R.id.btn_login_B);
        btn_login_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mSignin = SignIn.getSignInstance(AuthDefine.AUTH_TYPE.FACEBOOK, LoginActivity.this, mLoginCallBack);
                mSignin.signIn();
            }
        });

        //--------------------
        // 로그인 - 위챗
        //--------------------
        btn_login_C = (Button) findViewById(R.id.btn_login_C);
        btn_login_C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mSignin = SignIn.getSignInstance(AuthDefine.AUTH_TYPE.WECHAT, LoginActivity.this, mLoginCallBack);
                mSignin.signIn();
            }
        });

        //--------------------
        // 로그인 - 위챗
        //--------------------
        btn_login_D= (Button) findViewById(R.id.btn_login_D);
        btn_login_D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mSignin = SignIn.getSignInstance(AuthDefine.AUTH_TYPE.KAKAO, LoginActivity.this, mLoginCallBack);
                mSignin.signIn();
            }
        });

        //--------------------
        // 자동로그인
        //--------------------
        mSignin = SignIn.getSignInstance(SignIn.getSelectedSignIn(LoginActivity.this),this , mLoginCallBack);
        if (mSignin != null) {
            mSignin.silentLogin();
            CommonUtils.log("@# NOT NULL");
        }else {
            CommonUtils.log("@# NULL");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (mSignin != null){
            mSignin.onActivityResult(requestCode, resultCode, data);
        }
    }

    //--------------------------------------------------
    // 소셜 로그인 콜백리스너
    //--------------------------------------------------
    public AuthDefine.LoginCallBack mLoginCallBack = new AuthDefine.LoginCallBack() {
        @Override
        public void onLoginResult(AuthDefine.LOGIN_CALLBACK_STATUS var1) {

            if ( AuthDefine.LOGIN_CALLBACK_STATUS.LOGIN_SUCCESS.equals(var1) ){

                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        }
    };

}
