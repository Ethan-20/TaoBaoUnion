package com.example.taobaounion.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.taobaounion.R;
import com.example.taobaounion.base.BaseFragment;
import com.example.taobaounion.ui.fragment.HomeFragment;
import com.example.taobaounion.ui.fragment.RedPacketFragment;
import com.example.taobaounion.ui.fragment.SearchFragment;
import com.example.taobaounion.ui.fragment.SelectedFragment;
import com.example.taobaounion.utils.LogUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.main_navigation_bar)
    public BottomNavigationView mNavigationView;
    private HomeFragment mHomeFragment;
    private SelectedFragment mSelectedFragment;
    private RedPacketFragment mRedPacketFragment;
    private SearchFragment mSearchFragment;
    private FragmentManager mFm;
    private Unbinder mBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBind = ButterKnife.bind(this);
//        initView();
        initFragments();
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBind != null) {
            mBind.unbind();
        }
    }

    private void initFragments() {
        mHomeFragment = new HomeFragment();
        mSelectedFragment = new SelectedFragment();
        mRedPacketFragment = new RedPacketFragment();
        mSearchFragment = new SearchFragment();
        //Return the FragmentManager for interacting with fragments associated with this activity.
        mFm = getSupportFragmentManager();
        switchFragment(mHomeFragment);
    }

    private void initListener() {
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
//                Log.d(TAG,"title--->"+item.getTitle()+" itemId--->"+item.getItemId());
                switch (item.getItemId())
                {
                    case R.id.home:
                        LogUtils.d(this,"切换到首页");
                        switchFragment(mHomeFragment);
                        break;
                    case R.id.selected:
                        LogUtils.i(this,"切换到精选");
                        switchFragment(mSelectedFragment);
                        break;
                    case R.id.red_packet:
                        LogUtils.w(this,"切换到特惠");
                        switchFragment(mRedPacketFragment);
                        break;
                    case R.id.search:
                        LogUtils.e(this,"切换到搜索");
                        switchFragment(mSearchFragment);
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 上一次显示的fragment
     */
    private BaseFragment lastFragment = null;

    private void switchFragment(BaseFragment targetFragment) {

        if (targetFragment==lastFragment)
            return;
        //重构成add和hide的方式来控制fragment的切换
        FragmentTransaction fragmentTransaction = mFm.beginTransaction();

        if (!targetFragment.isAdded()) {
            fragmentTransaction.add(R.id.main_page_container, targetFragment);
        }
        else {
            fragmentTransaction.show(targetFragment);
        }
        if (lastFragment != null) {
            LogUtils.d(this,"lastFragment"+lastFragment);
            fragmentTransaction.hide(lastFragment);
        }
        lastFragment = targetFragment;
       // fragmentTransaction.replace(R.id.main_page_container, targetFragment);
        fragmentTransaction.commit();
    }

    /**
     * 2023/4/18 因为忘记删这一段代码,折腾了一个半钟,铭记
     */
    private void initView() {
        HomeFragment homeFragment = new HomeFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();//拿到一个事务
        transaction.add(R.id.main_page_container,homeFragment);
        transaction.commit();
    }
}
