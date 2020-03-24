package com.edit.photomovie.moviefilter;

public class OverlayMovieFilter extends BaseMovieFilter {
    protected static final String FRAGMENT_SHADER =
            "#extension GL_OES_EGL_image_external : require\n"
                    + "precision mediump float;\n"
                    + "uniform sampler2D inputImageTexture;\n"
                    + " float contrast;\n"
                    + " float brightness;\n"
                    + "varying vec2 textureCoordinate;\n" + "void main() {\n"
                    + "  vec4 color = texture2D(inputImageTexture, textureCoordinate);\n"
                    + "  vec3 ncolor = vec3(0.0, 0.0, 0.0);\n" + "  float value;\n"
                    + "  ncolor.b = color.b * 0.7 + 0.25;\n"
                    + "  if (color.r < 0.5) {\n" + "    value = color.r;\n"
                    + "  } else {\n" + "    value = 1.0 - color.r;\n" + "  }\n"
                    + "  float red = 4.0 * value * value * value;\n"
                    + "  if (color.r < 0.5) {\n" + "    ncolor.r = red;\n"
                    + "  } else {\n" + "    ncolor.r = 1.0 - red;\n" + "  }\n"
                    + "  if (color.g < 0.5) {\n" + "    value = color.g;\n"
                    + "  } else {\n" + "    value = 1.0 - color.g;\n" + "  }\n"
                    + "  float green = 2.0 * value * value;\n"
                    + "  if (color.g < 0.5) {\n" + "    ncolor.g = green;\n"
                    + "  } else {\n" + "    ncolor.g = 1.0 - green;\n" + "  }\n"
                    + "  contrast =" + 0.5f + ";\n"
                    + "  brightness =" + 0.2f + ";\n"
                    + "  ncolor = (ncolor) * contrast;\n"

                    + "  ncolor = ncolor + vec3(brightness,brightness,brightness);\n"
                    + "  gl_FragColor = vec4(ncolor.rgb, color.a);\n" + "}\n";
    public OverlayMovieFilter(){
        super(VERTEX_SHADER,FRAGMENT_SHADER);
    }
}
