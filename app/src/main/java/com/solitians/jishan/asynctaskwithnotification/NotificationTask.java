package com.solitians.jishan.asynctaskwithnotification;

/**
 * Created by Jishan on 17-01-2018.
 */
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.RemoteViews;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NotificationTask extends AsyncTask<String, Double, Void> {

    private final static String TAG = NotificationTask.class.getName();
    private NotificationCompat.Builder mBuilder;
    private final Context mContext;
    private final int mId;
    int sizeof;
    int pro;
    int downloadsize;
    private NotificationManager mNotifyManager;
    private static double SPACE_KB = 1024;
    private static double SPACE_MB = 1024 * SPACE_KB;
    private static double SPACE_GB = 1024 * SPACE_MB;
    private static double SPACE_TB = 1024 * SPACE_GB;

    public NotificationTask(Context context, int id) {
        mContext = context;
        mId = id;
    }

    private void initNotification() {
        mNotifyManager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(mContext);
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage());
        }
        setCompletedNotification();
    }

    @Override
    protected Void doInBackground(String... params) {
        int count = 0;
        try {
            URL url = new URL(params[0]);
            URLConnection conection = url.openConnection();
            conection.connect();
            int lenghtOfFile = conection.getContentLength();
            sizeof=conection.getContentLength();
            Log.d(TAG, "doInBackground: "+sizeof);
            InputStream input = new BufferedInputStream(url.openStream(), 8192);
            OutputStream output = new FileOutputStream("/sdcard/downloadedfile.jpg");
            byte data[] = new byte[1024];
            Double total = Double.valueOf(0);
            while ((count = input.read(data)) != -1) {
                total += count;
                downloadsize= (int) total.longValue();
                pro= (int) (total*100/lenghtOfFile);
                publishProgress(total*100/lenghtOfFile);
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "onPreExecute");
        super.onPreExecute();
        initNotification();
        setStartedNotification();

    }

    @Override
    protected void onProgressUpdate(Double... values) {
        super.onProgressUpdate(values);
            setProgressNotification();
        updateProgressNotification();
    }


    private void setCompletedNotification() {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),
                R.layout.custon_notification_view);
        mBuilder.setContent(remoteViews);
        remoteViews.setTextViewText(R.id.downloading,"Downloaded");
        remoteViews.setTextViewText(R.id.downloadpercent,"100%");
        remoteViews.setTextViewText(R.id.textView5,bytes2String(downloadsize)+"   "+"/");
        remoteViews.setProgressBar(R.id.progressBar,100,100,false);
        Intent resultIntent = new Intent(mContext, Result.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        mNotifyManager.notify(mId, mBuilder.build());
    }

    private void setProgressNotification() {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),
                R.layout.custon_notification_view);
        mBuilder.setContent(remoteViews);
        remoteViews.setTextViewText(R.id.downloading,"Download in Progress");
        remoteViews.setTextViewText(R.id.downloadsize,bytes2String(Long.parseLong(String.valueOf(sizeof))));
        mNotifyManager.notify(mId, mBuilder.build());
    }


    private void setStartedNotification() {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),
                R.layout.custon_notification_view);
        mBuilder.setSmallIcon(R.drawable.ic_launcher_foreground).setContent(remoteViews);
        Intent resultIntent = new Intent(mContext, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        mNotifyManager.notify(mId, mBuilder.build());
    }


    private void updateProgressNotification() {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),
                R.layout.custon_notification_view);
        mBuilder.setContent(remoteViews);
        remoteViews.setProgressBar(R.id.progressBar,sizeof,downloadsize,false);
        remoteViews.setTextViewText(R.id.textView5,bytes2String(downloadsize)+"   "+"/");
        remoteViews.setTextViewText(R.id.downloadpercent,""+pro+"%");
        mNotifyManager.notify(mId, mBuilder.build());

    }
    public static String bytes2String(long sizeInBytes) {
        NumberFormat nf = new DecimalFormat();
        nf.setMaximumFractionDigits(2);
        try {
            if ( sizeInBytes < SPACE_KB ) {
                return nf.format(sizeInBytes) + " Byte(s)";
            } else if ( sizeInBytes < SPACE_MB ) {
                return nf.format(sizeInBytes/SPACE_KB) + " KB";
            } else if ( sizeInBytes < SPACE_GB ) {
                return nf.format(sizeInBytes/SPACE_MB) + " MB";
            } else if ( sizeInBytes < SPACE_TB ) {
                return nf.format(sizeInBytes/SPACE_GB) + " GB";
            } else {
                return nf.format(sizeInBytes/SPACE_TB) + " TB";
            }
        } catch (Exception e) {
            return sizeInBytes + " Byte(s)";
        }

    }

}