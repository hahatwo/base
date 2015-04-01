package com.example.administrator.base.framegent.pageFramgent;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.base.R;

/**
 * Created by Administrator on 2014/12/25.
 */
public class MsmPage extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.msm_framelayout, null);
    }
}
