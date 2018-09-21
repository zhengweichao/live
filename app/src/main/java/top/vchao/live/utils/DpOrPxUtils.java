package top.vchao.live.utils;

import android.content.Context;

/**
 * @ Create_time: 2018/9/21 on 18:48.
 * @ description：
 * @ author: vchao
 */
public class DpOrPxUtils {
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
