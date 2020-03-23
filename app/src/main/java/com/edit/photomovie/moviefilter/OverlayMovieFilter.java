package com.edit.photomovie.moviefilter;

public class OverlayMovieFilter extends BaseMovieFilter {
    protected static final String FRAGMENT_SHADER =
            "#extension GL_OES_EGL_image_external : require\n" +
                    "precision mediump float;\n" +
                    "             uniform sampler2D inputImageTexture; \n" +
                    "             varying vec2 textureCoordinate; \n" +
                    "  \n" +
                    "             void main() \n" +
                    "             { \n" +
                    "              vec4 sample0,sample1,sample2,sample3; \n" +
                    "              float fstep=" +  0.0015f +"; \n" +
                    "              sample0=texture2D(inputImageTexture,vec2(textureCoordinate.x-fstep,textureCoordinate.y-fstep)); \n" +
                    "              sample1=texture2D(inputImageTexture,vec2(textureCoordinate.x+fstep,textureCoordinate.y-fstep)); \n" +
                    "              sample2=texture2D(inputImageTexture,vec2(textureCoordinate.x+fstep,textureCoordinate.y+fstep)); \n" +
                    "              sample3=texture2D(inputImageTexture,vec2(textureCoordinate.x-fstep,textureCoordinate.y+fstep)); \n" +
                    "              vec4 color=(sample0+sample1+sample2+sample3) / 4.0; \n" +
                    "              gl_FragColor=color; \n" +
                    "             } ";
    public OverlayMovieFilter(){
        super(VERTEX_SHADER,FRAGMENT_SHADER);
    }
}
