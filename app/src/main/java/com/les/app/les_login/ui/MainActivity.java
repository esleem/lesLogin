package com.les.app.les_login.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import com.les.app.les_login.R;
import com.les.app.les_login.base.listener.SelectListItemListener;
import com.les.app.les_login.databinding.AcMainBinding;
import com.les.app.les_login.model.UserModel;
import com.les.app.les_login.utils.CommonUtils;
import com.les.app.myfirstlibrary.LoadingDialog;
import com.les.app.mysecondlibrary.SecondUtils;

import java.util.Locale;

import static com.les.app.les_login.oauth.SignIn.mSignin;

public class MainActivity extends AppCompatActivity {

    private AcMainBinding acMainBinding;

    private UserModel mUserModel = new UserModel();
    private int mCnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //--------------------
        // Rx Android
        //--------------------
        acMainBinding = DataBindingUtil.setContentView(this, R.layout.ac_main);
        acMainBinding.setUser(mUserModel);

        //--------------------
        // 기본 셋팅
        //--------------------
        defaultSetting();

    }


    //--------------------------------------------------
    // 기본 셋팅
    //--------------------------------------------------
    private void defaultSetting(){

        //--------------------
        // 버튼 - 로그아웃
        //--------------------
        acMainBinding.btnLogout.setOnClickListener(view -> {

            mSignin.logout(new SelectListItemListener() {
                @Override
                public void onSelectItem(String position) {
                    if ("LOGOUT".equals(position)) {

                        finish();
                    }
                }

                @Override
                public void onSelectItem(int position) {

                }
            });

        });

        //--------------------
        // 버튼 - 체인지
        //--------------------
        acMainBinding.btnChange.setOnClickListener(v -> {

            mCnt++;

            String mCntStr = mCnt + "";

            int sum = 0;
            String[] strings = mCntStr.split("");
            for (String conso : strings){

                if ("".equals(conso)) conso = "0";
                int a = Integer.parseInt(conso);
                sum += a;

            }

            CommonUtils.log("@# sum : " + sum);
            mCntStr = sum + "";
            mCntStr = mCntStr.substring(mCntStr.length() - 1, mCntStr.length());

            String notDash = mUserModel.getPhone().replaceAll("-", "");
            notDash = notDash.substring(0, notDash.length() - 1);

            notDash += mCntStr;

            String nameTarget = mCnt + "name";
            String phoneTarget = PhoneNumberUtils.formatNumber(notDash, Locale.getDefault().getCountry());

            mUserModel.setPhone(phoneTarget);
            mUserModel.setName(nameTarget);

            CommonUtils.log("@# n : " + mUserModel.getName() + " / p : " + mUserModel.getPhone());
        });

        //--------------------
        // 유저 정보 출력
        //--------------------
        mUserModel.setName(mSignin.getUserName());

        //--------------------
        // 폰번호 유틸
        //--------------------
        String oPhone = "01012345678";
        String phone = PhoneNumberUtils.formatNumber(oPhone, Locale.getDefault().getCountry());
        CommonUtils.log("@# PHONE : "+ phone);
        mUserModel.setPhone(phone);

        LoadingDialog loadingDialog = new LoadingDialog();
        SecondUtils utils = new SecondUtils();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSignin.onDestroy();
    }
}
