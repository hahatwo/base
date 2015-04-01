package com.example.administrator.base.Service;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;


import com.example.administrator.base.DataBaseHelper;
import com.example.administrator.base.Function;
import com.example.administrator.base.ShowDialog;
import com.example.administrator.base.bayes.SMSFilterSystem;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class MsgReceiver extends BroadcastReceiver {
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    String centernumber = "";
    SharedPreferences sp;
    public String SmsNumber = "", SmsBody = "", Body = "";
    boolean isrealbase = false;
    Context context;
    public int complexcount = 0;
    boolean trash = false;
    public boolean contain106 = false;
    int flag = 0;

    public void onReceive(Context context, Intent intent) {
        // 第一步、获取短信的内容和发件人
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        StringBuilder body = new StringBuilder();// 短信内容
        StringBuilder number = new StringBuilder();// 短信发件人

        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                // 将带有puds字符串特征的对象取出
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] message = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    // 通过SmsMessage.createFromPdu()方式取出短信文字
                    message[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                for (SmsMessage currentMessage : message) {
                    // 获得短信内容
                    body.append(currentMessage.getDisplayMessageBody());//append用于在一个字符串末尾加入str
                    number.append(currentMessage.getDisplayOriginatingAddress());
                    //伪基站短信判断
                    centernumber = currentMessage.getServiceCenterAddress();
                }

                //如果前后的centernum相同，则存储centernum的值；
                if (isRealBase(centernumber)) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("centernumber", centernumber);
                    editor.apply();
                }
                System.out.println("centernumber==========" + centernumber);
                //判断前后的centernum是否相同；相同的话isrealbase=false；反之为true；
                isrealbase = (!isRealBase(centernumber));
                intent.setClass(context, fbService.class);
                intent.putExtra("isrealbase", isrealbase);
                SmsBody = body.toString();
                SmsNumber = number.toString();

                // substring是过滤字符串方法
                SmsNumber = SmsNumber.substring(0, SmsNumber.length()
                        / pdus.length);
                if (SmsNumber.contains("+86")) {
                    SmsNumber = SmsNumber.substring(3);
                }
                if (SmsNumber.substring(0, 3).equals("106")) {
                    contain106 = true;
                }


/******************繁体字转化简体***************************************************************/
                Function function = new Function();
                String[] a = function.to_SimpleByComplex(SmsBody);
                SmsBody = a[0];
                complexcount = Integer.valueOf(a[1]);
                System.out.println("转化为简体后为：" + SmsBody);
                //Body是未经处理过的原短信；
                Body = SmsBody;
/**********************************简繁转化***********************************************************/


                /********************checkblack()方法检查黑名单号码和关键字并处理**************************************/

                checkblack(context);
                this.context = context;
                if (trash)
                    dealtrash();
                else {

/******调用贝叶斯模块***********************************************************************************************/
                    SubThread subThread = new SubThread();
                    subThread.start();
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }


    /**
     * 子线程  用来运算本地贝叶斯模块
     *
     * @author Administrator
     */
    private class SubThread extends Thread {

        @Override
        public void run() {

            System.out.println("贝叶斯线程开始");
            String text = Body.trim();
            SMSFilterSystem smsFilterSystem = new SMSFilterSystem();
            System.out.print("最初的Reciver中传入的text是" + text + "\n");
            String resultString = smsFilterSystem.SMSFilter(text);
            if (resultString.equals("good")) {
                Log.i("config", "贝叶斯判断为正常短信");
                //do nothing
                flag = 0;
                System.out.print("Reciever里面的flag======== " + flag);

            } else if (resultString.equals("bad")) {
                Log.i("config", "贝叶斯判断为垃圾短信！");
                trash = true;
                flag = 1;//0代表正常短信，1代表垃圾短信，2代表伪基站短信，3代表诈骗短信，4代表黑名单
                System.out.print("Reciever里面的flag======== " + flag);
            } else if (resultString.equals("诈骗类")) {
                trash = true;
                flag = 3;
                System.out.print("Reciever里面的flag========" + flag);
            } else if (resultString.equals("广告类")) {
                trash = true;
                flag = 1;//0代表正常短信，1代表垃圾短信，2代表伪基站短信，3代表诈骗短信，4代表黑名单
                System.out.print("Reciever里面的flag========" + flag);
            }
            dealtrash();
            Log.i("config", "线程结束");
        }
    }


    //**********查找短信中是否含有自定义的黑名单和关键字******/
    public void checkblack(Context context) {
/*******************处理字符串-----删除标点，去掉空格，停用词***************************************/
        Pattern p = Pattern.compile("[~！@#￥%……&*（）——+·=、】【；‘，。/,.;'|}{、1234567890!$^` 啊我的个中或更您从自往朝向到在于当以为着对连同跟和与比把而地得着了过呢吗啊吧嘛所被喽哎哼喂哦呀恩嗯如请中的将是后那就好在今由]");// 增加对应的标点
        Matcher m = p.matcher(SmsBody);
        SmsBody = m.replaceAll(""); // 把英文标点符号替换成空，即去掉英文标点符号
        SmsBody = SmsBody.replaceAll(" ", "");
/**************************删除停用词************************************************************************/

/***********查找短信中是否含有自定义的黑名单和关键字*********************************************************************************/


        DataBaseHelper dbhelp = new DataBaseHelper(context, "FakeBase");
        SQLiteDatabase dbh = dbhelp.getWritableDatabase();
        Cursor numcursor = dbh.rawQuery("SELECT * FROM num_table", null);
        Cursor wordcursor = dbh.rawQuery("SELECT * FROM word_table", null);
        numcursor.moveToFirst();
        wordcursor.moveToFirst();
        do

        {
            //判断号码在不在黑名单中
            if (numcursor.getCount() > 0) {
                if (SmsNumber.contains(numcursor.getString(numcursor.getColumnIndex("badnum")))) {
                    //若包含则trash=true
                    trash = true;
                    flag = 4;  //0代表正常短信，1代表垃圾短信，2代表伪基站短信，3代表诈骗短信，4代表黑名单
                    System.out.print("Reciever里面的flag======== " + flag);
                    break;
                }
            }
        }

        while (numcursor.moveToNext());
        //判断短信中是否含有自定义的拦截关键字
        do

        {
            if (wordcursor.getCount() > 0) {
                if (SmsBody.contains(wordcursor.getString(wordcursor.getColumnIndex("badword")))) {
                    //若包含则trash=true
                    trash = true;
                    flag = 4;  //0代表正常短信，1代表垃圾短信，2代表伪基站短信，3代表诈骗短信，4代表黑名单
                    System.out.print("Reciever里面的flag======== " + flag);
                    break;
                }
            }
        } while (wordcursor.moveToNext());
    }


    //若包含用户自定义黑名单则处理
    public void dealtrash() {
        if (trash) {
            System.out.print("trash========true \n");
            Intent it = new Intent(context, ShowDialog.class);
            it.putExtra("smsNumber", SmsNumber);
            it.putExtra("Body", Body);
            System.out.print("Reciever里面的flag======== " + flag);
            it.putExtra("flag", flag);    //0代表正常短信，1代表垃圾短信，2代表伪基站短信，3代表诈骗短信，4代表黑名单
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(it);
        } else {
            //将短信的内容和号码加入到数据库的msg_table表的recievebody,recievenum中
            ContentValues values = new ContentValues();
            DataBaseHelper dbhelper = new DataBaseHelper(context, "FakeBase");
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            values.put("recievenum", SmsNumber);
            values.put("recievebody", Body);
            System.out.print("Reciever里面的flag======== " + flag);
            values.put("flag", 0);

            db.insert("msg_table", null, values);
        }
    }


    /**
     * 判断短信是否为真实基站发出
     *
     * @return
     */
    private boolean isRealBase(String gatenumber) {
        //获取短信中心号码
        String centernumber = sp.getString("centernumber", "");
        if (TextUtils.isEmpty(centernumber)) {
            return true;
        } else {
            if (!gatenumber.equals(centernumber)) {
                return false;
            }
        }
        return true;
    }

}

