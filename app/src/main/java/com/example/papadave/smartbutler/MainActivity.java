package com.example.papadave.smartbutler;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.papadave.smartbutler.fragment.ButlerFragment;
import com.example.papadave.smartbutler.fragment.GirlFragment;
import com.example.papadave.smartbutler.fragment.UserFragment;
import com.example.papadave.smartbutler.fragment.WechatFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
private TabLayout mTabLayout;
private ViewPager mViewPager;
private List<String> mTitle;
private List<android.support.v4.app.Fragment> mFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
    }


    private void initData() {
        mTitle = new ArrayList<>();
        mTitle.add("服务管家");
        mTitle.add("微信精选");
        mTitle.add("图片壁纸");
        mTitle.add("个人中心");

        mFragment = new ArrayList<>();
        mFragment.add(new ButlerFragment());
        mFragment.add(new WechatFragment());
        mFragment.add(new GirlFragment());
        mFragment.add(new UserFragment());
    }
    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        mViewPager.setOffscreenPageLimit(mFragment.size());

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });

        mTabLayout.setupWithViewPager(mViewPager);
    }

}
