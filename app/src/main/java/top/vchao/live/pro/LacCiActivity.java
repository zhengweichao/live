package top.vchao.live.pro;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.vchao.live.R;
import top.vchao.live.mainUi.base.BaseActivity;
import top.vchao.live.utils.LogUtils;

//<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
public class LacCiActivity extends BaseActivity {

    @BindView(R.id.button12)
    Button button12;
    @BindView(R.id.textView11)
    TextView textView11;

    @Override
    public int getLayoutId() {
        return R.layout.activity_lac_ci;
    }

    @OnClick(R.id.button12)
    public void onViewClicked() {

        try {
            TelephonyManager mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            // 返回值MCC + MNC
            String operator = mTelephonyManager.getNetworkOperator();
//            int mcc = Integer.parseInt(operator.substring(0, 3));
//            int mnc = Integer.parseInt(operator.substring(3));
            // 中国移动和中国联通获取LAC、CID的方式
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            GsmCellLocation location = (GsmCellLocation) mTelephonyManager.getCellLocation();

            int lac = location.getLac();

            int cellId = location.getCid();


            LogUtils.e("\t LAC = " + lac + "\t CID = " + cellId);
//            LogUtils.e(" MCC = " + mcc + "\t MNC = " + mnc + "\t LAC = " + lac + "\t CID = " + cellId);


            // 中国电信获取LAC、CID的方式

                /*CdmaCellLocation location1 = (CdmaCellLocation) mTelephonyManager.getCellLocation();

                lac = location1.getNetworkId();

                cellId = location1.getBaseStationId();

                cellId /= 16;*/


            // 获取邻区基站信息

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            List<NeighboringCellInfo> infos = mTelephonyManager.getNeighboringCellInfo();

            StringBuffer sb = new StringBuffer("总数 : " + infos.size() + "\n");

            for (NeighboringCellInfo info1 : infos) { // 根据邻区总数进行循环

                sb.append(" LAC : " + info1.getLac()); // 取出当前邻区的LAC

                sb.append(" CID : " + info1.getCid()); // 取出当前邻区的CID

                sb.append(" BSSS : " + (-113 + 2 * info1.getRssi()) + "\n"); // 获取邻区基站信号强度

            }
            LogUtils.e(" 获取邻区基站信息:" + sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
