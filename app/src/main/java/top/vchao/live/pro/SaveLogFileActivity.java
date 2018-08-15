package top.vchao.live.pro;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.OnClick;
import top.vchao.live.R;
import top.vchao.live.utils.LogUtils;

public class SaveLogFileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_log_file);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button15)
    public void onViewClicked() {
        writeLogtoFile("添加一条日志");

    }
    private static void writeLogtoFile(String text) {
        String path = Environment.getExternalStorageDirectory().getPath()+"/testlog/";
        LogUtils.i("日志将要保存的路径 ： "+path);
        Date D = new Date(System.currentTimeMillis());
        SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.US);
        String S = SDF.format(D);

        String needWriteMessage = S + "  " + text;

        File logdir = new File(path);// 如果没有log文件夹则新建该文件夹
        if (!logdir.exists()) {
            logdir.mkdirs();
        }
        File file = new File(path + "/log0815.txt");
        try {
            FileWriter filerWriter = new FileWriter(file, true);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(needWriteMessage);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
