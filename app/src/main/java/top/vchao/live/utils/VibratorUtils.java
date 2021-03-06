package top.vchao.live.utils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

/**
 * @ Create_time: 2018/9/13 on 20:51.
 * @ description：震动工具类
 * @ author: vchao
 */
public class VibratorUtils {
    /**
     * final Activity activity ：调用该方法的Activity实例
     * long pattern ：震动的时长，单位是毫秒
     */
    public static void Vibrate(final Context context, long milliseconds) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    /**
     * <strong>@param </strong>activity 调用该方法的Activity实例
     * <strong>@param </strong>pattern long[] pattern ：自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
     * <strong>@param </strong>isRepeat 是否反复震动，如果是true，反复震动，如果是false，只震动一次
     */
    public static void Vibrate(final Activity activity, long[] pattern, boolean isRepeat) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }
}
