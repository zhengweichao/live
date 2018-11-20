package top.vchao.live.pro.bean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * @ Create_time: 2018/8/20 on 15:08.
 * @ description：
 * @ author: vchao  blog: http://blog.csdn.net/zheng_weichao
 */
public interface GetRequest_Interface {

    //    @GET("ajax.php?a=fy&f=auto&t=auto&w=hello%20world")
    @GET("/update")
    Call<Translation> getCall();

// 注解里传入 网络请求 的部分URL地址
    // Retrofit把网络请求的URL分成了两部分：一部分放在Retrofit对象里，另一部分放在网络请求接口里
    // 如果接口里的url是一个完整的网址，那么放在Retrofit对象里的URL可以忽略
    // getCall()是接受网络请求数据的方法

}
