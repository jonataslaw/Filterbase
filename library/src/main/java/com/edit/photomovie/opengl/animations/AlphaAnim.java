package com.edit.photomovie.opengl.animations;


import com.edit.photomovie.opengl.GLESCanvas;
import com.edit.photomovie.util.Utils;

public class AlphaAnim extends CanvasAnim {
    private final float mStartAlpha;
    private final float mEndAlpha;
    private float mCurrentAlpha;

    public AlphaAnim(float from, float to) {
        mStartAlpha = from;
        mEndAlpha = to;
        mCurrentAlpha = from;
    }

    @Override
    public void apply(GLESCanvas canvas) {
        canvas.multiplyAlpha(mCurrentAlpha);
    }

    @Override
    public int getCanvasSaveFlags() {
        return GLESCanvas.SAVE_FLAG_ALPHA;
    }

    @Override
    protected void onCalculate(float progress) {
        mCurrentAlpha = Utils.clamp(mStartAlpha + (mEndAlpha - mStartAlpha) * progress, 0f, 1f);
    }
}
