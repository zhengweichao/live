package top.vchao.live.pro;

import android.view.View;
import android.view.ViewStub;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import top.vchao.live.R;
import top.vchao.live.mainUi.base.BaseActivity;
import top.vchao.live.utils.LogUtils;

public class UIbetterActivity extends BaseActivity {

    @BindView(R.id.bt_merge1)
    Button btMerge1;
    @BindView(R.id.bt_merge2)
    Button btMerge2;
    @BindView(R.id.viewstub)
    ViewStub viewstub;

    @Override
    public int getLayoutId() {
        return R.layout.activity_uibetter;
    }

    @OnClick({R.id.bt_merge1, R.id.bt_merge2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_merge1:
                if (viewstub != null) {
                    viewstub.inflate();
                }
                if (viewstub == null) {
                    LogUtils.e("填充之后 viewstub 变为空 ~~~~");
                }
                if (findViewById(R.id.timeSelect) != null) {
                    LogUtils.e(" timeSelect  不为空 ~~");
                }
                break;
            case R.id.bt_merge2:
                break;
        }
    }
}
