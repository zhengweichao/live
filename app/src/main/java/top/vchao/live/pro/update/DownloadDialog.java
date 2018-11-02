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
public class DownloadDialog extends Dialog {
    private Context context;

    public DownloadDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.setContentView(R.layout.download_dialog);
    }

    public DownloadDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        this.setContentView(R.layout.download_dialog);

    }
}
