package io.github.amazinglee.base;

import android.content.Context;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.amazinglee.baseutil.BaseActivity;

public class MainActivity extends BaseActivity {

    @Bind(R.id.button1)
    Button mButton1;
    @Bind(R.id.button2)
    Button mButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        Logger.v(TAG, "mobile:" + mobileInfo.isConnected() + "\n"
                + "wifi:" + wifiInfo.isConnected() + "\n" +
                "active:" + activeInfo.getTypeName());
        Toast.makeText(this, "mobile:" + mobileInfo.isConnected() + "\n"
                + "wifi:" + wifiInfo.isConnected() + "\n"
                + "active:" + activeInfo.getTypeName(), Toast.LENGTH_SHORT).show();
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
                Logger.v(TAG, "app version  " + getVersionCode());
                break;
            case R.id.button2:
                startActivity(Activity2.class);
                break;
        }
    }

}
