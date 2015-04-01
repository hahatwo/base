package com.example.administrator.base.framegent.pageFramgent;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.administrator.base.ItemCategoryModel;
import com.example.administrator.base.R;
import com.example.administrator.base.adapter.LeftCategoryAdapter;
import com.example.administrator.base.blackwhite.BadSms;
import com.example.administrator.base.blackwhite.SetBad;
import com.example.administrator.base.location.LocationMainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/12/13.
 */
/* 设置左边缘滑出的界面*/
public class LefeCategoryFramgent extends Fragment {
    final boolean WORD=true,NUMBER=false;
    private Intent intent1,intent2;
    private View left_View;
    private Context left_Context;
    private ListView listView_left_category;
    private LeftCategoryAdapter mAdapter;
    private String[] left_category_name;
    private String[] left_category_ename;
    private Integer[] left_category_img;
    private List<ItemCategoryModel> mLists;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState) {
        if (left_View == null) {
            left_View = inflater.inflate(R.layout.left_category, container, false);
            initView();
            initvaliData();
            bindData();
            initListener();
        }

        return left_View;
    }

    private void initView() {
        listView_left_category = (ListView) left_View.findViewById(R.id.listview_left_category);
    }

    private void initvaliData() {
        left_Context = left_View.getContext();
        // 进行模拟和初始化需要进行服务类别设置的数据
        left_category_img = new Integer[]{R.drawable.biz_navigation_tab_local_news, R.drawable.biz_navigation_tab_micro,
                R.drawable.biz_navigation_tab_news, R.drawable.biz_navigation_tab_pics, R.drawable.biz_navigation_tab_ties,
                R.drawable.biz_navigation_tab_ugc,R.drawable.ic_menu_settings_holo_light};

        left_category_name = left_Context.getResources().getStringArray(R.array.array_left_category_name);
        left_category_ename = left_Context.getResources().getStringArray(R.array.array_left_category_ename);
        mLists = new ArrayList<ItemCategoryModel>();

        // 构造要显示的服务类别对象集合
        for (int i = 0; i < left_category_img.length; i++) {
            mLists.add(new ItemCategoryModel(left_category_img[i], left_category_name[i],
                    left_category_ename[i]));

        }
        // 初始化适配器
        mAdapter = new LeftCategoryAdapter(left_Context, mLists);
    }

    /**
     * 绑定数据
     */
    private void bindData() {
        listView_left_category.setAdapter(mAdapter);
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        listView_left_category
                .setOnItemClickListener(new MyOnItemClickListener());
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            intent1= new Intent(left_Context,SetBad.class);
            intent2=new Intent(left_Context,BadSms.class);
           switch (position){
               case 2:
                   intent2.putExtra("clickfrom", 2);
                   left_Context.startActivity(intent2);
                   break;
               case 3:
                   intent2.putExtra("clickfrom", 3);
                   left_Context.startActivity(intent2);
                   break;
               case 4:
                   intent1.putExtra("numorword",NUMBER);
                   left_Context.startActivity(intent1);
                   break;
               case 5:
                   intent1.putExtra("numorword",WORD);
                   left_Context.startActivity(intent1);
                   break;
               case 6:
                   left_Context.startActivity(new Intent(left_Context, LocationMainActivity.class));
                   break;
           }

        }
    }


}
