package top.vchao.live.pro;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.vchao.live.R;
import top.vchao.live.mainUi.base.BaseActivity;
import top.vchao.live.utils.LogUtils;
import top.vchao.live.utils.ToastUtil;

public class ScreenshotsActivity extends BaseActivity {

    @BindView(R.id.iv_screenshots)
    ImageView ivScreenshots;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.button2)
    Button button2;
    @BindView(R.id.switch1)
    Switch switch1;
    private ScreenShotListenManager manager;
    boolean CanScreen = true;

    @Override
    public int getLayoutId() {
        return R.layout.activity_screenshots;
    }

    @Override
    public void initView() {
        manager = ScreenShotListenManager.newInstance(ScreenshotsActivity.this);
        manager.setListener(
                new ScreenShotListenManager.OnScreenShotListener() {
                    public void onShot(String imagePath) {
                        // do something
                        if (CanScreen) {

                            LogUtils.e("获取到了截屏的图片" + imagePath);
                            ToastUtil.showShort(imagePath);
                            Glide.with(ScreenshotsActivity.this)
                                    .load(imagePath)
                                    .into(ivScreenshots);
                        } else {
                            ToastUtil.showShort("当前页面禁止截图！");
                            File file = new File(imagePath);
                            file.delete();

                        }
                    }
                }
        );
    }

    @OnClick({R.id.button, R.id.button2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button:
                if (Build.VERSION.SDK_INT > 22) {
                    LogUtils.e("请求权限");

                    List<String> permissionList = new ArrayList<>();
                    // 检查权限
                    if (ContextCompat.checkSelfPermission(ScreenshotsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // 解释权限
//                    ActivityCompat.shouldShowRequestPermissionRationale(ScreenshotsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    } else {
                        doStart();
                    }

                    if (permissionList != null && (permissionList.size() != 0)) {
                        ActivityCompat.requestPermissions(ScreenshotsActivity.this, permissionList.toArray(new String[permissionList.size()]), 0);
                    }

                }

                break;
            case R.id.button2:
                doStop();
                break;

        }
    }

    @Override
    public void initListener() {
        switch1.setChecked(CanScreen);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CanScreen = true;
                } else {
                    CanScreen = false;
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LogUtils.e("请求权限结果");
        if (requestCode == 0) {
            if (ContextCompat.checkSelfPermission(ScreenshotsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                doStart();
            } else {
                openAppDetails();
            }
        }
    }

    private void doStart() {
        button.setEnabled(false);
        button2.setEnabled(true);
        manager.startListen();
    }

    private void doStop() {
        button.setEnabled(true);
        button2.setEnabled(false);
        manager.stopListen();
    }

    /**
     * 打开 APP 的详情设置
     */
    private void openAppDetails() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("没有权限，请到 “应用信息 -> 权限” 中授予！");
        builder.setPositiveButton("去手动授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

}
