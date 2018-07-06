package top.vchao.live.pro.newudp;
//网络状态枚举类
public enum NetworkState {
    NETWORK_STATE_NULL, // 无状态
    NETWORK_STATE_CONNECT_SUCCEED, // 网络连接成功
    NETWORK_STATE_DISCONNECT_SUCCEED, // 网络断开成功(自身断开)
    NETWORK_STATE_SERVER_DISCONNECT, // 网络被服务器断开
    NETWORK_STATE_CONNECT_FAILLD, // 连接服务器失败，IP/端口不正常
    NETWORK_STATE_CONNECT_ING, // 正在连接过程中...
    NETWORK_STATE_RXD, // 接收数据
    NETWORK_STATE_TXD; // 发送数据

}
