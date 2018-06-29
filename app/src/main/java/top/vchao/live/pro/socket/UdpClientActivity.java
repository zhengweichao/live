package top.vchao.live.pro.socket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import top.vchao.live.R;

public class UdpClientActivity extends AppCompatActivity {

    TextView txt_Recv, txt_Send;
    Button btn_CleanRecv, btn_Send, btn_UdpConn, btn_UdpClose;
    EditText edit_Send;
    private UDPClient client = null;
    public static Context context;
    private final MyHandler myHandler = new MyHandler(this);
    private StringBuffer udpRcvStrBuf = new StringBuffer(), udpSendStrBuf = new StringBuffer();


    MyBtnClick myBtnClick = new MyBtnClick();


    private class MyHandler extends Handler {
        private final WeakReference<UdpClientActivity> mActivity;

        public MyHandler(UdpClientActivity activity) {
            mActivity = new WeakReference<UdpClientActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    udpRcvStrBuf.append(msg.obj.toString());
                    txt_Recv.setText(udpRcvStrBuf.toString());
                    break;
                case 2:
                    udpSendStrBuf.append(msg.obj.toString());
                    txt_Send.setText(udpSendStrBuf.toString());
                    break;
                case 3:
                    txt_Recv.setText(udpRcvStrBuf.toString());
                    break;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udp_client);

        context = this;
        bindWidget();   //控件绑定
        listening();    //监听事件
        bindReceiver();//注册broadcastReceiver接收器
        iniWidget();    //初始化控件状态
    }

    private void bindWidget() {
        txt_Recv = (TextView) findViewById(R.id.txt_Recv);
        txt_Send = (TextView) findViewById(R.id.txt_Send);
        btn_CleanRecv = (Button) findViewById(R.id.btn_CleanRecv);
        btn_Send = (Button) findViewById(R.id.btn_Send);
        btn_UdpConn = (Button) findViewById(R.id.btn_udpConn);
        btn_UdpClose = (Button) findViewById(R.id.btn_udpClose);
        edit_Send = (EditText) findViewById(R.id.edit_Send);
    }

    private void listening() {
        btn_Send.setOnClickListener(myBtnClick);
        btn_UdpConn.setOnClickListener(myBtnClick);
        btn_UdpClose.setOnClickListener(myBtnClick);
        btn_CleanRecv.setOnClickListener(myBtnClick);
    }

    private void bindReceiver() {
        IntentFilter udpRcvIntentFilter = new IntentFilter("udpRcvMsg");
        registerReceiver(broadcastReceiver, udpRcvIntentFilter);
    }

    private void iniWidget() {
        btn_Send.setEnabled(false);
    }

    class MyBtnClick implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_CleanRecv:
                    udpRcvStrBuf.delete(0, udpRcvStrBuf.length());
                    Message message = new Message();
                    message.what = 3;
                    myHandler.sendMessage(message);
                    break;
                case R.id.btn_udpConn:
                    //建立线程池
                    ExecutorService exec = Executors.newCachedThreadPool();
                    client = new UDPClient();
                    exec.execute(client);
                    btn_UdpClose.setEnabled(true);
                    btn_UdpConn.setEnabled(false);
                    btn_Send.setEnabled(true);
                    break;
                case R.id.btn_udpClose:
                    client.setUdpLife(false);
                    btn_UdpConn.setEnabled(true);
                    btn_UdpClose.setEnabled(false);
                    btn_Send.setEnabled(false);
                    break;
                case R.id.btn_Send:
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message message = new Message();
                            message.what = 2;
                            if (edit_Send.getText().toString() != "") {
                                client.send(edit_Send.getText().toString());
                                message.obj = edit_Send.getText().toString();
                                myHandler.sendMessage(message);
                            }

                        }
                    });
                    thread.start();
                    break;
            }
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("udpRcvMsg")) {
                Message message = new Message();
                message.obj = intent.getStringExtra("udpRcvMsg");
                message.what = 1;
                Log.i("主界面Broadcast", "收到" + message.obj.toString());
                myHandler.sendMessage(message);
            }
        }
    };

}
