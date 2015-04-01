package com.example.administrator.base.framegent.pageFramgent;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.administrator.base.R;
import com.example.administrator.base.adapter.ListViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014/12/21.
 */
public class PhonePage extends Fragment {
    private View phonepage_view;
    private Context phonepage_context;
    private ListView phonepage_listview;
    private ListViewAdapter phonepage_adapter;
    private List<Map<String, Object>> listItems;
    private Integer[] listview_img;//lsitview图片
    private String[] listview_title;//listview标题
    private String[] listview_text;//listview信息
    private TelephonyManager telephony;
    private GsmCellLocation gsmcelllocation;
    String MyLac, MyCellID, NeighborLac, NeighborCellID , NeighborStrength;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (phonepage_view == null) {
            phonepage_view = inflater.inflate(R.layout.phone_framelayout, container, false);
            phonepage_context = phonepage_view.getContext();
            telephony =(TelephonyManager)phonepage_context.getSystemService(Context.TELEPHONY_SERVICE);
            gsmcelllocation = (GsmCellLocation)telephony.getCellLocation();
            /**通过GsmCellLocation获取中国移动和联通 LAC 和cellID */
            MyLac = "" + gsmcelllocation.getLac();
            MyCellID = "" + gsmcelllocation.getCid();
            int strength = 0;
            /**通过getNeighboringCellInfo获取BSSS */
            List<NeighboringCellInfo> infoLists = telephony.getNeighboringCellInfo();
            for (NeighboringCellInfo info : infoLists) {
                strength += (-133 + 2 * info.getRssi());// 获取邻区基站信号强度
                NeighborLac =""+info.getLac();// 取出当前邻区的LAC
                NeighborCellID = "" + info.getCid();// 取出当前邻区的CID
                NeighborStrength = "" + strength;
            }
            listview_text = new String[]{MyLac, MyCellID, NeighborLac, NeighborCellID, NeighborStrength};

            initView();
            initvaliData();
            bindData();
        }
        return phonepage_view;
    }

    private void initView() {
        phonepage_listview = (ListView) phonepage_view.findViewById(R.id.list_view_phone);


    }

    private void initvaliData() {

        listview_img = new Integer[]{R.drawable.biz_navigation_tab_local_news,
                R.drawable.biz_navigation_tab_micro, R.drawable.biz_navigation_tab_news,
                R.drawable.biz_navigation_tab_pics, R.drawable.biz_navigation_tab_ties
        };
        listview_title = phonepage_context.getResources().getStringArray(R.array.listview_title);
        listItems = getListItems();
        phonepage_adapter = new ListViewAdapter(phonepage_context, listItems);

    }

    private void bindData() {
        phonepage_listview.setAdapter(phonepage_adapter);
    }

    private List<Map<String, Object>> getListItems() {
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < listview_img.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("imgItem_list", listview_img[i]);
            map.put("titleItem_list", listview_title[i]);
            map.put("textItem_list", listview_text[i]);
            listItems.add(map);
        }
        return listItems;
    }
}
