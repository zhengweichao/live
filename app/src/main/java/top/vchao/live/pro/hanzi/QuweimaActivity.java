package top.vchao.live.pro.hanzi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.vchao.live.R;
import top.vchao.live.utils.CommonUtil;
import top.vchao.live.utils.LogUtils;
import top.vchao.live.utils.NewEncodeUtils;
import top.vchao.live.utils.WordUtil;

public class QuweimaActivity extends AppCompatActivity {

    @BindView(R.id.editText3)
    EditText editText3;
    @BindView(R.id.button10)
    Button button10;
    @BindView(R.id.textView6)
    TextView textView6;
    @BindView(R.id.editText4)
    EditText editText4;
    @BindView(R.id.button11)
    Button button11;
    @BindView(R.id.textView10)
    TextView textView10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quweima);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.button10, R.id.button11})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button10:
                String text = CommonUtil.getText(editText3);
                LogUtils.e("要转换的内容：  " + text);
                String encode = WordUtil.encode(text);
                textView6.setText(encode);
                break;
            case R.id.button11:
                String text1 = CommonUtil.getText(editText4);
                LogUtils.e("要转换的内容：  " + text1);
                String words = WordUtil.codeToChinese(text1);
                LogUtils.e("转换结果：  " + words);
                textView10.setText(words);
                break;
        }
    }
}
