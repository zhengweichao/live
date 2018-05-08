package top.vchao.live.pro.TimeSelector;

import android.content.Context;

/**
 * @ 创建时间: 2018/4/19 on 15:08.
 * @ 描述：时间选择器
 * @ 作者: 郑卫超 QQ: 2318723605
 */
public class TimeSelectorUtil {

    /**
     * 底部滚轮点击事件回调
     */
    public interface OnWheelViewClick {
        void onClick(String time);
    }

    /**
     * 弹出底部滚轮选择
     *
     * @param context    上下文
     * @param start_time 每天开始服务时间
     * @param end_time   每天结束服务时间
     * @param click      回调
     */
    public static void alertBottomWheelOption(Context context, int start_time, int end_time, final OnWheelViewClick click) {
        NewTimeSelector timeSelector = new NewTimeSelector(context, start_time, end_time, click);
        timeSelector.setIsLoop(false);
        timeSelector.show();
    }


}
