package top.vchao.live.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;


public class ScreenUtils {

    public static int height;
    public static int width;
    private Context context;

    private static ScreenUtils instance;

    private ScreenUtils(Context context) {
        this.context = context;
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
    }

    public static ScreenUtils getInstance(Context context) {
        if (instance == null) {
            instance = new ScreenUtils(context);
        }
        return instance;
    }

    /**
     * 得到手机屏幕的宽度, pix单位
     */
    public int getScreenWidth() {
        return width;
    }

}