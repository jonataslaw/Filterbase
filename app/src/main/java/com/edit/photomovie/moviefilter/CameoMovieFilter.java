package com.edit.photomovie.moviefilter;

import android.opengl.GLES20;
import com.edit.photomovie.PhotoMovie;
import com.edit.photomovie.opengl.FboTexture;


public class CameoMovieFilter extends BaseMovieFilter {

    protected static final String FRAGMENT_SHADER =
            "#extension GL_OES_EGL_image_external : require\n" +
            "precision mediump float;\n" +
            "uniform samplerExternalOES sTexture;  \n" +
            "  \n" +
            "varying vec2 vTextureCoord;\n" +
            "const float barrelPower = 0.5;   \n" +
            "const int num_iter = " + 5 + ";  \n" +
            "const float reci_num_iter_f = 1.0 / float(num_iter); \n" +
            "  \n" +
            "vec2 barrelDistortion(vec2 coord, float amt) \n" +
            "{  \n" +
            "    vec2 cc = coord - 0.5;  \n" +
            "    float dist = dot(cc, cc);  \n" +
            "    return coord + cc * dist * amt;   \n" +
            "}  \n" +
            "  \n" +
            "float sat( float t )  \n" +
            "{  \n" +
            "    return clamp( t, 0.0, 1.0 );  \n" +
            "}  \n" +
            "  \n" +
            "float linterp( float t ) {  \n" +
            "    return sat( 1.0 - abs( 2.0*t - 1.0 ) );  \n" +
            "}  \n" +
            "  \n" +
            "float remap( float t, float a, float b )   \n" +
            "{  \n" +
            "    return sat( (t - a) / (b - a) );  \n" +
            "}  \n" +
            "  \n" +
            "vec3 spectrum_offset( float t )   \n" +
            "{  \n" +
            "    vec3 ret;  \n" +
            "    float lo = step(t,0.5);  \n" +
            "    float hi = 1.0-lo;  \n" +
            "    float w = linterp( remap( t, 1.0/6.0, 5.0/6.0 ) );  \n" +
            "    ret = vec3(lo,1.0,hi) * vec3(1.0-w, w, 1.0-w);  \n" +
            "  \n" +
            "    return pow( ret, vec3(1.0/2.2) );  \n" +
            "}  \n" +
            "  \n" +
            "void main()  \n" +
            "{     \n" +
            "    vec2 uv=(gl_FragCoord.xy/vTextureCoord.xy);  \n" +
            "  \n" +
            "    vec3 sumcol = vec3(0.0);  \n" +
            "    vec3 sumw = vec3(0.0);    \n" +
            "    for ( int i=0; i<num_iter;++i )  \n" +
            "    {  \n" +
            "        float t = float(i) * reci_num_iter_f;  \n" +
            "        vec3 w = spectrum_offset( t );\n" +
            "        sumw += w;\n" +
            "        sumcol += w * texture2D( sTexture, barrelDistortion(vTextureCoord, barrelPower*t ) ).rgb;   \n"
            +
            "    }\n" +
            "    gl_FragColor = vec4(sumcol.rgb / sumw, 1.0);  \n" +
            "}  ";
//    private int mTexSizeHandle;
    public CameoMovieFilter(){
        super(VERTEX_SHADER,FRAGMENT_SHADER);
    }

//    @Override
//    public void initShader() {
//        super.initShader();
//        mTexSizeHandle = GLES20.glGetUniformLocation(mProgId,"TexSize");
//    }
//
//    @Override
//    protected void onPreDraw(PhotoMovie photoMovie, int elapsedTime, FboTexture inputTexture) {
//        super.onPreDraw(photoMovie, elapsedTime,inputTexture);
//        GLES20.glUniform2fv(mTexSizeHandle,1,new float[]{inputTexture.getTextureWidth(),inputTexture.getTextureHeight()},0);
//    }
}
