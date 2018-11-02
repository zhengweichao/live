package top.vchao.live.pro.update;

import android.app.Dialog;
import android.content.Context;

import top.vchao.live.R;


/**
 * @className： BindingPhoneDialog
 * @classDescription： 版本更新
 * @author： 万
 * @createTime： 2017/12/4 19:48
 */
public class UpdateDialog extends Dialog {
    private Context context;

    public UpdateDialog(Context context) {
        super(context);
        this.context = context;
        this.setContentView(R.layout.update_dialog);
    }

    public UpdateDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        this.setContentView(R.layout.update_dialog);
    }
}
