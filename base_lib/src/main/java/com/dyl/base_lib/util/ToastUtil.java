package com.dyl.base_lib.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dyl.base_lib.R;


public class ToastUtil {
    private static String INTERNETERRO = "网络连接缓慢！";
    private static Toast mToast = null;

    public static void toConnectErro(Context context) {
        showMessage(context, INTERNETERRO, Toast.LENGTH_SHORT);
    }

    public static void toast(Context context, String text) {

        showMessage(context, text, Toast.LENGTH_SHORT);
    }

    private static Handler handler = new Handler(Looper.getMainLooper());

    private static Object synObj = new Object();

    public static void showMessage(final Context act, final String msg) {
        cancelToast();
        showMessage(act, msg, Toast.LENGTH_SHORT);
    }

    public static void showMessage(final Context act, final int msg) {
        cancelToast();
        showMessage(act, msg + "", Toast.LENGTH_SHORT);
    }

    public static void showMessages(final Context act, final String msg, final int draw) {
        cancelToast();
        showMessage(act, msg, Toast.LENGTH_SHORT, draw);
    }

    public static void showMessage(final Context act, final String msg, final int len) {
        new Thread(new Runnable() {
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (synObj) {
                            mToast = new Toast(act);
                            View view = LayoutInflater.from(act).inflate(R.layout.toast_view, null);
                            TextView tv = (TextView) view.findViewById(R.id.message);
                            tv.setText(msg);
                            mToast.setView(view);
                            mToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 150);
                            mToast.setDuration(len);
                            mToast.show();
                        }
                    }
                });
            }
        }).start();
    }
    public static void showMessageCenter(final Context act, final String msg, final int len) {
        new Thread(new Runnable() {
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (synObj) {
                            mToast = new Toast(act);
                            View view = LayoutInflater.from(act).inflate(R.layout.toast_view_center, null);
                            TextView tv = (TextView) view.findViewById(R.id.message);
                            tv.setText(msg);
                            mToast.setView(view);
                            mToast.setGravity(Gravity.CENTER, 0, 0);
                            mToast.setDuration(len);
                            mToast.show();
                        }
                    }
                });
            }
        }).start();
    }
    public static void showMessage(final Context act, final String msg, final int len, final int back) {
        new Thread(new Runnable() {
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (synObj) {
                            mToast = new Toast(act);
                            View view = LayoutInflater.from(act).inflate(R.layout.toast_view, null);
                            LinearLayout ll_toast = (LinearLayout) view.findViewById(R.id.ll_toast);
                            TextView tv = (TextView) view.findViewById(R.id.message);
                            ll_toast.setBackgroundResource(back);
                            tv.setText(msg);
                            mToast.setView(view);
                            mToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 120);
                            mToast.setDuration(len);
                            mToast.show();
                        }
                    }
                });
            }
        }).start();
    }
    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

}
