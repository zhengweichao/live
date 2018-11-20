package top.vchao.live.pro.ui;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import top.vchao.live.R;
import top.vchao.live.mainUi.base.BaseActivity;
import top.vchao.live.utils.LogUtils;

public class RxjavaActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_rxjava;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        // 第一步：初始化Observable   被观察者（小偷）
        ObservableOnSubscribe observableOnSubscribe = new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                LogUtils.e("Observable emit 1" + "\n");
                e.onNext(1);
                LogUtils.e("Observable emit 2" + "\n");
                e.onNext(2);
                LogUtils.e("Observable emit 3" + "\n");
                e.onNext(3);
                e.onComplete();
                LogUtils.e("Observable emit 4" + "\n");
                e.onNext(4);
            }
        };
        // 第二步：初始化Observer  观察（警察）
        Observer observer = new Observer<Integer>() {
            private int i;
            private Disposable mDisposable;

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                i++;
                if (i == 2) {
                    // 在RxJava 2.x 中，新增的Disposable可以做到切断的操作，让Observer观察者不再接收上游事件
                    mDisposable.dispose();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                LogUtils.e("onError : value : " + e.getMessage() + "\n");
            }

            @Override
            public void onComplete() {
                LogUtils.e("onComplete" + "\n");
            }
        };
        // 第三步：订阅
        Observable.create(observableOnSubscribe).subscribe(observer);
    }
}
