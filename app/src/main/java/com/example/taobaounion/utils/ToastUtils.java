package com.example.taobaounion.utils;

import android.widget.Toast;
import com.example.taobaounion.base.BaseApplication;

/**
 * 为了防止Toast使用出错(多个Toast同时调用出错-->具体表现为会持续很长一段时间的Toast.show())
 * 这里建一个Toast工具类来统一管理Toast
 */
public class ToastUtils {
    public static Toast sToast;
    public static void showToast(String tips) {
        if (sToast == null) {
         sToast = Toast.makeText(BaseApplication.getAppContext(), tips, Toast.LENGTH_SHORT);
        }
        else{
            sToast.setText(tips);
        }
        sToast.show();
    }
}
