package top.vchao.live.pro.litepal;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.vchao.live.R;
import top.vchao.live.utils.ToastUtils;

public class LitePalActivity extends AppCompatActivity {

    @BindView(R.id.tv_lite_content)
    TextView tvLiteContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lite_pal);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.lite_pal_create, R.id.lite_pal_add, R.id.lite_pal_delete, R.id.lite_pal_update, R.id.lite_pal_query})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lite_pal_create:
                SQLiteDatabase db = LitePal.getDatabase();
                ToastUtils.showShort("创建数据库成功");

                break;
            case R.id.lite_pal_add:
                Album album = new Album();
                album.setName("album");
                album.setPrice(10.99f);
                album.save();

                Song song1 = new Song();
                song1.setName("song1");
                song1.setDuration(320);
                song1.setAlbum(album);
                song1.save();

                Song song2 = new Song();
                song2.setName("song2");
                song2.setDuration(356);
                song2.setAlbum(album);
                song2.save();
//                song2.
                query();
                break;
            case R.id.lite_pal_delete:
                LitePal.deleteAll(Song.class);
                query();
                break;
            case R.id.lite_pal_update:
                Album albumToUpdate = LitePal.find(Album.class, 1);
                albumToUpdate.setPrice(20.99f); // raise the price
                albumToUpdate.save();

               /* Album albumToUpdate = new Album();
                albumToUpdate.setPrice(20.99f); // raise the price
                albumToUpdate.update(id);
                Or you can update multiple records with a where condition:

                Album albumToUpdate = new Album();
                albumToUpdate.setPrice(20.99f); // raise the price
                albumToUpdate.updateAll("name = ?", "album");*/
                break;
            case R.id.lite_pal_query:
                query();

                break;
        }
    }

    private void query() {
        List<Song> allSongs = LitePal.findAll(Song.class);
        String s = "";
        for (Song song : allSongs) {
            s = s + song.getId() +"   "+ song.getName()+"   " + song.getDuration()+"   " + song.getAlbum() + "\n";
        }
        tvLiteContent.setText(TextUtils.isEmpty(s) ? "无数据" : s);
    }
}
