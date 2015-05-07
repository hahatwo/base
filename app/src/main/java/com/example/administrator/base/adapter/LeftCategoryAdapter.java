package com.example.administrator.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.base.ItemCategoryModel;
import com.example.administrator.base.R;

import java.util.List;

/**
 * Created by Administrator on 2014/12/16.
 */
//设置左边缘滑出界面的adapter
public class LeftCategoryAdapter extends BaseAdapter {
    private Context mContext;
    private List<ItemCategoryModel> mLists;
    private LayoutInflater mLayoutInflater;
    private int height;


    public LeftCategoryAdapter(Context mContext, List<ItemCategoryModel> mLists) {
        this.mContext = mContext;
        this.mLists = mLists;
        mLayoutInflater = LayoutInflater.from(this.mContext);
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        height = windowManager.getDefaultDisplay().getHeight();
    }

    @Override
    public int getCount() {
        return mLists != null ? mLists.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder _Holder = null;
       // LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, height / 7);

        if (convertView == null) {
            _Holder = new Holder();
            convertView = mLayoutInflater.inflate(R.layout.left_category_item, null);
            //convertView.setLayoutParams(layoutParams);
            _Holder.left_category_item_img = (ImageView) convertView.findViewById(R.id.left_categroy_item_img);
            _Holder.left_category_item_name = (TextView) convertView.findViewById(R.id.left_category_item_name);
            _Holder.left_category_item_ename = (TextView) convertView.findViewById(R.id.left_category_item_ename);
            convertView.setTag(_Holder);
        } else _Holder = (Holder) convertView.getTag();
        _Holder.left_category_item_img.setImageResource(mLists.get(position).getId());
        _Holder.left_category_item_name.setText(mLists.get(position).getName());
        _Holder.left_category_item_ename.setText(mLists.get(position).getTitle());
        return convertView;
    }

    private static class Holder {
        ImageView left_category_item_img;
        TextView left_category_item_name;
        TextView left_category_item_ename;
    }
}



