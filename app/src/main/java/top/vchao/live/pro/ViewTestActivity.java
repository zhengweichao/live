package top.vchao.live.pro;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.vchao.live.R;
import top.vchao.live.view.CircleBarView;
import top.vchao.live.view.MyCircleBar;
import top.vchao.live.view.TemperatureBar;

public class ViewTestActivity extends Activity {

    @BindView(R.id.progressBarHorizontal)
    ProgressBar progressBarHorizontal;
    @BindView(R.id.circle_bar_view)
    CircleBarView circleBarView;
    @BindView(R.id.text_progress)
    TextView textProgress;
    @BindView(R.id.temperature_bar)
    TemperatureBar temperatureBar;
    @BindView(R.id.my_circle_bar)
    MyCircleBar myCircleBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_test);
        ButterKnife.bind(this);
//        circleBarView.setProgressNum(3000);
//        circleBarView.setProgressNum(100, 3000);
        temperatureBar.setRsb_temp();

        circleBarView.setTextView(textProgress);
        circleBarView.setOnAnimationListener(new CircleBarView.OnAnimationListener() {
            @Override
            public String howToChangeText(float interpolatedTime, float progressNum, float maxNum) {
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                String s = decimalFormat.format(interpolatedTime * progressNum / maxNum * 100) + "%";
                return s;
            }
        });
        circleBarView.setProgressNum(80, 3000);

        progressBarHorizontal.setProgress(80);
        progressBarHorizontal.setSecondaryProgress(88);

        myCircleBar.showProgress(90, 5000);
//        progressBarHorizontal.incrementProgressBy(10);
//        progressBarHorizontal.incrementSecondaryProgressBy(10);
    }


}
