package top.vchao.live;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import top.vchao.live.mainUi.base.BaseActivity;
import top.vchao.live.pro.BadgeActivity;
import top.vchao.live.pro.ChageIconActivity;
import top.vchao.live.pro.JumpIntentActivity;
import top.vchao.live.pro.NightActivity;
import top.vchao.live.pro.ParcelableActivity;
import top.vchao.live.pro.PermissionCheckActivity;
import top.vchao.live.pro.ScreenshotsActivity;
import top.vchao.live.pro.SiYiFuActivity;
import top.vchao.live.pro.SurfaceViewActivity;
import top.vchao.live.pro.TimeSelector.TimeSelectorActivity;
import top.vchao.live.pro.UIbetterActivity;
import top.vchao.live.pro.bean.User;

public class MainActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick({R.id.live_bt_test1, R.id.live_bt_test2, R.id.live_bt_test3, R.id.live_bt_test4, R.id.live_bt_test5,
            R.id.live_bt_test6, R.id.live_bt_test7, R.id.live_bt_test8, R.id.live_bt_test9, R.id.live_bt_test10,
            R.id.live_bt_test20, R.id.live_bt_test21, R.id.live_bt_test22})
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
                startActivity(new Intent(MainActivity.this, ChageIconActivity.class));
                break;
            case R.id.live_bt_test9:
                User.Car car = new User.Car("车牌号信息");
                ArrayList<String> strings = new ArrayList<>();
                strings.add("aaaaaaaa");
                ArrayList<User.Car> cars = new ArrayList<>();
                cars.add(car);

                Intent intent = new Intent(MainActivity.this, ParcelableActivity.class);
                intent.putExtra("test", new User("name", "other", strings, true, 55, cars));
                startActivity(intent);
                break;
            case R.id.live_bt_test10:
                startActivity(new Intent(MainActivity.this, SurfaceViewActivity.class));
                break;
            case R.id.live_bt_test20:
                startActivity(new Intent(MainActivity.this, SiYiFuActivity.class));
                break;
            case R.id.live_bt_test21:
                break;
            case R.id.live_bt_test22:
                break;

        }
    }



}
