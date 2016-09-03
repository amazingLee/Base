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

public abstract class BaseActivity extends AppCompatActivity {
    /**
     * Log output symbol
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
     * Whether the status bar
     **/
    private boolean isSetStatusBar = true;
    /**
     * Whether to allow full screen
     **/
    private boolean mAllowFullScreen = true;
    /**
     * Whether to ban the rotating screen
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


    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }


    public void startActivity(Class<?> clz) {
        startActivity(new Intent(BaseActivity.this, clz));
    }


    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }



    public abstract void initParms(Bundle parms);

    protected abstract void initActionBar();

    protected abstract void initView();

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
            String action = intent.getAction();
            if (ACTION_NETWORK_CHANGE.equals(action)) {
                ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo activeInfo = manager.getActiveNetworkInfo();
                onNetStateChanged(context, mobileInfo, wifiInfo, activeInfo);
            } else if (ACTION_PUSH_DATA.equals(action)) {
               /* Bundle b = intent.getExtras();
                MData<Employee> mdata = (MData<Employee>) b.get("data");
                if (dataCallback != null) {
                    dataCallback.onNewData(mdata);
                }*/
            } else if (ACTION_NEW_VERSION.equals(action)) {
                onNewVersion();
            }
        }
    };


    protected abstract void onNetStateChanged(Context context, NetworkInfo mobileInfo, NetworkInfo wifiInfo, NetworkInfo activeInfo);


    protected abstract void onNewVersion();

    /**
     *
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
     *
     * @return version
     */
    protected int getVersionCode() {
        return Build.VERSION.SDK_INT;
    }


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


    protected abstract void onBack();

    public void exitApp() {
        mAppManager.AppExit(this);
    }
}
