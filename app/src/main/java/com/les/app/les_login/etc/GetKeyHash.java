package com.les.app.les_login.etc;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

import com.kakao.auth.helper.Base64;
import com.les.app.les_login.utils.CommonUtils;

import java.security.MessageDigest;

public class GetKeyHash {

    public static void getKeyHash(Context context)
    {
        try{
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for(Signature signature : info.signatures){
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());

                CommonUtils.log("@# " + Base64.encodeBase64URLSafeString(messageDigest.digest()));
            }
        } catch (Exception e){
            Log.d("error", "PackageInfo error is " + e.toString());
        }
    }

}
