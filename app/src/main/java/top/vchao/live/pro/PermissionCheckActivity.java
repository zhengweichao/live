package top.vchao.live.pro;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.Map;

import butterknife.BindView;
import top.vchao.live.R;
import top.vchao.live.constants.SPkey;
import top.vchao.live.mainUi.base.BaseActivity;
import top.vchao.live.utils.LogUtils;
import top.vchao.live.utils.NotificationsUtils;
import top.vchao.live.utils.SystemUtil;

public class PermissionCheckActivity extends BaseActivity {

    @BindView(R.id.tv_permission_location)
    TextView tvPermissionLocation;
    @BindView(R.id.tv_permission_memo)
    TextView tvPermissionMemo;
    @BindView(R.id.tv_permission_cam)
    TextView tvPermissionCam;
    @BindView(R.id.tv_permission_noti)
    TextView tvPermissionNoti;
    @BindView(R.id.tv_permission_call)
    TextView tvPermissionCall;
    @BindView(R.id.tv_permission_chage_setting)
    TextView tvPermissionChageSetting;
    @BindView(R.id.tv_tel_info)
    TextView tvTelInfo;
    @BindView(R.id.tv_user_info)
    TextView tvUserInfo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_permission_check;
    }


    @Override
    public void initView() {
        initTitleBar("APP运行检测");
    }

    private void initPermission() {
        if (ContextCompat.checkSelfPermission(PermissionCheckActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            tvPermissionLocation.setText("定位权限        关闭");
        } else {
            tvPermissionLocation.setText("定位权限        已允许");
        }
        if (ContextCompat.checkSelfPermission(PermissionCheckActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            tvPermissionCall.setText("拨号权限        关闭");
        } else {
            tvPermissionCall.setText("拨号权限        已允许");
        }
        if (ContextCompat.checkSelfPermission(PermissionCheckActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            tvPermissionMemo.setText("文件存储权限        关闭");
        } else {
            tvPermissionMemo.setText("文件存储权限        已允许");
        }
        if (ContextCompat.checkSelfPermission(PermissionCheckActivity.this, Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
            tvPermissionChageSetting.setText("更改设置权限     关闭");
        } else {
            tvPermissionChageSetting.setText("更改设置权限     已允许");
        }
        if (ContextCompat.checkSelfPermission(PermissionCheckActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            tvPermissionCam.setText("相机权限     关闭");
        } else {
            tvPermissionCam.setText("相机权限     已允许");
        }

        if (!NotificationsUtils.isNotificationEnabled(PermissionCheckActivity.this)) {
            tvPermissionNoti.setText("通知权限    关闭");
        } else {
            tvPermissionNoti.setText("通知权限    已允许");
        }
    }

    @Override
    public void initData() {
        initPermission();
        initAPPinfo();
        initUserInfo();
    }

    String user_info;

    private void initUserInfo() {
        logAll();
        tvUserInfo.setText(user_info);
    }

    private void initAPPinfo() {
//        APP版本
        String versionName = SystemUtil.getVersionName(PermissionCheckActivity.this);
//        手机型号
        String systemModel = SystemUtil.getSystemModel();
//        系统版本
        String systemVersion = SystemUtil.getSystemVersion();
//        手机厂商
        String deviceBrand = SystemUtil.getDeviceBrand();

        tvTelInfo.setText("您的手机信息：\nAPP版本： " + versionName + "\n手机信息：  " + deviceBrand + "  " + systemModel + "  \n安卓版本：  " + systemVersion);
    }

    public void logAll() {
        SharedPreferences spf = getSharedPreferences(SPkey.SpName, Activity.MODE_PRIVATE);
        Map<String, ?> key_Value = (Map<String, ?>) spf.getAll(); //获取所有保存在对应标识下的数据，并以Map形式返回
        /* 只需遍历即可得到存储的key和value值*/
        for (Map.Entry<String, ?> entry : key_Value.entrySet()) {
            LogUtils.e("sp中 获取的key：" + entry.getKey() + "      ======      获取的value:" + spf.getString(entry.getKey(), SPkey.DEFAUL));
            if (!spf.getString(entry.getKey(), SPkey.DEFAUL).equals(SPkey.DEFAUL)) {
                if (TextUtils.isEmpty(user_info)) {
                    user_info = entry.getKey() + "     " + spf.getString(entry.getKey(), SPkey.DEFAUL + "\n\n");
                } else {
                    user_info = user_info + "\n" + entry.getKey() + "     " + spf.getString(entry.getKey(), SPkey.DEFAUL + "\n\n");
                }
            }
        }
    }

}