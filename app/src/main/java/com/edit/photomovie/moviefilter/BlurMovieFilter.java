package com.edit.photomovie.moviefilter;

public class BlurMovieFilter extends BaseMovieFilter {
    protected static final String FRAGMENT_SHADER =
            "#extension GL_OES_EGL_image_external : require\n" +
                    "precision mediump float;\n" +
                    "uniform sampler2D inputImageTexture;  \n" +
                    "  \n" +
                    "varying vec2 textureCoordinate;\n" +
                    "const float barrelPower = 0.5;   \n" +
                    "const int num_iter = "+ 5 +";  \n" +
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
                    "    vec2 uv=(gl_FragCoord.xy/textureCoordinate.xy);  \n" +
                    "  \n" +
                    "    vec3 sumcol = vec3(0.0);  \n" +
                    "    vec3 sumw = vec3(0.0);    \n" +
                    "    for ( int i=0; i<num_iter;++i )  \n" +
                    "    {  \n" +
                    "        float t = float(i) * reci_num_iter_f;  \n" +
                    "        vec3 w = spectrum_offset( t );\n" +
                    "        sumw += w;\n" +
                    "        sumcol += w * texture2D( inputImageTexture, barrelDistortion(textureCoordinate, barrelPower*t ) ).rgb;   \n" +
                    "    }\n" +
                    "    gl_FragColor = vec4(sumcol.rgb / sumw, 1.0);  \n" +
                    "}  ";
    public BlurMovieFilter(){
        super(VERTEX_SHADER,FRAGMENT_SHADER);
    }
}
