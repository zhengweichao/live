package top.vchao.live;

import android.content.Intent;
import android.view.View;

import butterknife.OnClick;
import top.vchao.live.mainUi.base.BaseActivity;
import top.vchao.live.pro.PermissionCheckActivity;
import top.vchao.live.pro.ScreenshotsActivity;

public class MainActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick({R.id.live_bt_test1, R.id.live_bt_test2, R.id.live_bt_test3, R.id.live_bt_test4, R.id.live_bt_test5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.live_bt_test1:
                startActivity(new Intent(MainActivity.this, ScreenshotsActivity.class));
                break;
            case R.id.live_bt_test2:
                startActivity(new Intent(MainActivity.this, PermissionCheckActivity.class));
                break;
            case R.id.live_bt_test3:
                break;
            case R.id.live_bt_test4:
                break;
            case R.id.live_bt_test5:
                break;
        }
    }
}
