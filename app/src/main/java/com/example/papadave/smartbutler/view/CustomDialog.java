package com.example.papadave.smartbutler.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import com.example.papadave.smartbutler.R;

/**
 * Created by papadave on 2017/9/23.
 */
public class CustomDialog extends Dialog {

    public CustomDialog(Context context,int layout,int style){
        this(context, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,layout,style, Gravity.CENTER);

    }
    public CustomDialog (Context context,int width,int heigh,int layout,int style,int gravity,int anim){
        super(context,style);
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width=width;
        layoutParams.height=heigh;
        layoutParams.gravity=gravity;
        window.setAttributes(layoutParams);
        window.setWindowAnimations(anim);
    }


    public CustomDialog(Context context, int width, int height, int layout, int style, int gravity) {
        this(context,width,height,layout,style,gravity, R.style.pop_anim_style);
    }
}
