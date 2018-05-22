package top.vchao.live.widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import top.vchao.live.R;
import top.vchao.live.utils.LogUtils;
import top.vchao.live.utils.TimeUtil;

/**
 * @ Create_time: 2018/5/21 on 15:34.
 * @ description：
 * @ author: vchao  blog: http://blog.csdn.net/zheng_weichao
 */
public class liveWidgetService extends Service {
    AppWidgetManager awm;
    ComponentName appWidgetProvider;
    Timer timer;
    TimerTask task;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 获得widget管理者
        awm = AppWidgetManager.getInstance(this);

        // 开启定时任务 每隔 28 秒更新widget
        timer = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                // 初始化一个远程的view(RemoteViews)
                RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_process_safer);


                String strTime = TimeUtil.getNowTime();
                String week = TimeUtil.getTodayWeek();
                SimpleDateFormat date_Format = new SimpleDateFormat("MM月dd日");
                String date = TimeUtil.getTime(TimeUtil.getCurrentTimeInLong(), date_Format);

                LogUtils.e(strTime + week);
                // 设置views内容
                views.setTextViewText(R.id.tv_count_widget, "" + strTime);
                views.setTextViewText(R.id.tv_freeMem_widget, date + " " + week);


                LogUtils.e("" + getPackageName());
                WeatherQueryImpl weatherQuery = new WeatherQueryImpl();
                // TODO: 2018/5/22  这里进行天气数据等的设置
                WeatherJson localWeather = weatherQuery.weatherQuery();
                views.setTextViewText(R.id.weather, localWeather.getTemp());


                appWidgetProvider = new ComponentName(getApplicationContext(), liveWidgetProvider.class);
                Intent intent1 = new Intent();
                ComponentName component = new ComponentName(getPackageName(), "top.vchao.live.MainActivity");
                intent1.setComponent(component);
                intent1.putExtra("category_value", "1");
                intent1.putExtra("title", "上门洗车");

                PendingIntent intentAction = PendingIntent.getActivity(getApplicationContext(), 0, intent1, 0);
                views.setOnClickPendingIntent(R.id.bt_clean, intentAction);

                awm.updateAppWidget(appWidgetProvider, views);
            }
        };
        timer.schedule(task, 1000, 28000);
        return super.onStartCommand(intent, flags, startId);
    }
}
