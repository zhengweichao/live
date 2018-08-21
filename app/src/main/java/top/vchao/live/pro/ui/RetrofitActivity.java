package top.vchao.live.pro.ui;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import top.vchao.live.R;
import top.vchao.live.mainUi.base.BaseActivity;
import top.vchao.live.pro.bean.GetRequest_Interface;
import top.vchao.live.pro.bean.Translation;
import top.vchao.live.utils.ToastUtils;

public class RetrofitActivity extends BaseActivity {

    @BindView(R.id.textView12)
    TextView textView12;

    @Override
    public int getLayoutId() {
        return R.layout.activity_retrofit;
    }

    public void request() {

        //步骤4:创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fy.iciba.com/") // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .build();

        // 步骤5:创建 网络请求接口 的实例
        final GetRequest_Interface request = retrofit.create(GetRequest_Interface.class);

        //对 发送请求 进行封装
        Call<Translation> call = request.getCall();

        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<Translation>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<Translation> call, Response<Translation> response) {
                // 步骤7：处理返回的数据结果
                Translation body = response.body();
                ToastUtils.showShort("连接成功");
                textView12.setText(body.getData().getDescription());
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<Translation> call, Throwable throwable) {
                ToastUtils.showShort("连接失败");
            }
        });
    }

    @OnClick(R.id.button16)
    public void onViewClicked() {
        request();
    }
}
