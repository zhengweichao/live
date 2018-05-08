package top.vchao.live.utils;

import android.content.ClipboardManager;
import android.content.Context;

/**
 * @ 创建时间: 2017/11/1 on 14:14.
 * @ 描述：复制粘贴工具类
 * @ 作者: 郑卫超 QQ: 2318723605
 */
public class CopyUtils {
    /**
     * 复制
     *
     * @param content
     * @param context
     */
    public static void copy(String content, Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * 粘贴
     *
     * @param context
     * @return
     */
    public static String paste(Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }
}

