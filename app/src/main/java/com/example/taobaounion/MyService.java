package com.example.taobaounion;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import com.example.taobaounion.ui.activity.MainActivity;

public class MyService extends Service {
    public MyService() {
    }
    AIDL_Service1.Stub mBinder = new AIDL_Service1.Stub() {

        @Override
        public void AIDL_Service() {
            System.out.println("客户端通过AIDL与远程后台成功通信");
        }

    };
    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 创建要显示在通知栏中的通知
        Notification notification = new Notification.Builder(this)
                .setContentTitle("My Foreground Service")
                .setContentText("Running...")
                .setContentIntent(PendingIntent.getActivity(this, 0,
                        new Intent(this, MainActivity.class), 0))
                .build();

        // 将服务设置为前台服务，并显示通知
        startForeground(1, notification);

        // 执行服务中的逻辑

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}