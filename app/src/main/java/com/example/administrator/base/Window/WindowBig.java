package com.example.administrator.base.Window;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.base.CustomWidget.RoundedImageView;
import com.example.administrator.base.MainActivity;
import com.example.administrator.base.R;
import com.example.administrator.base.Service.WindowService;

/**
 * Created by Administrator on 2015/3/22.
 */
public class WindowBig extends LinearLayout {
    /**
     * 记录大悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录大悬浮窗的高度
     */
    public static int viewHeight;

    public WindowBig(final Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_window_big, this);
        View view = findViewById(R.id.layout_window_big);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        RoundedImageView roundedImage_enter = (RoundedImageView) findViewById(R.id.roundImage_enter);
        Button back = (Button) findViewById(R.id.back);
        ImageView close_window_big = (ImageView) findViewById(R.id.close_window_big);
        roundedImage_enter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击关闭悬浮窗的时候，移除所有悬浮窗，并停止Service
                MyWindowManager.removeBigWindow(context);
                // MyWindowManager.removeSmallWindow(context);
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
                //context.stopService(intent);
            }
        });
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击返回的时候，移除大悬浮窗，创建小悬浮窗
                MyWindowManager.removeBigWindow(context);
                MyWindowManager.createSmallWindow(context);
            }
        });
        close_window_big.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MyWindowManager.removeBigWindow(context);
                Intent intent = new Intent(getContext(), WindowService.class);
                context.stopService(intent);
            }

        });
    }
}