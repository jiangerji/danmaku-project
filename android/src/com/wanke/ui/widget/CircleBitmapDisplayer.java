package com.wanke.ui.widget;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.wanke.WankeTVApplication;
import com.wanke.tv.R;
import com.wanke.ui.UiUtils;

/**
 * 显示圆形的ImageLoader应用的显示器
 * 
 */

public class CircleBitmapDisplayer implements BitmapDisplayer {

    protected final int margin;

    public CircleBitmapDisplayer() {
        this(0);
    }

    public CircleBitmapDisplayer(int margin) {
        this.margin = margin;
    }

    @Override
    public void display(
            Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        if (!(imageAware instanceof ImageViewAware)) {
            throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
        }

        imageAware.setImageDrawable(new CircleDrawable(bitmap, margin));
        //        int targetWidth = ((ImageViewAware) imageAware).getWidth();
        //        int targetHeight = ((ImageViewAware) imageAware).getHeight();
        //        imageAware.setImageBitmap(processAlbum(bitmap,
        //                targetWidth,
        //                targetHeight));
    }

    @SuppressWarnings("unused")
    private Bitmap processAlbum(
            Bitmap albumBitmap, int targetWidth, int targetHeight) {
        Drawable drawable = WankeTVApplication.getCurrentApplication()
                .getResources().getDrawable(R.drawable.default_album_mask);

        // 获得mask bitmap
        Bitmap mask = UiUtils.drawableToBitmap(drawable);

        Bitmap output = Bitmap.createBitmap(targetWidth, targetHeight,
                Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);

        // 绘制mask
        Matrix matrix = new Matrix();
        matrix.postScale(((float) targetWidth) / mask.getWidth(),
                ((float) targetHeight) / mask.getHeight());
        canvas.drawBitmap(mask, matrix, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

        // 绘制用户头像
        matrix = new Matrix();
        matrix.postScale(((float) targetWidth) / albumBitmap.getWidth(),
                ((float) targetHeight) / albumBitmap.getHeight());

        canvas.drawBitmap(albumBitmap, matrix, paint);
        paint.setXfermode(null);

        mask.recycle();
        return output;
    }
}
