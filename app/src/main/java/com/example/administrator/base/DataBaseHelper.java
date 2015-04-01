package com.example.administrator.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
//SQL构造类，里面有几个构造函数，不用管它


public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;

    public DataBaseHelper(Context context, String name, CursorFactory factory,
                          int version) {
        //必须通过Super调用父类的构造函数
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }

    public DataBaseHelper(Context context, String name) {
        this(context, name, null, VERSION);
    }

    public DataBaseHelper(Context context, String name, int version) {
        this(context, name, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        // SQLite默认的是数据库存在则不会再执行创建数据库操作，仅首次执行会创建，所以仅打印一次。
        System.out.println("数据库创建成功");
        //exeSQL用于执行SQL语句
//		数据库名称是FakeBase，目前只创建了两个表，num_table(黑名单) ,word_table（关键字），第一列是id（自增长），第二列是用户输入的关键字或黑名单号码。

        db.execSQL("Create table num_table(id INTEGER PRIMARY KEY AUTOINCREMENT, badnum varchar(30) UNIQUE)");

        db.execSQL("Create table word_table(id INTEGER PRIMARY KEY AUTOINCREMENT, badword varchar(30)UNIQUE )");

        //0代表正常短信，1代表垃圾短信，2代表伪基站短信，3代表诈骗短信，4代表黑名单
        db.execSQL("Create table msg_table(id INTEGER PRIMARY KEY AUTOINCREMENT, recievenum varchar(30),recievebody varchar(150),flag int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
