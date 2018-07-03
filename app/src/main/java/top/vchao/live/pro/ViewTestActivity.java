package top.vchao.live.pro;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.vchao.live.R;

public class ViewTestActivity extends Activity {

    @BindView(R.id.progressBarHorizontal)
    ProgressBar progressBarHorizontal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_test);
        ButterKnife.bind(this);

        progressBarHorizontal.setProgress(80);
        progressBarHorizontal.setSecondaryProgress(88);
//        progressBarHorizontal.incrementProgressBy(10);
//        progressBarHorizontal.incrementSecondaryProgressBy(10);
    }


}
