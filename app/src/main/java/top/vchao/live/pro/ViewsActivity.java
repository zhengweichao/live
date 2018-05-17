package top.vchao.live.pro;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import top.vchao.live.R;
import top.vchao.live.mainUi.base.BaseActivity;
import top.vchao.live.view.ZoomImageView;

public class ViewsActivity extends BaseActivity {

    @BindView(R.id.zoomImageView)
    ZoomImageView zoomImageView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_views;
    }

    @Override
    public void initView() {
        Glide.with(ViewsActivity.this)
                .load(R.mipmap.car)
                .into(zoomImageView);
    }


}
