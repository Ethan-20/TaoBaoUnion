package com.example.taobaounion.ui.activity;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.taobaounion.R;
import com.example.taobaounion.ui.custom.TextFlowLayout;
import com.example.taobaounion.utils.LogUtils;

import java.util.LinkedList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    @BindView(R.id.test_navigation_bar)
    public RadioGroup mRadioGroup;
    @BindView(R.id.test_text_flow)
    public TextFlowLayout flowTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        List<String> textList = new LinkedList<>();
        textList.add("滋蚌");
        textList.add("衣服");
        textList.add("先别");
        textList.add("我承");
        textList.add("没睡");
        textList.add("滋蚌狗真不得好死吧");
        textList.add("衣服包包给你换新的");
        textList.add("先别");
        textList.add("我承");
        textList.add("没睡");
        textList.add("滋蚌狗真不得好死吧");
        textList.add("衣服包包给你换新的");
        textList.add("先别");
        textList.add("我承");
        textList.add("没睡");
        flowTextView.setTextList(textList);
        flowTextView.setItemClickListener(new TextFlowLayout.OnFlowTextItemClickListener() {
            @Override
            public void onFlowItemClick(String text) {
                LogUtils.d(this,"clickTest---->"+text );
            }
        });
        initListener();
    }

    private void initListener() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LogUtils.d(TestActivity.class,""+checkedId);
                switch (checkedId)
                {
                    case R.id.home:
                        LogUtils.d(MainActivity.class,"切换到首页");
//                        switchFragment(mHomeFragment);
                        break;
                    case R.id.selected:
                        LogUtils.i(MainActivity.class,"切换到精选");
//                        switchFragment(mSelecetedFragment);
                        break;
                    case R.id.red_packet:
                        LogUtils.w(MainActivity.class,"切换到特惠");
//                        switchFragment(mRedPacketFragment);
                        break;
                    case R.id.search:
                        LogUtils.e(MainActivity.class,"切换到搜索");
//                        switchFragment(mSearchFragment);
                        break;
                }
            }
        });
    }
}