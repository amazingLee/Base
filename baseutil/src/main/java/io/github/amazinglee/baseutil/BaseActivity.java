package io.github.amazinglee.baseutil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import io.github.amazinglee.baseutil.manager.AppManager;

/**
 * 项目名称：Base
 * 类描述：BaseActivity类，继承自AppCompatActivity，功能如下
 * 1.写了三个抽象类，用于初始化界面和获取数据
 * 2.
 * 创建人：renhao
 * 创建时间：2016/8/31 16:14
 * 修改备注：
 */
public abstract class BaseActivity extends AppCompatActivity {
    /**
     * 日志输出标志
     **/
    protected final String TAG = this.getClass().getSimpleName();
    public static final String ACTION_NETWORK_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    public static final String ACTION_PUSH_DATA = "fm.data.push.action";
    public static final String ACTION_NEW_VERSION = "apk.update.action";

    private static final int ACTIVITY_RESUME = 0;
    private static final int ACTIVITY_STOP = 1;
    private static final int ACTIVITY_PAUSE = 2;
    private static final int ACTIVITY_DESTROY = 3;

    public int activityState;

    public AppManager mAppManager;

    public AppManager getAppManager() {
        return mAppManager;
    }

    /**
     * 是否沉浸状态栏
     **/
    private boolean isSetStatusBar = true;
    /**
     * 是否允许全屏
     **/
    private boolean mAllowFullScreen = true;
    /**
     * 是否禁止旋转屏幕
     **/
    private boolean isAllowScreenRoate = false;

    public boolean isAllowScreenRoate() {
        return isAllowScreenRoate;
    }

    public void setAllowScreenRoate(boolean allowScreenRoate) {
        isAllowScreenRoate = allowScreenRoate;
    }

    public boolean isSetStatusBar() {
        return isSetStatusBar;
    }

    public void setSetStatusBar(boolean setStatusBar) {
        isSetStatusBar = setStatusBar;
    }

    public boolean isAllowFullScreen() {
        return mAllowFullScreen;
    }

    public void setAllowFullScreen(boolean allowFullScreen) {
        mAllowFullScreen = allowFullScreen;
    }

    public BaseActivity() {
        mAppManager = AppManager.getAppManager();
        mAppManager.addActivity(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Logger.v(TAG, "onCreat");
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        initParms(bundle);
        initActionBar();
        initView();
       /* if (mAllowFullScreen) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        if (isSetStatusBar) {
            steepStatusBar();
        }
        if (!isAllowScreenRoate) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }*/
        initDate();
    }

    /**
     * [沉浸状态栏]
     */
    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * [页面跳转]
     *
     * @param clz
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(BaseActivity.this, clz));
    }

    /**
     * [携带数据的页面跳转]
     *
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * [含有Bundle通过Class打开编辑界面]
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }


    /**
     * [初始化参数]
     *
     * @param parms
     */
    public abstract void initParms(Bundle parms);

    /**
     * 初始化标题栏
     */
    protected abstract void initActionBar();

    /**
     * 初始化视图
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initDate();

    @Override
    protected void onStart() {
        Logger.v(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Logger.v(TAG, "onResume");
        super.onResume();
        activityState = ACTIVITY_RESUME;
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_NETWORK_CHANGE);
        filter.addAction(ACTION_PUSH_DATA);
        filter.addAction(ACTION_NEW_VERSION);
        registerReceiver(receiver, filter);
        //还可能发送统计数据，比如第三方的SDK 做统计需求
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Logger.v(TAG, "onWindowFocusChanged");
    }

    @Override
    protected void onStop() {
        Logger.v(TAG, "onStop");
        super.onStop();
        activityState = ACTIVITY_STOP;
    }

    @Override
    protected void onPause() {
        Logger.v(TAG, "onPause");
        super.onPause();
        activityState = ACTIVITY_PAUSE;
        unregisterReceiver(receiver);
        //还可能发送统计数据，比如第三方的SDK 做统计需求
    }

    @Override
    protected void onDestroy() {
        Logger.v(TAG, "onDestroy");
        super.onDestroy();
        activityState = ACTIVITY_DESTROY;
        mAppManager.finishActivity();
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 处理各种情况
            String action = intent.getAction();
            if (ACTION_NETWORK_CHANGE.equals(action)) { // 网络发生变化
                // 处理网络问题
                ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo activeInfo = manager.getActiveNetworkInfo();
                onNetStateChanged(context, mobileInfo, wifiInfo, activeInfo);
            } else if (ACTION_PUSH_DATA.equals(action)) { // 可能有新数据
                //更倾向于用eventbus解决
               /* Bundle b = intent.getExtras();
                MData<Employee> mdata = (MData<Employee>) b.get("data");
                if (dataCallback != null) { // 数据通知
                    dataCallback.onNewData(mdata);
                }*/
            } else if (ACTION_NEW_VERSION.equals(action)) { // 可能发现新版本
                // VersionDialog 可能是版本提示是否需要下载的对话框
                onNewVersion();
            }
        }
    };

    /**
     * 网络状态发生改变
     *
     * @param context
     * @param mobileInfo 手机网络
     * @param wifiInfo   wifi
     * @param activeInfo 如果无网络连接activeInfo为null
     */
    protected abstract void onNetStateChanged(Context context, NetworkInfo mobileInfo, NetworkInfo wifiInfo, NetworkInfo activeInfo);

    /**
     * 有新的版本
     */
    protected abstract void onNewVersion();

    /**
     * 获取android的版本
     * BASE //October 2008: The original, first, version of Android.
     * BASE_1_1 //February 2009: First Android update, officially called 1.1.
     * CUPCAKE //May 2009: Android 1.5.
     * CUR_DEVELOPMENT //Magic version number for a current development build, which has not yet turned into an official release.
     * DONUT //September 2009: Android 1.6.
     * ECLAIR //November 2009: Android 2.0
     * ECLAIR_0_1 //December 2009: Android 2.0.1
     * ECLAIR_MR1 //January 2010: Android 2.1
     * FROYO June //2010: Android 2.2
     * GINGERBREAD //November 2010: Android 2.3
     * GINGERBREAD_MR1 //February 2011: Android 2.3.3.
     * HONEYCOMB //February 2011: Android 3.0.
     * HONEYCOMB_MR1 //May 2011: Android 3.1.
     * HONEYCOMB_MR2 //June 2011: Android 3.2.
     * ICE_CREAM_SANDWICH //October 2011: Android 4.0.
     * ICE_CREAM_SANDWICH_MR1 //December 2011: Android 4.0.3.
     * JELLY_BEAN //June 2012: Android 4.1.
     * JELLY_BEAN_MR1 //Android 4.2: Moar jelly beans!
     * JELLY_BEAN_MR2//4.3.x	API level 18
     * KITKAT//4.4 - 4.4.4	API level 19
     * LOLLIPOP//5.0	API level 21
     * LOLLIPOP_MR1//5.1	API level 22
     * M//6.0	API level 23
     *
     * @return
     */
    protected int getVersionCode() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 显示Toast通知
     *
     * @param msg
     */
    protected void showToast(String msg) {
        Toast.makeText(mAppManager.currentActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            onBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 回退事件
     */
    protected abstract void onBack();

    /**
     * 退出应用
     */
    public void exitApp() {
        mAppManager.AppExit(this);
    }
}
