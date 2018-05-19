package top.vchao.live.pro;

import android.widget.TextView;

import butterknife.BindView;
import top.vchao.live.R;
import top.vchao.live.mainUi.base.BaseActivity;
import top.vchao.live.pro.bean.User;

public class ParcelableActivity extends BaseActivity {

    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.textView7)
    TextView textView7;
    @BindView(R.id.textView8)
    TextView textView8;
    @BindView(R.id.textView9)
    TextView textView9;
    private User user;

    @Override
    public int getLayoutId() {
        return R.layout.activity_parceable;
    }

    @Override
    public void getPreIntent() {
        user = getIntent().getParcelableExtra("test");
    }

    @Override
    public void initData() {

        try {
            textView2.setText(user.getUsername() + "");
            textView7.setText(user.getStrings().get(0) + "");
            textView8.setText(user.getData().get(0).getBrand() + "");
            textView9.setText(user.getAge() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
