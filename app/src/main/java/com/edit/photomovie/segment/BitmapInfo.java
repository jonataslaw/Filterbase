package com.edit.photomovie.segment;

import android.graphics.Rect;
import android.graphics.RectF;
import com.edit.photomovie.opengl.BitmapTexture;
import com.edit.photomovie.opengl.GLESCanvas;
import com.edit.photomovie.util.PhotoUtil;
import com.edit.photomovie.util.ScaleType;

/**
 * Created by huangwei on 2018/9/4 0004.
 */
public class BitmapInfo {
    public BitmapTexture bitmapTexture;
    public Rect srcRect = new Rect();
    public RectF srcShowRect = new RectF();
    public ScaleType scaleType = ScaleType.CENTER_CROP;

    public void applyScaleType(RectF dstRect) {
        if (dstRect == null || dstRect.width() <= 0 || dstRect.height() <= 0) {
            srcShowRect.set(srcRect);
        }

        switch (scaleType){
            case CENTER_CROP:
                srcShowRect.set(PhotoUtil.getCroppedRect(null,
                        srcRect.width(),
                        srcRect.height(),
                        dstRect.width(),
                        dstRect.height()));
                break;
            case FIT_XY:
            case FIT_CENTER:
                srcShowRect.set(srcRect);
                break;
        }
    }

    public boolean isTextureAvailable() {
        return bitmapTexture != null && bitmapTexture.isLoaded();
    }

    /**
     * 如果材质不可用，尝试重新加载一次
     *
     * @param canvas
     * @return
     */
    public boolean makeTextureAvailable(GLESCanvas canvas) {
        if (bitmapTexture == null) {
            return false;
        }
        if (bitmapTexture.isLoaded()) {
            return true;
        }
        bitmapTexture.updateContent(canvas);
        return bitmapTexture.isLoaded();
    }
}
