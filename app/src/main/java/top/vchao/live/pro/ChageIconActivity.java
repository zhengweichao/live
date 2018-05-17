package top.vchao.live.pro;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import top.vchao.live.R;
import top.vchao.live.mainUi.base.BaseActivity;
import top.vchao.live.utils.ToastUtils;

public class ChageIconActivity extends BaseActivity {

    @BindView(R.id.button4)
    Button button4;
    @BindView(R.id.button5)
    Button button5;

    @Override
    public int getLayoutId() {
        return R.layout.activity_chage_icon;
    }

    @OnClick({R.id.button4, R.id.button5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button4:
                switchIcon(1);
                ToastUtils.showShort("更换为java图标 ");
                break;
            case R.id.button5:
                switchIcon(2);
                ToastUtils.showShort("复原");
                break;
        }
    }


    /**
     * @param useCode =1、为活动图标 =2 为用普通图标 =3、不启用判断
     */
    private void switchIcon(int useCode) {

        try {
            //要跟manifest的 activity-alias 的 name 保持一致
            String icon_tag = "top.vchao.live.icon_tag";
            String icon_tag_other = "top.vchao.live.icon_tag_1212";

            PackageManager pm = getPackageManager();
            ComponentName normalComponentName = new ComponentName(
                    getBaseContext(),
                    icon_tag);
            //正常图标新状态
            int normalNewState = useCode == 2 ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                    : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
            if (pm.getComponentEnabledSetting(normalComponentName) != normalNewState) {//新状态跟当前状态不一样才执行
                pm.setComponentEnabledSetting(
                        normalComponentName,
                        normalNewState,
                        PackageManager.DONT_KILL_APP);
            }

            ComponentName actComponentName = new ComponentName(
                    getBaseContext(),
                    icon_tag_other);
            //正常图标新状态
            int actNewState = useCode == 1 ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                    : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
            if (pm.getComponentEnabledSetting(actComponentName) != actNewState) {//新状态跟当前状态不一样才执行

                pm.setComponentEnabledSetting(
                        actComponentName,
                        actNewState,
                        PackageManager.DONT_KILL_APP);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
