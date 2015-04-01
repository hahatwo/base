package com.example.administrator.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.administrator.base.R;

import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * 创建ListView的适配器
 * Created by Administrator on 2014/12/21.
 */
public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, Object>> listItems;
    private LayoutInflater inflater;


    //自定义ListView控件
    private static class Holder{
        ImageView imgItem_list;
        TextView titleItem_list;
        TextView textItem_list;
    }

    public  ListViewAdapter(Context context, List<Map<String, Object>> listItems) {
       this.context=context;
        inflater=LayoutInflater.from(this.context);
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder _Holder=null;
        if(convertView==null){
            _Holder=new Holder();
            convertView=inflater.inflate(R.layout.list_view,null);
            _Holder.imgItem_list=(ImageView)convertView.findViewById(R.id.imgItem);
            _Holder.titleItem_list=(TextView)convertView.findViewById(R.id.titleItem);
            _Holder.textItem_list=(TextView)convertView.findViewById(R.id.textItem);
            convertView.setTag(_Holder);
        }
        else _Holder=(Holder)convertView.getTag();
        _Holder.imgItem_list.setBackgroundResource((Integer) listItems.get(position).get("imgItem_list"));
        _Holder.titleItem_list.setText((String)listItems.get(position).get("titleItem_list"));
        _Holder.textItem_list.setText((String)listItems.get(position).get("textItem_list"));
        return convertView;
    }


}
