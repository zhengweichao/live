package top.vchao.live;

import android.content.Intent;
import android.view.View;

import butterknife.OnClick;
import top.vchao.live.mainUi.base.BaseActivity;
import top.vchao.live.pro.BadgeActivity;
import top.vchao.live.pro.JumpIntentActivity;
import top.vchao.live.pro.NightActivity;
import top.vchao.live.pro.PermissionCheckActivity;
import top.vchao.live.pro.ScreenshotsActivity;
import top.vchao.live.pro.TimeSelector.TimeSelectorActivity;
import top.vchao.live.pro.UIbetterActivity;

public class MainActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick({R.id.live_bt_test1, R.id.live_bt_test2, R.id.live_bt_test3, R.id.live_bt_test4, R.id.live_bt_test5,
            R.id.live_bt_test6, R.id.live_bt_test7, R.id.live_bt_test8, R.id.live_bt_test9, R.id.live_bt_test10})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.live_bt_test1:
                startActivity(new Intent(MainActivity.this, ScreenshotsActivity.class));
                break;
            case R.id.live_bt_test2:
                startActivity(new Intent(MainActivity.this, PermissionCheckActivity.class));
                break;
            case R.id.live_bt_test3:
                startActivity(new Intent(MainActivity.this, TimeSelectorActivity.class));
                break;
            case R.id.live_bt_test4:
                startActivity(new Intent(MainActivity.this, JumpIntentActivity.class));
                break;
            case R.id.live_bt_test5:
                startActivity(new Intent(MainActivity.this, BadgeActivity.class));
                break;
            case R.id.live_bt_test6:
                startActivity(new Intent(MainActivity.this, NightActivity.class));
                break;
            case R.id.live_bt_test7:
                startActivity(new Intent(MainActivity.this, UIbetterActivity.class));
                break;
            case R.id.live_bt_test8:
                break;
            case R.id.live_bt_test9:
                break;
            case R.id.live_bt_test10:
                break;
        }
    }

}
