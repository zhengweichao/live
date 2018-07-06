package top.vchao.live.pro.newudp;

import android.annotation.SuppressLint;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class UDPSocketClientManage {
    // 服务器IP
    private static String SERVER_IP = "172.16.160.37";
    // 服务器端口
    private static int LOCAL_PORT_AUDIO = 8899;
    // 接收数据包
    private DatagramPacket Packet_Receive;
    // 端口
    private DatagramSocket msocketClient;

    NetworkState mLastNetworkState = NetworkState.NETWORK_STATE_NULL;
    SocketConnectListener mConnectListener = null;

    // 设置网络连接参数
    public void setNetworkParameter(String strIP, int nPort) {
        SERVER_IP = strIP;
        LOCAL_PORT_AUDIO = nPort;
    }

    // 注册接收连接状态和数据的回调函数
    public void RegSocketConnectListener(SocketConnectListener listener) {
        mConnectListener = listener;
    }

    /**
     * 启动连接服务器
     */
    public void Connect() {
        // 正在开始连接
        mLastNetworkState = NetworkState.NETWORK_STATE_CONNECT_ING;

        try {
            // 端口
            msocketClient = new DatagramSocket(LOCAL_PORT_AUDIO);
            // 接收数据缓存
            byte[] Buffer_Receive = new byte[1024];
            // 接收包
            Packet_Receive = new DatagramPacket(Buffer_Receive, 1024);

            mLastNetworkState = NetworkState.NETWORK_STATE_CONNECT_SUCCEED;

        } catch (IOException e) {
            mLastNetworkState = NetworkState.NETWORK_STATE_CONNECT_FAILLD;
            Log.e("Show", e.toString());
            e.printStackTrace();
        } catch (Exception e) {
            mLastNetworkState = NetworkState.NETWORK_STATE_CONNECT_FAILLD;
            Log.e("Show", e.toString());
            e.printStackTrace();
        }

        // 向回调发数据
        if (null != mConnectListener) {
            mConnectListener.OnConnectStatusCallBack(mLastNetworkState);
        }

        if (msocketClient != null) {
            new Thread(reRunnable).start();
        }
    }

    Runnable reRunnable = new Runnable() {
        @SuppressLint("NewApi")
        @Override
        public void run() {
            while (true) {
                try {
                    // 接收数据
                    if (Packet_Receive != null) {
                        msocketClient.receive(Packet_Receive);

                        // 判断数据是否合法
                        InetSocketAddress address = (InetSocketAddress) Packet_Receive.getSocketAddress();
                        // 判断是否是调度服务器的ip
                        if (!address.getHostName().equals(SERVER_IP)) {
                            continue;
                        }
                        // 判断是否是调度服务器的端口
                        if (address.getPort() != LOCAL_PORT_AUDIO) {
                            continue;
                        }

                        int length = Packet_Receive.getLength();
                        if (length > 0)
                            mConnectListener.OnReceiverCallBack(length, Packet_Receive.getData());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("Show", e.toString());
                }
            }
        }
    };

    /**
     * 断开连接
     */
    public void Close() {
        if (msocketClient != null) {
            msocketClient = null;
            mLastNetworkState = NetworkState.NETWORK_STATE_DISCONNECT_SUCCEED;
            mConnectListener.OnConnectStatusCallBack(mLastNetworkState);
        }
    }

    /**
     * @param data :需要发送的数据
     * @param len  :数据字节数据
     * @brief 发送数据
     */
    public void send(byte[] data, int len) {
        Thread_Send thread_send = new Thread_Send(data, len);
        new Thread(thread_send).start();
    }

    /**
     * @brief 发送线程
     */
    private class Thread_Send implements Runnable {
        // 发送数据缓存
        private byte[] Buffer_Send = new byte[1024];
        // 发送数据包
        private DatagramPacket Packet_Send;

        /**
         * @param data :需要发送的数据
         * @param len  :数据字节数据
         * @brief 构造函数
         */
        public Thread_Send(byte[] data, int len) {
            // 发送包
            Packet_Send = new DatagramPacket(Buffer_Send, 1024);
            Packet_Send.setData(data);
            Packet_Send.setLength(len);
        }

        @Override
        public void run() {
            try {
                Packet_Send.setPort(LOCAL_PORT_AUDIO);
                Packet_Send.setAddress(InetAddress.getByName(SERVER_IP));
                if (msocketClient != null) {
                    msocketClient.send(Packet_Send);
                    mLastNetworkState = NetworkState.NETWORK_STATE_TXD;
                    mConnectListener.OnConnectStatusCallBack(mLastNetworkState);
                } else {
                    mLastNetworkState = NetworkState.NETWORK_STATE_NULL;
                    mConnectListener.OnConnectStatusCallBack(mLastNetworkState);
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
                mLastNetworkState = NetworkState.NETWORK_STATE_NULL;
                mConnectListener.OnConnectStatusCallBack(mLastNetworkState);
            } catch (IOException e) {
                e.printStackTrace();
                mLastNetworkState = NetworkState.NETWORK_STATE_NULL;
                mConnectListener.OnConnectStatusCallBack(mLastNetworkState);
            }
        }
    }

    // 获取最后的网络状态
    public NetworkState getLastNetworkState() {
        return mLastNetworkState;
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("WifiPreferene IpAddress", ex.toString());
        }
        return null;
    }

}
