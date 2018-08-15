package top.vchao.live.pro.excel;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.OnClick;
import top.vchao.live.R;
import top.vchao.live.mainUi.base.BaseActivity;
import top.vchao.live.utils.LogUtils;

public class ExcelActivity extends BaseActivity {

    List<Order> orders = new ArrayList<Order>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_excel;
    }

    @Override
    public void initView() {
        loadPermission();
    }

    @Override
    public void initData() {
        int length = Const.OrderInfo.orderOne.length;
        for (int i = 0; i < length; i++) {
            Order order = new Order(Const.OrderInfo.orderOne[i][0], Const.OrderInfo.orderOne[i][1], Const.OrderInfo.orderOne[i][2], Const.OrderInfo.orderOne[i][3]);
            orders.add(order);
        }

    }

    private void loadPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionList = new ArrayList<>();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.CALL_PHONE);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.WRITE_SETTINGS);
            }
            if (!permissionList.isEmpty()) {  //申请的集合不为空时，表示有需要申请的权限
                requestPermissions(permissionList.toArray(new String[permissionList.size()]), 1);
            } else { //所有的权限都已经授权过了
            }
        }
    }

    @OnClick(R.id.button14)
    public void onViewClicked() {
        try {
            ExcelUtil.writeExcel(this, orders, "excel_01" );
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogUtils.e("6666666666");
        }

        /*try {
            ExcelUtils.initExcel("test.xls", new String[]{"aa", "bb", "cc"});
            ArrayList<BillObject> billObjects = new ArrayList<>();
            billObjects.add(new BillObject("001", "002", "003"));
            billObjects.add(new BillObject("011", "012", "013"));
            billObjects.add(new BillObject("021", "022", "023"));
            billObjects.add(new BillObject("031", "032", "033"));
            ExcelUtils.writeObjListToExcel(billObjects, "test.xls", ExcelActivity.this);
            LogUtils.e("成功");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
