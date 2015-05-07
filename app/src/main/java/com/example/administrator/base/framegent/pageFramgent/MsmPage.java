package com.example.administrator.base.framegent.pageFramgent;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.administrator.base.DataBaseHelper;
import com.example.administrator.base.R;
import com.example.administrator.base.black.SetBad;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2014/12/25.
 */
public class MsmPage extends Fragment {
    DataBaseHelper helper;
    SimpleAdapter adapter;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    private View msmpage_view;
    private Context msmpage_context;
    private ListView msmpage_listview;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.msm_framelayout, null);
        if (msmpage_view == null) {
            msmpage_view = inflater.inflate(R.layout.msm_framelayout, container, false);
            msmpage_context = msmpage_view.getContext();

            initView();
            bindData();

        }
        return msmpage_view;
    }

    private void initView() {
        msmpage_listview = (ListView) msmpage_view.findViewById(R.id.msm_framelayout_list);
        helper = new DataBaseHelper(msmpage_context, "FakeBase");
        SQLiteDatabase db = helper.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM msg_table", null);
            cursor.moveToFirst();
            System.out.print(" 数据库中的行数" + cursor.getCount());
            do {
                if (cursor.getCount() > 0) {
                    String number = cursor.getString(cursor.getColumnIndex("recievenum"));
                    String body = cursor.getString(cursor.getColumnIndex("recievebody"));
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("number", number);
                    map.put("body", body);
                    dataList.add(map);
                }
            } while (cursor.moveToNext());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (db.isOpen()) {
                db.close();
            }
        }
    }


    private void bindData() {
        adapter = new SimpleAdapter(msmpage_context, dataList, R.layout.dealmsg_list_item, new String[]{"number", "body"},
                new int[]{R.id.deal_num, R.id.deal_body});
        //添加并且显示
        msmpage_listview.setAdapter(adapter);
        msmpage_listview.setOnItemLongClickListener(new DeleteListener());
    }


    protected class DeleteListener implements AdapterView.OnItemLongClickListener {

        public boolean onItemLongClick(AdapterView<?> parent, View arg1,
                                       final int position, final long ItemId) {
            final HashMap<String, String> itemmap = (HashMap<String, String>) msmpage_listview.getItemAtPosition(position);
            final String number = itemmap.get("number");
            final String body = itemmap.get("body");
            Toast.makeText(msmpage_context.getApplicationContext(),
                    "你选择了第" + (position + 1) + "个Item，number的值是：" + number + "body的值是:" + body,
                    Toast.LENGTH_SHORT).show();
            new AlertDialog.Builder(msmpage_context)
                    .setNegativeButton("删除",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    DataBaseHelper dbHelper = new DataBaseHelper(
                                            msmpage_context, "FakeBase");
                                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                                    db.delete("msg_table", "recievebody=?", new String[]{body});
                                    dataList.remove(itemmap);
                                    adapter.notifyDataSetChanged();
                                }
                            })
                    .setNeutralButton("添加号码到黑名单", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            DataBaseHelper helper = new DataBaseHelper(msmpage_context, "FakeBase");
                            SQLiteDatabase db = helper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("badnum", number);
                            db.insert("num_table", null, values);
                            Intent intent = new Intent();
                            intent.setClass(msmpage_context, SetBad.class);
                            msmpage_context.startActivity(intent);
                            dialog.dismiss();

                        }
                    })
                    .show();
            return true;
        }

    }
}
