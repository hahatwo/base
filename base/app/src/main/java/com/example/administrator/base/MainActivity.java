package com.example.administrator.base;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;


import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.administrator.base.framegent.LefeCategoryFramgent;
import com.example.administrator.base.framegent.pageFramgent.MsmPage;
import com.example.administrator.base.framegent.pageFramgent.PhonePage;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


public class MainActivity extends SlidingFragmentActivity {
    private ImageButton imgButton;
    private ImageView phone_img, msm_img, basest_img;
    private FrameLayout phonePage_layout, msmPage_layout, basestPage_layout;
    private PhonePage phonePage;
    private MsmPage msmPage;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        initSlidingMenu();
        initView();
        initvaliData();
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
        //phonePage_layout = (FrameLayout) findViewById(R.id.layout_frame_phone);

    }

    private void initvaliData() {

    }

    private void bindData() {
    }

    private void initListener() {
        imgButton.setOnClickListener(new MyLeftCategoryListener());
        phone_img.setOnClickListener(new PhoneListener());
        msm_img.setOnClickListener(new MsmListener());
    }

    //ImageView手机信息的onclick响应事件
    class PhoneListener implements View.OnClickListener {
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

        }

    }

   class MsmListener implements View.OnClickListener{
       @Override
       public void onClick(View v) {
           //实例化Fragment页面
          msmPage=new MsmPage();
           //得到Fragment事务管理器
           FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
           //替换当前的页面
           fragmentTransaction.replace(R.id.frame_content1, msmPage);
           //事务管理提交
           fragmentTransaction.commit();
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
