package top.vchao.live.pro.litepal;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * @ Create_time: 2018/6/27 on 15:20.
 * @ description：
 * @ author: vchao  blog: http://blog.csdn.net/zheng_weichao
 */
public class Song extends LitePalSupport {

    @Column(nullable = false)
    private String name;

    private int id;
    private int duration;

    @Column(ignore = true)
    private String uselessField;

    private Album album;

    // generated getters and setters.
//    ...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getUselessField() {
        return uselessField;
    }

    public void setUselessField(String uselessField) {
        this.uselessField = uselessField;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}