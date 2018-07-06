package top.vchao.live.pro.newudp;

public abstract class SocketConnectListener {
	// 网络状态回调
	public abstract void OnConnectStatusCallBack(NetworkState networkState);

	// 接收数据回调
	public abstract void OnReceiverCallBack(int nLength, byte[] data);


}
