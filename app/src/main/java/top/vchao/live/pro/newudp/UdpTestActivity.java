package top.vchao.live.pro.newudp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import top.vchao.live.R;

public class UdpTestActivity extends AppCompatActivity implements View.OnClickListener {


    UDPSocketClientManage socketClientManage = null;
    private String mstrDataString = "";
    private TextView textViewRecrive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udp_test);


        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        TextView loTextView = (TextView) findViewById(R.id.textViewLoca);

        //手机端的连接路由之后IP地址，网络调试助手向目标主机发送的IP地址就是这里获取出来的
        String strLoString = UDPSocketClientManage.getLocalIpAddress();
        if (strLoString != null) {
            loTextView.setText(strLoString);
        }
        textViewRecrive = (TextView) findViewById(R.id.textViewRecrive);

        socketClientManage = new UDPSocketClientManage();
        socketClientManage.RegSocketConnectListener(new SocketConnectListener() {

            @Override
            public void OnReceiverCallBack(int nLength, byte[] data) {
                mstrDataString = new String(data);
                mHandler.sendEmptyMessage(1);
            }

            @Override
            public void OnConnectStatusCallBack(NetworkState networkState) {
                switch (networkState) {
                    case NETWORK_STATE_CONNECT_SUCCEED:
                        mHandler.sendEmptyMessage(0);
                        break;

                    default:
                        break;
                }

            }
        });
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: // 接受到消息之后，对UI控件进行修改
                    Toast.makeText(UdpTestActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                    break;
                case 1: // 接受到消息之后，对UI控件进行修改
                    textViewRecrive.append(mstrDataString);
                    Toast.makeText(UdpTestActivity.this, mstrDataString, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        socketClientManage.Connect();

                    }
                }).start();

                break;
            case R.id.button2:
                EditText ipEditText = (EditText) findViewById(R.id.editText1);
                EditText porText = (EditText) findViewById(R.id.editText2);
                String ipString = ipEditText.getText().toString().trim();
                String portString = porText.getText().toString().trim();
                socketClientManage.setNetworkParameter(ipString, portString != null ? Integer.parseInt(portString) : 0);
                Toast.makeText(UdpTestActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button3:
                EditText sendEditText = (EditText) findViewById(R.id.editText3);
                String sendDataString = sendEditText.getText().toString().trim();
                if (sendDataString != null)
                    socketClientManage.send(sendDataString.getBytes(), sendDataString.getBytes().length);
                break;
            default:
                break;
        }

    }


}
