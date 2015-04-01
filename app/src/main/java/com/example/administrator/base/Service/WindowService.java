package com.example.administrator.base.Service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.view.WindowManager;

import com.example.administrator.base.Window.MyWindowManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Administrator on 2015/3/22.
 */
public class WindowService extends Service{

    /**
    *用于线程中创建悬浮窗
     */
    private Handler handler=new Handler();

    /**
     * 创建定时器，定期检查是否创建或者移除悬浮窗
     */
    private Timer timer;

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        if(timer==null){
            timer=new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(),0,500);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Service被终止的同时也停止定时器继续运行
        timer.cancel();
        timer = null;
    }

    class RefreshTask extends TimerTask{
        @Override
        public void run() {

            // 当前界面是桌面，且没有悬浮窗显示，则创建悬浮窗。
            if (isHome() && !MyWindowManager.isWindowShowing()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyWindowManager.createSmallWindow(getApplicationContext());
                    }
                });
            }
            // 当前界面不是桌面，且有悬浮窗显示，则移除悬浮窗。
            else if (!isHome() && MyWindowManager.isWindowShowing()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyWindowManager.removeSmallWindow(getApplicationContext());
                        MyWindowManager.removeBigWindow(getApplicationContext());
                    }
                });
            }
            // 当前界面是桌面，且有悬浮窗显示，则更新内存数据。
            else if (isHome() && MyWindowManager.isWindowShowing()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyWindowManager.updateUsedPercent(getApplicationContext());
                    }
                });
            }

        }

        /*
        **判断当前界面是否为桌面
         */
        private boolean isHome(){

            ActivityManager activityManager=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> rti = activityManager.getRunningTasks(1);
            return getHomes().contains(rti.get(0).topActivity.getPackageName());

        }

        /*
        **获得属于桌面应用的应用包名称
         */
         private List<String> getHomes(){
             List<String> names=new ArrayList<String>();
             PackageManager packageManager=getPackageManager();
             Intent intent=new Intent(Intent.ACTION_MAIN);
             intent.addCategory(Intent.CATEGORY_HOME);
             List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY);
           for (ResolveInfo ri : resolveInfo) {
               names.add(ri.activityInfo.packageName);
                 }
             return names;
         }
    }

}
