package com.wanke.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.wanke.tv.R;
import com.wanke.ui.UiUtils;

/**
 * 圆形的Imageview
 * 
 */
public class CircleImageView extends ImageView {
    private Paint paint = new Paint();

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private Rect mRect = new Rect();

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();
        if (null != drawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap b = toRoundCorner(bitmap, 14);
            mRect.set(0, 0, b.getWidth(), b.getHeight());
            paint.reset();
            canvas.drawBitmap(b,
                    mRect,
                    new Rect(0, 0, getWidth(), getHeight()),
                    paint);
        } else {
            super.onDraw(canvas);
        }
    }

    private Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        int x = bitmap.getWidth();
        canvas.drawCircle(x / 2, x / 2, x / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    private Bitmap processAlbum(Bitmap albumBitmap) {
        Drawable drawable = getResources()
                .getDrawable(R.drawable.default_album_mask);

        // 获得mask bitmap
        Bitmap mask = UiUtils.drawableToBitmap(drawable);

        Bitmap output = Bitmap.createBitmap(getWidth(), getHeight(),
                Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);

        // 绘制mask
        Matrix matrix = new Matrix();
        matrix.postScale(((float) getWidth()) / mask.getWidth(),
                ((float) getHeight()) / mask.getHeight());
        canvas.drawBitmap(mask, matrix, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

        // 绘制用户头像
        matrix = new Matrix();
        matrix.postScale(((float) getWidth()) / albumBitmap.getWidth(),
                ((float) getHeight()) / albumBitmap.getHeight());

        canvas.drawBitmap(albumBitmap, matrix, paint);
        paint.setXfermode(null);

        mask.recycle();
        return output;
    }
}
