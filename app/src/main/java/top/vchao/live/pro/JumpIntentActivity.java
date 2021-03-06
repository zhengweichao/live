package top.vchao.live.pro;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import butterknife.OnClick;
import top.vchao.live.R;
import top.vchao.live.mainUi.base.BaseActivity;
import top.vchao.live.utils.ToastUtils;

public class JumpIntentActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_jump_intent;
    }

    @OnClick({R.id.jump_market, R.id.jump_contact, R.id.jump_call, R.id.jump_ready_call, R.id.jump_app_info, R.id.jump_browser,
            R.id.jump_install, R.id.jump_uninstall, R.id.ignore_battery})
    public void onViewClicked(View view) {
        Uri uri;
        Intent intent;
        switch (view.getId()) {
            case R.id.jump_market:
                uri = Uri.parse("market://details?id=" + getPackageName());
                intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.jump_contact:

                break;
            case R.id.jump_call:
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(JumpIntentActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(JumpIntentActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 0);
                    } else {
                        doCall();
                    }
                } else {
                    doCall();
                }
                break;
            case R.id.jump_ready_call:
                uri = Uri.parse("tel:18812341234");
                intent = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(intent);
                break;
            case R.id.jump_app_info:
                intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
                break;
            case R.id.jump_browser:
                uri = Uri.parse("http://www.baidu.com");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.jump_install:
                try {
                    Uri installUri = Uri.fromParts("package", "com.rjwl.reginet.yizhangb", null);
                    intent = new Intent(Intent.ACTION_PACKAGE_ADDED, installUri);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.jump_uninstall:
                try {
                    uri = Uri.fromParts("package", "com.rjwl.reginet.yizhangb", null);
                    intent = new Intent(Intent.ACTION_DELETE, uri);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ignore_battery:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PowerManager PM = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
                    String PN = this.getPackageName();
                    if (!PM.isIgnoringBatteryOptimizations(PN)) {
                        ToastUtils.showShort("请在“是否忽略电池优化”中选择“是”。");

                        Uri U = Uri.parse("package:" + PN);
                        Intent I = new Intent();
                        I.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                        I.setData(U);
                        I.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        this.startActivityForResult(I, 0);
                    }
                }
                break;
        }
    }

    @SuppressLint("MissingPermission")
    private void doCall() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + "18812341234");
        intent.setData(data);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 0:
                if (ContextCompat.checkSelfPermission(JumpIntentActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    doCall();
                } else {
                    ToastUtils.showShort("没有开启拨号权限");
                }
                break;
            default:
                break;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
//                忽略电池优化后续处理
                break;
        }
    }
}
