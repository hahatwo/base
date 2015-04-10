package com.example.administrator.base.blackwhite;

import java.util.ArrayList;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.base.DataBaseHelper;
import com.example.administrator.base.R;


public class SetBad extends ActionBarActivity {
    TextView textview;
    Button button;
    ListView listview;
    ArrayList<String> badarraylist = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    String table = "num_table", column = "badnum", dialogmessage = "添加黑名单";
    boolean numorword = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setbad);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textview = (TextView) findViewById(R.id.msgset);
        button = (Button) findViewById(R.id.msgbadbtn);
        listview = (ListView) findViewById(R.id.msgbadlistview);
        button.setText("添加");
/**************判断是添加关键字还是添加黑名单************************************************************/

        Intent intent = getIntent();
        numorword = intent.getBooleanExtra("numorword", false);
        //   word是true，num是false
        if (numorword == true) {
            table = "word_table";
            column = "badword";
            dialogmessage = "添加拦截关键字";
        }

        /***********显示黑名单数据库中的数据*********************************************************/
        DataBaseHelper helper = new DataBaseHelper(SetBad.this, "FakeBase");
        SQLiteDatabase db = helper.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + table, null);
            cursor.moveToFirst();
            do {
                if (cursor.getCount() > 0) {
                    String badstr = cursor.getString(cursor.getColumnIndex(column));
                    badarraylist.add(badstr);
                }
            } while (cursor.moveToNext());

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (db.isOpen()) {
                db.close();
            }
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, badarraylist);
            listview.setAdapter(adapter);
            /*****************************************************************************************/
            listview.setOnItemLongClickListener(new deleteListener());
            button.setOnClickListener(new AddListener());
        }
    }


    /**
     * **************添加自定义黑名单****************************************************************
     */
    protected class AddListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            LayoutInflater inflater = (LayoutInflater) SetBad.this
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            // 获得自定义对话框
            View view = inflater.inflate(R.layout.sqlitedialog, null);
            final EditText edit = (EditText) view.findViewById(R.id.edit);
            edit.setInputType(InputType.TYPE_CLASS_PHONE);
            if (numorword == true) {
                edit.setInputType(InputType.TYPE_CLASS_TEXT);
            }
            // TODO Auto-generated method stub
            final ContentValues values = new ContentValues();
            // 相 该对象当中插入键值对，其中键是列名，值是希望插入到这一列的值，值必须和数据库当中的数据类型一致
            new AlertDialog.Builder(SetBad.this)
                    .setMessage(dialogmessage)
                    .setView(view)
                    .setNeutralButton("确定",
                            new DialogInterface.OnClickListener() {
                                // @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    String editstr = edit.getText().toString();
                                    values.put(column, editstr);

                                    DataBaseHelper dbHelper = new DataBaseHelper(
                                            SetBad.this, "FakeBase", 2);
                                    SQLiteDatabase db = dbHelper
                                            .getWritableDatabase();
                                    // 调用insert方法，就可以将数据插入到数据库当中
                                    db.insert(table, null, values);
                                    badarraylist.add(editstr);
                                    adapter.notifyDataSetChanged();
                                }
                            }).show();
        }
    }

    /**
     * ***********************长按删除监听器******************************************
     */
    protected class deleteListener implements AdapterView.OnItemLongClickListener {

        public boolean onItemLongClick(AdapterView<?> parent, View arg1,
                                       final int position, final long ItemId) {
            final String choosestr = badarraylist.get(position);
            new AlertDialog.Builder(SetBad.this)
                    .setNegativeButton("删除",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    DataBaseHelper dbHelper = new DataBaseHelper(
                                            SetBad.this, "FakeBase");
                                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                                    db.delete(table, column + "=?", new String[]{choosestr});
                                    badarraylist.remove(position);
                                    adapter.notifyDataSetChanged();
                                }
                            })
                    .show();
            return true;
        }
    }


}
