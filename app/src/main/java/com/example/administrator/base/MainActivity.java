package com.example.administrator.base;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.administrator.base.Service.fbService;
import com.example.administrator.base.framegent.pageFramgent.LefeCategoryFramgent;
import com.example.administrator.base.framegent.pageFramgent.BasestPage;
import com.example.administrator.base.framegent.pageFramgent.MsmPage;
import com.example.administrator.base.framegent.pageFramgent.PhonePage;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


public class MainActivity extends SlidingFragmentActivity {
    private ImageButton imgButton;
    private ImageView phone_img, msm_img, basest_img;
    private PhonePage phonePage;
    private MsmPage msmPage;
    private BasestPage basestPage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        // 创建一个database对象，SQLite默认的是数据库存在则不会再执行创建数据库操作，仅首次执行会创建

        DataBaseHelper dbHelper = new DataBaseHelper(MainActivity.this,
                "FakeBase");
        // 只有调用了databasehelper对象的getreadabledatabase（）方法或是getwriteabledatabase方法才会创建或打开数据库
        //SQLiteDatabase db = dbHelper.getReadableDatabase();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        initSlidingMenu();
        initView();
        Intent intent = new Intent(MainActivity.this, fbService.class);
        startService(intent);
        // initvaliData();
        initListener();
    }


    /**
     * 初始化SlidingMenu视图
     */
    private void initSlidingMenu() {
        // 设置滑动菜单的属性值
        getSlidingMenu().setMode(SlidingMenu.LEFT);
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        getSlidingMenu().setShadowWidthRes(R.dimen.shadow_width);
        getSlidingMenu().setShadowDrawable(R.drawable.shadow);
        getSlidingMenu().setBehindOffsetRes(R.dimen.slidingmenu_offset);
        getSlidingMenu().setFadeDegree(0.35f);
        //设置主界面的视图
        setContentView(R.layout.activity_main);
        // 设置左边菜单打开后的视图界面
        setBehindContentView(R.layout.left_content);
        getSupportFragmentManager().beginTransaction().replace(R.id.left_content_id, new LefeCategoryFramgent()).commit();
    }

    private void initView() {
        imgButton = (ImageButton) findViewById(R.id.imgButton);
        phone_img = (ImageView) findViewById(R.id.phone_img);
        msm_img = (ImageView) findViewById(R.id.msm_img);
        basest_img = (ImageView) findViewById(R.id.basest_img);


    }

    private void initvaliData() {
        Intent intent = new Intent(MainActivity.this, fbService.class);
        startService(intent);
    }


    private void initListener() {
        imgButton.setOnClickListener(new MyLeftCategoryListener());
        phone_img.setOnClickListener(new PhoneListener());
        msm_img.setOnClickListener(new MsmListener());
        basest_img.setOnClickListener(new BasestListener());
    }

    //ImageView手机信息的onclick响应事件
    class PhoneListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //实例化Fragment页面
            basestPage = new BasestPage();
            //得到Fragment事务管理器
            FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
            //替换当前的页面
            fragmentTransaction.replace(R.id.frame_content1, basestPage);
            //事务管理提交
            fragmentTransaction.commit();
            // basest_img.setImageResource(R.drawable.base_before);
            //   msm_img.setImageResource(R.drawable.sms_before);
            // phone_img.setImageResource(R.drawable.phone_after);


        }

    }

    //ImageView收件箱的onclick响应事件
    class MsmListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //实例化Fragment页面
            msmPage = new MsmPage();
            //得到Fragment事务管理器
            FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
            //替换当前的页面
            fragmentTransaction.replace(R.id.frame_content1, msmPage);
            //事务管理提交
            fragmentTransaction.commit();
            // basest_img.setImageResource(R.drawable.base_before);
            // msm_img.setImageResource(R.drawable.sms_after);
            // phone_img.setImageResource(R.drawable.phone_before);
        }
    }

    class BasestListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            //实例化Fragment页面
            phonePage = new PhonePage();
            //得到Fragment事务管理器
            FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
            //替换当前的页面
            fragmentTransaction.replace(R.id.frame_content1, phonePage);
            //事务管理提交
            fragmentTransaction.commit();
            // basest_img.setImageResource(R.drawable.base_after);
            //  msm_img.setImageResource(R.drawable.sms_before);
            //  phone_img.setImageResource(R.drawable.phone_before);
        }
    }


    // 进行侧滑界面打开与关闭
    class MyLeftCategoryListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            toggle();
        }
    }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
