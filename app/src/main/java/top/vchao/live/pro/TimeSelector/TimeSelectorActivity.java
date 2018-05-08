package top.vchao.live.pro.TimeSelector;

import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import top.vchao.live.R;
import top.vchao.live.mainUi.base.BaseActivity;
import top.vchao.live.utils.LogUtils;

public class TimeSelectorActivity extends BaseActivity {

    @BindView(R.id.timeSelect)
    Button timeSelect;

    @Override
    public int getLayoutId() {
        return R.layout.activity_time_selector;
    }


    @OnClick(R.id.timeSelect)
    public void onViewClicked() {
        TimeSelectorUtil.alertBottomWheelOption(TimeSelectorActivity.this, 7, 18, new TimeSelectorUtil.OnWheelViewClick() {
            @Override
            public void onClick(String time) {
                LogUtils.e("" + time);
            }
        });
    }
}
