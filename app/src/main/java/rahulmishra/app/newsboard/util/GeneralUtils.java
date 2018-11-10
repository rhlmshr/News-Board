package rahulmishra.app.newsboard.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import rahulmishra.app.newsboard.NewsBoardApp;
import rahulmishra.app.newsboard.R;
import rahulmishra.app.newsboard.views.activities.MainActivity;
import rahulmishra.app.newsboard.views.activities.WebViewActivity;

public class GeneralUtils {

//    public static int getColor(Context context, int id) {
//        return ContextCompat.getColor(context, id) ;
//    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 150);
        return noOfColumns;
    }

    public static boolean isNetworkConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) NewsBoardApp.instance()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo() != null;
    }

    public static int dpToPx(Context context, int i) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(i * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    public static void showToastMessage(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showProgressDialog(Activity activity, ProgressDialog dialog) {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
            if (dialog == null) {
                dialog = new ProgressDialog(activity);
            }

            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Loading...");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            if (!activity.isFinishing()) dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cancelProgressDialog(ProgressDialog dialog) {
        if (dialog != null) {
            dialog.cancel();
            dialog = null;
        }
    }

    public static void scheduleJob(String title, String content, Bitmap image, String url, Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("ID", "Name", importance);
            notificationManager.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder(context, notificationChannel.getId());
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        builder = builder
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(image)
                        .setBigContentTitle(content))
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);

        Intent notificationIntent = new Intent(context, WebViewActivity.class);

        notificationIntent.putExtra(Constants.INTENT_URL, url);

        TaskStackBuilder TSB = TaskStackBuilder.create(context);
        TSB.addParentStack(MainActivity.class);

        TSB.addNextIntent(notificationIntent);
        PendingIntent resultPendingIntent = TSB.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(12, builder.build());
    }

    public static void ifNullViewGone(TextView view, String text) {
        if (text == null) {
            view.setVisibility(View.GONE);
        } else {
            view.setText(text);
        }
    }
}
