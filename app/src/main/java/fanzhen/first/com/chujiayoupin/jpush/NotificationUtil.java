package fanzhen.first.com.chujiayoupin.jpush;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;

public class NotificationUtil {
    public static final String SETTINGS_ACTION =
            "android.settings.APPLICATION_DETAILS_SETTINGS";
    public static boolean isOpenNotification(Context context){
        NotificationManagerCompat notification = NotificationManagerCompat.from(context);
        boolean isEnabled = notification.areNotificationsEnabled();
        return isEnabled;
    }
    public  static  void setNotification(Context context){
        if(isOpenNotification(context))
            return;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
            context.startActivity(intent);
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
            Intent intent = new Intent()
                    .setAction(SETTINGS_ACTION)
                    .setData(Uri.fromParts("package",
                            context.getApplicationContext().getPackageName(), null));
            context.startActivity(intent);
            return;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent()
                    .setAction(SETTINGS_ACTION)
                    .setData(Uri.fromParts("package",
                            context.getApplicationContext().getPackageName(), null));
            context.startActivity(intent);
            return;
        }
    }
}
