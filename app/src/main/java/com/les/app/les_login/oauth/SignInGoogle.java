package com.les.app.les_login.oauth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.les.app.les_login.R;
import com.les.app.les_login.base.listener.SelectListItemListener;
import com.les.app.les_login.utils.CommonUtils;

public class SignInGoogle extends SignIn{

    private Context mContext;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;

    private boolean isSilentSignIn;                  // 자동로그인 여부
    private GoogleSignInAccount mAccount;            // 로그인 계정

    private AuthDefine.LoginCallBack mLoginCallBack; // 로그인 콜백리스너

    private final static int GOOGLE_LOGIN_RQ = 9001;

    public SignInGoogle(Context context, AuthDefine.LoginCallBack loginCallBack){
        super(context);

        mContext = context;
        mLoginCallBack = loginCallBack;

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mContext.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .enableAutoManage(((FragmentActivity)mContext), new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    public void signIn() {

        CommonUtils.log("Google - signIn()");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        ((Activity)mContext).startActivityForResult(signInIntent, GOOGLE_LOGIN_RQ);
    }

    @Override
    public void silentLogin() {

        CommonUtils.log("Google - silentLogin()");

        isSilentSignIn = true;
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);

        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {

            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {

                    handleSignInResult(googleSignInResult);
                }
            });
        }

    }

    @Override
    public void logout(final SelectListItemListener itemListener) {

        CommonUtils.log("Google - logout()");

        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {

                FirebaseAuth.getInstance().signOut();

                if (mGoogleApiClient.isConnected()){

                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {

                            if (status.isSuccess()){

                                setSelectedOauth(AuthDefine.AUTH_TYPE.NO_SIGNUP);
                                itemListener.onSelectItem("LOGOUT");

                            }

                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        CommonUtils.log("Google - onActivityResult()");
        if (requestCode == GOOGLE_LOGIN_RQ) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result != null) handleSignInResult(result);
        }
    }

    @Override
    public String getToken() {

        if(mAccount != null) {
            return mAccount.getIdToken();
        }

        return "";
    }

    private void handleSignInResult(GoogleSignInResult result){

        CommonUtils.log("Google - handleSignInResult() isSuccess " + result.isSuccess());

        if (result.isSuccess()){

            mAccount = result.getSignInAccount();

            //----------
            // 로그인 성공
            //----------
            setSelectedOauth(AuthDefine.AUTH_TYPE.GOOGLE);
            mLoginCallBack.onLoginResult(AuthDefine.LOGIN_CALLBACK_STATUS.LOGIN_SUCCESS);

        }else {

            if(isSilentSignIn && (result.getStatus().getStatusCode() != 12501)) {

                //----------
                // 토큰 만료
                //----------
                CommonUtils.log( result.getStatus().toString() );
                mLoginCallBack.onLoginResult(AuthDefine.LOGIN_CALLBACK_STATUS.TOKEN_EXPIRED);

            }else {

                //----------
                // 로그인 실패
                //----------
                mLoginCallBack.onLoginResult(AuthDefine.LOGIN_CALLBACK_STATUS.LOGIN_FAIL);
            }
        }

    }
}
