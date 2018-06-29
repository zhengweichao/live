package top.vchao.live.pro.udp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.vchao.live.R;
import top.vchao.live.utils.ToastUtils;

public class UDPActivity extends Activity {

    @BindView(R.id.button7)
    Button button7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udp);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button7)
    public void onViewClicked() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                //Looper.prepare();
                sendDataByUDP();
            }
        }).start();
    }

    private UdpMessageTool mUdpMessageTool;
    // 服务器主机ip
    private static final String HOST = "10.10.100.254";
    // 服务器请求端口号
    private static final int PORT = 8899;
    // 随便定义的发送内容，发送格式是与服务器端协议
    private static final String CONTENT = "SEND MESSAGE?key1=abc&key2=cba";
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            if (!TextUtils.isEmpty(result)) {
                ToastUtils.showShort("收到的数据是：" + result);
            }
        }
    };

    private void sendDataByUDP() {
        try {
            mUdpMessageTool = UdpMessageTool.getInstance();
            mUdpMessageTool.setTimeOut(5000);// 设置超时为5s
            // 向服务器发数据
            mUdpMessageTool.send(HOST, PORT, CONTENT.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String result = mUdpMessageTool.receive(HOST, PORT);
            Thread.sleep(2000);
            if (result == null) {
                Message msg = new Message();
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mUdpMessageTool.close();
    }

}
