package top.vchao.live.utils;

import android.text.TextUtils;
import android.widget.EditText;

/**
 * @ Create_time: 2018/6/27 on 13:39.
 * @ description：公用基础工具类
 * @ author: vchao  blog: http://blog.csdn.net/zheng_weichao
 */
public class CommonUtil {

    /**
     * 检查 EditText 是否为空
     *
     * @return  空 true   非空 false
     */
    public static boolean isEmpty(EditText et) {
        if (et != null) {
            String s = et.getText().toString();
            if (!TextUtils.isEmpty(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取文本框内容
     *
     * @return 有数据  true   空false
     */
    public static String getText(EditText et) {
        if (!isEmpty(et)) {
            String s = et.getText().toString().trim();
            return s;
        }
        return "";
    }

}
