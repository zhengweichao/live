package top.vchao.live.pro;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatDelegate;

import butterknife.OnClick;
import top.vchao.live.R;
import top.vchao.live.mainUi.base.BaseActivity;

public class NightActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_night;
    }

    private void setNightMode() {
        //  获取当前模式
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        //  切换模式
        getDelegate().setDefaultNightMode(currentNightMode == Configuration.UI_MODE_NIGHT_NO ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

//        重启Activity
//        recreate();
        startActivity(new Intent(this, NightActivity.class));
        overridePendingTransition(R.anim.anim_in, R.anim.anim_in);
        finish();
    }


    @OnClick(R.id.button3)
    public void onViewClicked() {
        setNightMode();
    }
}
