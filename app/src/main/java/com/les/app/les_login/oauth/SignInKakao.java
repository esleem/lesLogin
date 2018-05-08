package com.les.app.les_login.oauth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.IPushConfig;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.les.app.les_login.base.listener.SelectListItemListener;
import com.les.app.les_login.utils.CommonUtils;

public class SignInKakao extends  SignIn{

    private Context mContext;
    private AuthDefine.LoginCallBack mLoginCallBack;
    private SessionCallback mSessionCallback;

    protected SignInKakao(Context context, AuthDefine.LoginCallBack loginCallBack) {
        super(context);

        this.mContext = context;
        this.mLoginCallBack = loginCallBack;


        if(KakaoSDK.getAdapter() == null) {

            KakaoSDK.init(new KakaoSDKAdapter(mContext.getApplicationContext()));
        }

        mSessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(mSessionCallback);
    }

    @Override
    public void signIn() {

        CommonUtils.log("Kakao - signIn()");
        Session.getCurrentSession().open(AuthType.KAKAO_TALK_ONLY, (Activity) mContext);

    }

    @Override
    public void silentLogin() {

        CommonUtils.log("Kakao - silentLogin()");
        Session.getCurrentSession().checkAndImplicitOpen();
    }

    @Override
    public void logout(final SelectListItemListener itemListener) {

        CommonUtils.log("Kakao - logout()");

        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {

                setSelectedOauth(AuthDefine.AUTH_TYPE.NO_SIGNUP);
                itemListener.onSelectItem("LOGOUT");
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        CommonUtils.log("Kakao - onActivityResult()");
        Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    public String getToken() {

        CommonUtils.log("Kakao - getToken() : " + Session.getCurrentSession().getTokenInfo().getAccessToken());
        return Session.getCurrentSession().getTokenInfo().getAccessToken();
    }

    @Override
    public String getUserName() {

        String nickName = UserProfile.loadFromCache().getNickname();
        CommonUtils.log("Kakao - getUserName() : " + nickName);

        return nickName;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        CommonUtils.log("Kakao - onDestroy()");
        Session.getCurrentSession().removeCallback(mSessionCallback);
    }

    //--------------------------------------------------
    //
    //--------------------------------------------------
    private void requestMe(){

        UserManagement.getInstance().requestMe(new MeResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {

                CommonUtils.log("requestMe - onSessionClosed");
                setSelectedOauth(AuthDefine.AUTH_TYPE.NO_SIGNUP);
            }

            @Override
            public void onNotSignedUp() {

                CommonUtils.log("requestMe - onNotSignedUp");
                setSelectedOauth(AuthDefine.AUTH_TYPE.NO_SIGNUP);
            }

            @Override
            public void onSuccess(UserProfile result) {

                CommonUtils.log("requestMe - onSuccess");
                setSelectedOauth(AuthDefine.AUTH_TYPE.KAKAO);
                mLoginCallBack.onLoginResult(AuthDefine.LOGIN_CALLBACK_STATUS.LOGIN_SUCCESS);
            }
        });
    }

    //--------------------------------------------------
    // KakaoAdapter
    //--------------------------------------------------
    private class KakaoSDKAdapter extends KakaoAdapter{

        private Context mContext;

        public KakaoSDKAdapter(Context context) {
            super();

            this.mContext = context;
        }

        @Override
        public IApplicationConfig getApplicationConfig() {
            return new IApplicationConfig(){
                @Override
                public Context getApplicationContext() {
                    return mContext;
                }
            };
        }

        @Override
        public IPushConfig getPushConfig() {
            return new IPushConfig() {
                @Override
                public String getDeviceUUID() {
                    return null;
                }

                @Override
                public ApiResponseCallback<Integer> getTokenRegisterCallback() {
                    return null;
                }
            };
        }

        @Override
        public ISessionConfig getSessionConfig() {
            return new ISessionConfig() {
                @Override
                public AuthType[] getAuthTypes() {
                    return new AuthType[] {AuthType.KAKAO_LOGIN_ALL};
                }

                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                }

                @Override
                public boolean isSecureMode() {
                    return false;
                }

                @Override
                public ApprovalType getApprovalType() {
                    return ApprovalType.INDIVIDUAL;
                }

                @Override
                public boolean isSaveFormData() {
                    return false;
                }
            };
        }
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            CommonUtils.log("Kakao Session CB - onSessionOpened");
            requestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                CommonUtils.log("Kakao Session CB - onSessionOpenFailed : " + exception.getMessage());
            }
        }
    }

}
