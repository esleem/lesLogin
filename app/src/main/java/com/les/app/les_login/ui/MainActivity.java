package com.les.app.les_login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.les.app.les_login.R;
import com.les.app.les_login.base.listener.SelectListItemListener;
import com.les.app.les_login.utils.CommonUtils;

import java.util.Locale;

import static com.les.app.les_login.oauth.SignIn.mSignin;

public class MainActivity extends AppCompatActivity {

    private TextView tv_userName;
    private Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);

        //--------------------
        // 기본 셋팅
        //--------------------
        defaultSetting();

        //--------------------
        // Rx Android
        //--------------------

    }


    //--------------------------------------------------
    // 기본 셋팅
    //--------------------------------------------------
    private void defaultSetting(){

        //--------------------
        // 폰번호 유틸
        //--------------------
        String oPhone = "01012345678";
        String phone = PhoneNumberUtils.formatNumber(oPhone, Locale.getDefault().getCountry());
        CommonUtils.log("@# PHONE : "+ phone);

        //--------------------
        // 로그아웃
        //--------------------
        btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSignin.logout(new SelectListItemListener(){
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
            }
        });

        //--------------------
        // 유저 정보 출력
        //--------------------
        tv_userName = (TextView) findViewById(R.id.tv_userName);
        tv_userName.setText(mSignin.getUserName());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSignin.onDestroy();
    }
}
