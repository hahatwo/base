package com.example.administrator.base.Service;


import android.app.AlertDialog;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.widget.Toast;

public class fbService extends Service {
    private int test1 = 0; // 信号强度跳变
    private int test2 = 0; // 信号强度太大
    private int test3 = 0; // 频繁的Lac变化
    private int test4 = 0; // Lac，Cid号比较可疑
    private int test5 = 0;//短信中心号码匹配

    private TelephonyManager tm;
    private int signalbefore = 0, lacnow = 0, cidnow = 0, lacbefore = 0,
            signalnow = 0;
    boolean lacchange = false, istimeover = false;
    GsmCellLocation gsmCellLocation;
    GsmCellLocation locationuse;
    long signaltime = 0, lactime = 0;
    boolean isbasereal = true;

    // 绑定客户端，Service默认服务
    public IBinder onBind(Intent intent) {

        isbasereal = intent.getBooleanExtra("isrealbase", true);
        return null;
    }

    @Override
    public void onCreate() {
        settest5();    //判断短信中心号码是否变化
        signaltime = System.currentTimeMillis();// 获取服务创建时的时间
        /********************************************************************************************************************************  */
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        locationuse = (GsmCellLocation) tm.getCellLocation();
        lacnow = locationuse.getLac();// 当前Lac
        lacbefore = lacnow;
        //获取手机信号量
        final TelephonyManager telmanager = (TelephonyManager) fbService.this
                .getSystemService(Context.TELEPHONY_SERVICE);
        // 手机信号变化监听
        PhoneStateListener listen = new PhoneStateListener() {
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {

                if (signalStrength.isGsm()) {
                    // 当前信号强度
                    signalnow = -113 + 2
                            * signalStrength.getGsmSignalStrength();
                    //当前信号强度大于-60的话将test2的值赋为50
                    if (signalnow > (-60))
                        test2 = 50;    //test2表示信号强度太大
                    signaltime = istimeover(20000, signaltime);
                    // 如果信号变化前后的时间差相隔20秒以上
                    if (istimeover) {
                        signalbefore = signalnow;
                        test1 = 0;//20秒以上说明正常，test1 表示信号强度跳变
                        System.out.print("istimeover=" + istimeover + "\n");
                    }

                    // 若是信号变化在20之内
                    else {
                        System.out.print("istimeover=" + istimeover + "\n");
                        // 信号强度升上升超过15的将test1 设置为60
                        if ((signalnow - signalbefore) >= 15) {
                            test1 = 60;

                        }
                        // 信号强度下降超过15的将test1设为60
                        else if ((signalbefore - signalnow) >= 15) {
                            test1 = 60;
                        }
                        //虽然变化是在20秒之间，但是变化的幅度较小，属于正常的范围
                        else {
                            test1 = 0;
                        }
                        signalbefore = signalnow;
                    }
                    Toast toast = Toast.makeText(
                            fbService.this,
                            "信号" + String.valueOf(signalnow) + "\n" + "LAC:" + String.valueOf(lacnow) + "\n" + "CID: " + String.valueOf(cidnow),
                            Toast.LENGTH_SHORT);// 测试用于显示
                    toast.show();
                } else {
                    Toast toast1 = Toast.makeText(getApplicationContext(),
                            "该手机不是GSM", Toast.LENGTH_SHORT);
                    toast1.show();
                }

            }

            /*********************************************************************************************************************/
            // Lac Cid 变化的监听器
            @Override
            public void onCellLocationChanged(CellLocation location) {

                gsmCellLocation = (GsmCellLocation) location;
                lacnow = gsmCellLocation.getLac();
                cidnow = gsmCellLocation.getCid();
                // Lac发生变化
                if (lacbefore != lacnow) {
                    lacchange = true;
                }
                // 20秒之内变化
                lactime = istimeover(20000, lactime);
                if (!istimeover && lacchange) {
                    test3 = 60;   //test3 表示频繁的lac变化
                    // 在20秒之内变回原来的Lac，说明有问题
                    if (lacnow == lacbefore) {
                        showDialog();
                    }
                    lacbefore = lacnow;
                }
                //若时间超过20秒
                if (istimeover) {
                    //Lac变化了
                    if (lacbefore != lacnow) {
                        test3 = 30;
                    } else {
                        test3 = 0;
                    }
                }
            }

            @Override
            public void onDataActivity(int direction) {

            }

            @Override
            public void onDataConnectionStateChanged(int state) {
                //
                // super.onDataConnectionStateChanged(state);
            }

        };
        // phoneListener完
        // 启动监听
        telmanager.listen(listen, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
                | PhoneStateListener.LISTEN_CELL_LOCATION
                | PhoneStateListener.LISTEN_DATA_ACTIVITY
                | PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);


        /**************利用handler让某些函数隔一段时间执行一次*******************************************************/

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // 在此处添加执行的代码
                go();
                // isdanger();
                handler.postDelayed(this, 3000);// 1000ms是延时时长,每隔1000执行一次.
            }
        };
        handler.postDelayed(runnable, 50);// 打开定时器，执行操作
        //  handler.removeCallbacks(runnable);// 关闭定时器处理
    }// onCreate 结束


    /**
     * 主要的功能函数 ************************************************************************************************
     */
    public void go() {
        // 出现绝对不可能的Lac和可疑的Lac；
        set1();
        //判断总的可疑度
        System.out.println("test1=" + test1 + " test2=" + test2 + " test3="
                + test3 + " test4=" + test4 + "test5" + test5);
    }

    /**
     * **************************************************************************************
     */
    // 判断时间是否超出ctime秒
    private long istimeover(long ctime, long time) {
        if (System.currentTimeMillis() - time >= ctime) {
            istimeover = true;
            time = System.currentTimeMillis();
            System.out.println("时间 over 20秒");
            return time;
        } else {
            istimeover = false;
            System.out.println("时间 20秒 之内");
            time = System.currentTimeMillis();
            return time;
        }


    }


    /**
     * *********************************************************************************************
     */
    private void showDialog() {
        new AlertDialog.Builder(fbService.this)
                .setTitle("Warnning!!")
                .setMessage("可能被伪基站劫持！")
                .setNegativeButton("我知道了", null);
    }

    /**
     * ****************************************************************************************
     */
    // 出现绝对不可能的Lac
    private void set1() {

        if (cidnow == 10 || lacnow == 65535 || lacnow == 0 || lacnow == -1
                || cidnow == -1) {
            test4 = 100;  //test4 表示可疑的Cid或者Lac
            showDialog();
        }
        if (lacnow >= 1000 && lacnow <= 9999) {
            test4 = 60;
        }
    }

    /**
     * ****总体评估可疑度**********************************************************************************************
     */
    public void isdanger() {
        if (test1 + test2 + test3 + test4 >= 100) {
            System.out.println("dangerous!!!!!!!!!!!!!!!!!!!!!");
            showDialog();
        } else {
            test4 = 0;
        }
    }

    void settest5() {

        if (isbasereal) {
            test5 = 0;
        } else {
            test5 = 70;
        }
    }

}// 类结束


/****************************test之间的量还未协调好，并且有些量还不可以隔一段时间运行************/