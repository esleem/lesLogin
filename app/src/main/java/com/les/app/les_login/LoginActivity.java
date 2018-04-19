package com.les.app.les_login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login_A;             // 구글 로그인
    private Button btn_login_B;             // 페북 로그인
    private Button btn_login_C;             // 위쳇 로그인

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //--------------------
        // 로그인 - 구글
        //--------------------
        btn_login_A = (Button) findViewById(R.id.btn_login_A);
        btn_login_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //--------------------
        // 로그인 - 페이스북
        //--------------------
        btn_login_B = (Button) findViewById(R.id.btn_login_B);
        btn_login_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //--------------------
        // 로그인 - 위챗
        //--------------------
        btn_login_C = (Button) findViewById(R.id.btn_login_C);
        btn_login_C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }
}
