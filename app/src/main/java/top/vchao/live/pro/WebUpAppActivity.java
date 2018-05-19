package top.vchao.live.pro;

import android.widget.TextView;

import butterknife.BindView;
import top.vchao.live.R;
import top.vchao.live.mainUi.base.BaseActivity;
import top.vchao.live.utils.LogUtils;

public class WebUpAppActivity extends BaseActivity {

    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.textView5)
    TextView textView5;
    private String test1;
    private String test2;
    private String test3;

    @Override
    public int getLayoutId() {
        return R.layout.activity_web_up_app;
    }

    @Override
    public void getPreIntent() {
//        获取网页传递的数据
        test1 = getIntent().getData().getQueryParameter("test1");
        test2 = getIntent().getData().getQueryParameter("test2");
        test3 = getIntent().getData().getQueryParameter("test3");
        LogUtils.e("   " + test1 + test2 + test3 + "     ");
    }

    @Override
    public void initView() {
        textView3.setText(test1);
        textView4.setText(test2);
        textView5.setText(test3);
    }
/*	<a href="live://test?test1=1111&test2=ceshi2&test3=测试文本">
        <button>打开测试</button>
	</a><br><br> */

}
