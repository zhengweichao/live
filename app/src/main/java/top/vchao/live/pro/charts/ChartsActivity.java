package top.vchao.live.pro.charts;

import butterknife.BindView;
import lecho.lib.hellocharts.view.LineChartView;
import top.vchao.live.R;
import top.vchao.live.mainUi.base.BaseActivity;

public class ChartsActivity extends BaseActivity {


    @BindView(R.id.lcv)
    LineChartView lcv;

    @Override
    public int getLayoutId() {
        return R.layout.activity_charts;
    }

    @Override
    public void initView() {


    }

    @Override
    public void initData() {


    }
}
