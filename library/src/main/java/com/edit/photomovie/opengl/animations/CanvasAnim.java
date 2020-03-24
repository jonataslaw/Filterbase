package com.edit.photomovie.opengl.animations;


import com.edit.photomovie.opengl.GLESCanvas;

public abstract class CanvasAnim extends Animation {

    public abstract int getCanvasSaveFlags();

    public abstract void apply(GLESCanvas canvas);
}
