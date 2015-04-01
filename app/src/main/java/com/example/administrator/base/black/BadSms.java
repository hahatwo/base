package com.example.administrator.base.black;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.administrator.base.DataBaseHelper;
import com.example.administrator.base.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class BadSms extends Activity {
    protected ListView list;
    SimpleAdapter adapter;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dealmessage);
        list = (ListView) findViewById(R.id.list);
        DataBaseHelper helper = new DataBaseHelper(BadSms.this, "FakeBase");
        SQLiteDatabase db = helper.getReadableDatabase();
        Intent intent = getIntent();
        int clickfrom = intent.getIntExtra("clickfrom", 0);
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM msg_table", null);
            cursor.moveToFirst();

            do {
                if (cursor.getCount() > 0) {
                    String number = cursor.getString(cursor.getColumnIndex("recievenum"));
                    String body = cursor.getString(cursor.getColumnIndex("recievebody"));
                    String flagstr = cursor.getString(cursor.getColumnIndex("flag"));
                    int flag = Integer.parseInt(flagstr);
                    if (clickfrom == 0) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("number", number);
                        map.put("body", body);
                        dataList.add(map);
                    } else if (flag == clickfrom) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("number", number);
                        map.put("body", body);
                        dataList.add(map);
                    }

                }
            } while (cursor.moveToNext());

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
        //获取适配器
        adapter = new SimpleAdapter(this, dataList, R.layout.dealmsg_list_item, new String[]{"number", "body"},
                new int[]{R.id.deal_num, R.id.deal_body});
        //添加并且显示
        list.setAdapter(adapter);
        list.setOnItemLongClickListener(new DeleteListener());

    }


    /**
     * ****************长按删除，加密监听器*******************************************
     */
    protected class DeleteListener implements AdapterView.OnItemLongClickListener {

        public boolean onItemLongClick(AdapterView<?> parent, View arg1,
                                       final int position, final long ItemId) {
            final HashMap<String, String> itemmap = (HashMap<String, String>) list.getItemAtPosition(position);
            final String number = itemmap.get("number");
            final String body = itemmap.get("body");
            new AlertDialog.Builder(BadSms.this)
                    .setNegativeButton("加密", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DataBaseHelper dbHelper = new DataBaseHelper(
                                    BadSms.this, "FakeBase");
                            SQLiteDatabase db = dbHelper.getReadableDatabase();
                            db.execSQL("UPDATE msgtable  SET flag = 9 WHERE recievebody= " + Arrays.toString(new String[]{body}));
                        }
                    })
                    .setNegativeButton("删除",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    DataBaseHelper dbHelper = new DataBaseHelper(
                                            BadSms.this, "FakeBase");
                                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                                    db.delete("msg_table", "recievebody=?", new String[]{body});
                                    dataList.remove(itemmap);
                                    adapter.notifyDataSetChanged();
                                }
                            })
                    .setNeutralButton("添加号码到黑名单", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            DataBaseHelper helper = new DataBaseHelper(BadSms.this, "FakeBase");
                            SQLiteDatabase db = helper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("badnum", number);
                            db.insert("num_table", null, values);
                            Intent intent = new Intent();
                            intent.setClass(BadSms.this, SetBad.class);
                            BadSms.this.startActivity(intent);
                            dialog.dismiss();
                            BadSms.this.finish();
                        }
                    })
                    .show();
            return true;
        }
    }


}
