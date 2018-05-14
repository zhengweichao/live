package top.vchao.live.pro;

import android.view.View;

import butterknife.OnClick;
import top.vchao.live.R;
import top.vchao.live.mainUi.base.BaseActivity;
import top.vchao.live.utils.BadgeUtils;

public class BadgeActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_badge;
    }

    @OnClick({R.id.send_badge, R.id.reset_badge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.send_badge:
                BadgeUtils.setBadgeCount(getApplicationContext(), 5);
                break;
            case R.id.reset_badge:
                BadgeUtils.resetBadgeCount(BadgeActivity.this);
                break;
        }
    }
}
