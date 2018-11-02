package top.vchao.live.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import top.vchao.live.R;


/**
 * Created by fpt on 2018/2/26.
 */

public class TemperatureBar extends RelativeLayout {
    private DuplexSeekBar rsb_temp;
    private ImageView iv_enable;

    public TemperatureBar(Context context) {
        super(context);
        initView(context);
    }

    public TemperatureBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TemperatureBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.view_temperature_bar, this);
        rsb_temp = view.findViewById(R.id.rsb_temp);
        iv_enable = view.findViewById(R.id.iv_enable);
        rsb_temp.setOnSeekBarChangeListener(new DuplexSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(DuplexSeekBar seekBar, double progressLow, double progressHigh) {
//                prefUtils.saveBorderParams(progressLow + "", progressHigh + "");
            }
        });

    }

    public void setRsb_temp() {

        //设置初始值
//        String min = prefUtils.getMinBorder();
//        String max = prefUtils.getMaxBorder();

        rsb_temp.setProgressHigh(100);
        rsb_temp.setProgressLow(30);
    }

    public void setEnable(boolean enable) {
        if (enable) {
            iv_enable.setVisibility(INVISIBLE);
        } else {
            iv_enable.setVisibility(VISIBLE);
        }
        rsb_temp.setThisEnable(enable);
    }

}
