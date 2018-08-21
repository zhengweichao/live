package top.vchao.live.pro;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;
import top.vchao.live.R;

public class PortActivity extends Activity {

    protected SerialPort mSerialPort;
    protected InputStream mInputStream;
    protected OutputStream mOutputStream;
    private ReadThread mReadThread;

    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();

            while (!isInterrupted()) {
                int size;
                Log.i("test", "接收线程已经开启");
                try {
                    byte[] buffer = new byte[64];

                    if (mInputStream == null)
                        return;

                    size = mInputStream.read(buffer);

                    if (size > 0) {
                        onDataReceived(buffer, size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_port);

        try {
            mSerialPort = new SerialPort(new File("/dev/ttyS2"), 9600, 0);
            mInputStream = mSerialPort.getInputStream();
            mOutputStream = mSerialPort.getOutputStream();
            for (int i = 0; i < Long.MAX_VALUE; i++) {
                mOutputStream.write("abcdefghijklmn".getBytes());
                Log.i("test", "发送成功" + i);
            }
            mReadThread = new ReadThread();
            mReadThread.start();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("test", "发送失败");
            e.printStackTrace();
        }

        Button btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mOutputStream.write("abcdefghijklmn".getBytes());
                    Log.i("test", "发送成功");
                } catch (IOException e) {
                    Log.i("test", "发送失败");
                    e.printStackTrace();
                }
            }
        });

    }

    protected void onDataReceived(final byte[] buffer, final int size) {
        runOnUiThread(new Runnable() {
            public void run() {
                String recinfo = new String(buffer, 0, size);
                Log.i("test", "接收到串口信息======" + recinfo.getBytes());
            }
        });
    }
}
