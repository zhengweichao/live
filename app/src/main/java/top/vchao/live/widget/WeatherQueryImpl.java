package top.vchao.live.widget;

/**
 * @ Create_time: 2018/5/22 on 10:25.
 * @ description： 查询天气
 * @ author: vchao  blog: http://blog.csdn.net/zheng_weichao
 */
public class WeatherQueryImpl {

    public WeatherJson weatherQuery() {

        // TODO: 2018/5/22 执行联网逻辑

        WeatherJson wf = new WeatherJson("雨天");
        return wf;
    }

}
