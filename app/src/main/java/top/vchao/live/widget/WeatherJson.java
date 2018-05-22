package top.vchao.live.widget;

/**
 * @ Create_time: 2018/5/22 on 10:25.
 * @ description： 天气信息的json类。等田写完接口了补充吧
 * @ author: vchao  blog: http://blog.csdn.net/zheng_weichao
 */
public class WeatherJson {
    private String temp;

    public WeatherJson() {
    }

    public WeatherJson(String temp) {
        this.temp = temp;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}
