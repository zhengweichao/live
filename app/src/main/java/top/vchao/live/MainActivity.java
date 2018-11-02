package top.vchao.live;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import top.vchao.live.mainUi.base.BaseActivity;
import top.vchao.live.pro.BadgeActivity;
import top.vchao.live.pro.ChageIconActivity;
import top.vchao.live.pro.JumpIntentActivity;
import top.vchao.live.pro.LacCiActivity;
import top.vchao.live.pro.NightActivity;
import top.vchao.live.pro.ParcelableActivity;
import top.vchao.live.pro.PermissionCheckActivity;
import top.vchao.live.pro.PortActivity;
import top.vchao.live.pro.SaveLogFileActivity;
import top.vchao.live.pro.ScreenshotsActivity;
import top.vchao.live.pro.SiYiFuActivity;
import top.vchao.live.pro.SurfaceViewActivity;
import top.vchao.live.pro.TimeSelector.TimeSelectorActivity;
import top.vchao.live.pro.UIbetterActivity;
import top.vchao.live.pro.ViewTestActivity;
import top.vchao.live.pro.bean.MessageEvent;
import top.vchao.live.pro.bean.User;
import top.vchao.live.pro.excel.ExcelActivity;
import top.vchao.live.pro.hanzi.QuweimaActivity;
import top.vchao.live.pro.litepal.LitePalActivity;
import top.vchao.live.pro.socket.UDPServerActivity;
import top.vchao.live.pro.socket.UdpClientActivity;
import top.vchao.live.pro.udp.UDPActivity;
import top.vchao.live.pro.ui.RetrofitActivity;
import top.vchao.live.pro.update.UpdateAppManager;
import top.vchao.live.utils.LogUtils;

public class MainActivity extends BaseActivity {

    @BindView(R.id.live_bt_test8)
    Button liveBtTest8;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {
        // 请求权限
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
    }

    @OnClick({R.id.live_bt_test1, R.id.live_bt_test2, R.id.live_bt_test3, R.id.live_bt_test4, R.id.live_bt_test5,
            R.id.live_bt_test6, R.id.live_bt_test7, R.id.live_bt_test8, R.id.live_bt_test9, R.id.live_bt_test10,
            R.id.live_bt_test20, R.id.live_bt_test21, R.id.live_bt_test22, R.id.live_bt_test23, R.id.live_bt_test24,
            R.id.live_bt_test25, R.id.live_bt_test26, R.id.live_bt_test27})
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
                startActivity(new Intent(MainActivity.this, LitePalActivity.class));
                break;
            case R.id.live_bt_test22:
                startActivity(new Intent(MainActivity.this, UDPActivity.class));
                break;
            case R.id.live_bt_test23:
                startActivity(new Intent(MainActivity.this, UDPServerActivity.class));
                break;
            case R.id.live_bt_test24:
                startActivity(new Intent(MainActivity.this, UdpClientActivity.class));
                break;
            case R.id.live_bt_test25:
                startActivity(new Intent(MainActivity.this, QuweimaActivity.class));
                break;
            case R.id.live_bt_test26:
                startActivity(new Intent(MainActivity.this, ViewTestActivity.class));
//                startActivity(new Intent(MainActivity.this, UdpTestActivity.class));
                break;
            case R.id.live_bt_test27:
                startActivity(new Intent(MainActivity.this, LacCiActivity.class));
                break;
        }
    }

    @OnClick({R.id.live_bt_test28, R.id.live_bt_test29, R.id.live_bt_test30, R.id.live_bt_test31, R.id.live_bt_test32, R.id.live_bt_test33})
    public void onViewClicked0(View view) {
        switch (view.getId()) {
            case R.id.live_bt_test28:
                startActivity(new Intent(MainActivity.this, ExcelActivity.class));
                break;
            case R.id.live_bt_test29:
                startActivity(new Intent(MainActivity.this, SaveLogFileActivity.class));
                break;
            case R.id.live_bt_test30:
                startActivity(new Intent(MainActivity.this, PortActivity.class));
                break;
            case R.id.live_bt_test31:
                startActivity(new Intent(MainActivity.this, RetrofitActivity.class));
                break;
            case R.id.live_bt_test32:
                UpdateAppManager manager = new UpdateAppManager(MainActivity.this, "main");
                manager.checkUpdate(MainActivity.this);
                break;
            case R.id.live_bt_test33:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX(MessageEvent messageEvent) {
        LogUtils.e("EventBus === " + messageEvent.getMessage());
        liveBtTest8.setText(messageEvent.getMessage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
