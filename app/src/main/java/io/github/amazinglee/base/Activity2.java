package io.github.amazinglee.base;

import android.content.Context;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.amazinglee.baseutil.BaseActivity;

/**
 * 项目名称：Base
 * 类描述：
 * 创建人：renhao
 * 创建时间：2016/9/1 0:54
 * 修改备注：
 */
public class Activity2 extends BaseActivity {
    private static final String TAG = "Activity2";
    @Bind(R.id.button1)
    Button mButton1;
    @Bind(R.id.button2)
    Button mButton2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    protected void initActionBar() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initDate() {

    }

    @Override
    protected void onNetStateChanged(Context context, NetworkInfo mobileInfo, NetworkInfo wifiInfo, NetworkInfo activeInfo) {

    }

    @Override
    protected void onNewVersion() {

    }

    @Override
    protected void onBack() {
        showToast("finish");
    }

    @OnClick({R.id.button1, R.id.button2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:
                break;
            case R.id.button2:
                startActivity(MainActivity.class);
                break;
        }
    }
}
