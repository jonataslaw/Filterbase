package com.edit.photomovie.segment.animation;

import android.animation.TimeInterpolator;
import android.view.animation.LinearInterpolator;

 
public abstract class SegmentAnimation {
    protected TimeInterpolator mInterpolator = new LinearInterpolator();

    public abstract Object update(float progress);

    public void setInterpolator(TimeInterpolator interpolator) {
        mInterpolator = interpolator;
    }
}
