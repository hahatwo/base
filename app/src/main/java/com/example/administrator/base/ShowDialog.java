package com.example.administrator.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import com.example.administrator.base.blackwhite.SetBad;


public class ShowDialog extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        final String smsBody = intent.getStringExtra("Body");
        final String smsNumber = intent.getStringExtra("smsNumber");
        final int flag = intent.getIntExtra("flag", 1);
        System.out.print("ShowDialog里面flag======== \n" + flag);
        String trashtype = "疑似垃圾短信，请谨慎处理";
        if (flag == 2) {
            trashtype = "疑似伪基站短信，请谨慎处理";
        } else if (flag == 3) {
            trashtype = "疑似诈骗短信，请谨慎处理";
        } else if (flag == 4) {
            trashtype = "拦截到自定义短信";
        }
        new AlertDialog.Builder(ShowDialog.this)
                .setMessage(trashtype + "\n" + smsBody)
                .setNegativeButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContentValues values = new ContentValues();
                                DataBaseHelper dbhelper = new DataBaseHelper(ShowDialog.this, "FakeBase");
                                SQLiteDatabase db = dbhelper.getWritableDatabase();
                                values.put("recievenum", smsNumber);
                                values.put("recievebody", smsBody);
                                System.out.print("ShowDialog里面插入数据库之前的flag======== \n" + flag);
                                values.put("flag", flag);
                                db.insert("msg_table", null, values);
                                dialog.dismiss();
                                ShowDialog.this.finish();
                            }
                        })
                .setNeutralButton("添加号码到黑名单", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = getIntent();
                        String smsNumber = intent.getStringExtra("smsNumber");
                        DataBaseHelper helper = new DataBaseHelper(ShowDialog.this, "FakeBase");
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("badnum", smsNumber);
                        db.insert("num_table", null, values);
                        intent.setClass(ShowDialog.this, SetBad.class);
                        ShowDialog.this.startActivity(intent);
                        dialog.dismiss();
                        ShowDialog.this.finish();
                    }
                })
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DataBaseHelper helper = new DataBaseHelper(ShowDialog.this, "FakeBase");
                        SQLiteDatabase db = helper.getWritableDatabase();
                        db.delete("msg_table", "recievebody=?", new String[]{smsBody});
                        deleteSMS(ShowDialog.this, smsBody);
                        dialog.dismiss();
                        ShowDialog.this.finish();
                    }
                })
                .show();
    }

    /**
     * *********将短信从系统信箱删除**************************************************
     */

    public void deleteSMS(Context context, String smscontent) {
        try {
            // 准备系统短信收信箱的uri地址
            Uri uri = Uri.parse("content://sms/inbox");// 收信箱
            // 查询收信箱里所有的短信
            Cursor isRead =
                    context.getContentResolver().query(uri, null, "read=" + 0,
                            null, null);
            while (isRead.moveToNext()) {
                // String phone =
                // isRead.getString(isRead.getColumnIndex("address")).trim();//获取发信人
                String body =
                        isRead.getString(isRead.getColumnIndex("body")).trim();// 获取信息内容
                if (body.equals(smscontent)) {
                    int id = isRead.getInt(isRead.getColumnIndex("_id"));

                    context.getContentResolver().delete(
                            Uri.parse("content://sms"), "_id=" + id, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
