package top.vchao.live.mainUi.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import top.vchao.live.R;
import top.vchao.live.utils.LogUtils;

/**
 * Activity  基类
 * 无特殊需求时，使用此BaseActivity
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        强制竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        LogUtils.i(getClass().getName() + "-----------onCreate");
        //得到布局文件
        setContentView(getLayoutId());
        ButterKnife.bind(this);

        getPreIntent();
        initView();
        initData();
        initListener();
    }


    /**
     * @return 布局文件id
     */
    public abstract int getLayoutId();

    /**
     * 获取上一个页面传递来的intent数据
     */
    public void getPreIntent() {
    }

    /**
     * 初始化View
     */
    public void initView() {
    }

    /**
     * 初始化界面数据
     */
    public void initData() {
    }

    /**
     * 绑定监听器与适配器
     */
    public void initListener() {
    }

    /**
     * 初始化标题栏
     *
     * @param title
     */
    public void initTitleBar(String title) {
        TextView tvTitle = (TextView) findViewById(R.id.title_bar_title);
        if (tvTitle == null) return;
        tvTitle.setText(title);
        ImageView ivBack = (ImageView) findViewById(R.id.title_bar_back_iv);
        if (ivBack == null) return;
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}