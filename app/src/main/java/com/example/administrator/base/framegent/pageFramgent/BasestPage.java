package com.example.administrator.base.framegent.pageFramgent;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
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
 * Created by Administrator on 2015/1/12.
 */
public class BasestPage extends Fragment {
    String phonetype, datastate, DeviceSoftwareVersion, deviceid, networkoperator, SimSerialNumber;
    String mcc, mnc, networktype = "null", networkopenratorname, roaming, IMSI;
    private View basepage_view;
    private Context basepage_context;
    private ListView basepage_listview;
    private ListViewAdapter basepage_adapter;
    private List<Map<String, Object>> listItems;
    private Integer[] listview_img;//lsitview图片
    private String[] listview_title;//listview标题
    private String[] listview_text;//listview信息
    private TelephonyManager telephony;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (basepage_view == null) {
            basepage_view = inflater.inflate(R.layout.basest_framelayout, container, false);
            basepage_context = basepage_view.getContext();

            initView();
            initvaliData();
            bindData();
        }
        return basepage_view;
    }


    private void initView() {
        telephony = (TelephonyManager) basepage_context.getSystemService(Context.TELEPHONY_SERVICE);
        networkoperator = telephony.getNetworkOperator();
        mcc = "移动国家码MCC:" + networkoperator.substring(0, 3);
        mnc = "移动网络码MNC:" + networkoperator.substring(3);
        deviceid = "IMEI:" + telephony.getDeviceId();
        IMSI = "IMSI : " + telephony.getSubscriberId();
        DeviceSoftwareVersion = "设备的软件版本号：" + telephony.getDeviceSoftwareVersion();
        networkopenratorname = "服务商名称：" + telephony.getSimOperatorName();
        SimSerialNumber = "手机SIM卡序列号：" + telephony.getSimSerialNumber();

        basepage_listview = (ListView) basepage_view.findViewById(R.id.list_view_basest);
    }

    private void initvaliData() {
        if (telephony.getPhoneType() == 1) {
            phonetype = "手机类型：" + "GSM";
        } else if (telephony.getPhoneType() == 2) {
            phonetype = "手机类型：" + "CMDA";
        } else {
            phonetype = "手机类型：未知";
        }
        if (telephony.getDataState() == 2) {
            if (telephony.getNetworkType() == telephony.NETWORK_TYPE_EDGE) {
                networktype = "(" + "EDGE" + "-移动2G)";
            } else if (telephony.getNetworkType() == telephony.NETWORK_TYPE_GPRS) {
                networktype = "(" + "GPRS" + "-联通2G)";
            } else if (telephony.getNetworkType() == telephony.NETWORK_TYPE_CDMA) {
                networktype = "(" + "CDMA" + "-电信2G)";
            }
            datastate = "数据连接 ： " + "已连接" + networktype;
        } else {
            datastate = "数据连接 ： " + "未连接";

        }

        if (telephony.isNetworkRoaming() == true) {
            roaming = "是否漫游：" + "是";
        } else {
            roaming = "是否漫游：" + "否";
        }


    }

    private void bindData() {
        listview_img = new Integer[]{R.drawable.biz_navigation_tab_local_news,
                R.drawable.biz_navigation_tab_micro, R.drawable.biz_navigation_tab_news,
                R.drawable.biz_navigation_tab_pics, R.drawable.biz_navigation_tab_ties,
                R.drawable.biz_navigation_tab_local_news, R.drawable.biz_navigation_tab_local_news,
                R.drawable.biz_navigation_tab_ties, R.drawable.biz_navigation_tab_pics,
                R.drawable.biz_navigation_tab_local_news
        };

        listview_title = basepage_context.getResources().getStringArray(R.array.listview_basetitle);

        listview_text = new String[]{mnc, mcc, IMSI, deviceid,
                DeviceSoftwareVersion, networkopenratorname, SimSerialNumber,
                phonetype, datastate, roaming};

        listItems = getListItems();
        basepage_adapter = new ListViewAdapter(basepage_context, listItems);
        basepage_listview.setAdapter(basepage_adapter);
    }

    private List<Map<String, Object>> getListItems() {
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < listview_img.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("imgItem_list", listview_img[i]);
            // map.put("titleItem_list", listview_title[i]);
            map.put("textItem_list", listview_text[i]);
            listItems.add(map);
        }
        return listItems;
    }

}
