package top.vchao.live.pro;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import top.vchao.live.R;

public class SiYiFuActivity extends Activity {

    private ImageView iv_after;
    private ImageView iv_before;
    private Bitmap alertBitmap;
    private Paint paint;
    private Canvas canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_si_yi_fu);

        iv_after = (ImageView) findViewById(R.id.iv_after);
        iv_before = (ImageView) findViewById(R.id.iv_before);
        BitmapFactory.Options opts=new BitmapFactory.Options();
        opts.inSampleSize=1;
        Bitmap after = BitmapFactory.decodeResource(getResources(), R.mipmap.after0, opts);
        Bitmap before = BitmapFactory.decodeResource(getResources(), R.mipmap.pre0, opts);
        alertBitmap = Bitmap.createBitmap(before.getWidth(),before.getHeight(),before.getConfig());
        paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(Color.BLACK);
        canvas = new Canvas(alertBitmap);
        canvas.drawBitmap(before, new Matrix(), paint);

        iv_after.setImageBitmap(after);
        iv_before.setImageBitmap(alertBitmap);
        iv_before.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                try {
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_MOVE:
                            int newX=(int) event.getX();
                            int newY=(int) event.getY();
                            for(int i=-10;i<10;i++){
                                for(int j=-10;j<10;j++){
                                    int r=(int) Math.sqrt(i*i+j*j);
                                    if(r<10){
                                        alertBitmap.setPixel(i+newX, j+newY, Color.TRANSPARENT);
                                    }
                                }
                            }
                            iv_before.setImageBitmap(alertBitmap);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }
}
