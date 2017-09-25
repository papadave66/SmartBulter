package com.example.papadave.smartbutler.application;

import android.app.Application;
import cn.bmob.v3.Bmob;
import com.example.papadave.smartbutler.utils.StaticClass;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by papadave on 2017/9/22.
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化bugly
        CrashReport.initCrashReport(getApplicationContext(), StaticClass.BUGLY_APP_ID, true);
        //初始化bmoob
        Bmob.initialize(this, StaticClass.BMOB_APP_ID);

    }
}
