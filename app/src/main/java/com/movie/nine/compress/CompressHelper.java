package com.movie.nine.compress;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;

public class CompressHelper {
    private static volatile CompressHelper INSTANCE;

    private Context context;

    private float maxWidth = 720.0f;

    private float maxHeight = 960.0f;

    private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;

    private Bitmap.Config bitmapConfig = Bitmap.Config.ARGB_8888;

    private int quality = 95;

    private String destinationDirectoryPath;

    private String fileNamePrefix;

    private String fileName;

    public static CompressHelper getDefault(Context context) {
        if (INSTANCE == null) {
            synchronized (CompressHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CompressHelper(context);
                }
            }
        }
        return INSTANCE;
    }


    private CompressHelper(Context context) {
        this.context = context;
        destinationDirectoryPath = context.getCacheDir().getPath() + File.pathSeparator + FileUtil.FILES_PATH;
    }

    public File compressToFile(File file) {
        return BitmapUtil.compressImage(context, Uri.fromFile(file), maxWidth, maxHeight,
                compressFormat, bitmapConfig, quality, destinationDirectoryPath,
                fileNamePrefix, fileName);
    }

    public Bitmap compressToBitmap(File file) {
        return BitmapUtil.getScaledBitmap(context, Uri.fromFile(file), maxWidth, maxHeight, bitmapConfig);
    }

    public static class Builder {
        private CompressHelper mCompressHelper;

        public Builder(Context context) {
            mCompressHelper = new CompressHelper(context);
        }

        public Builder setMaxWidth(float maxWidth) {
            mCompressHelper.maxWidth = maxWidth;
            return this;
        }

        public Builder setMaxHeight(float maxHeight) {
            mCompressHelper.maxHeight = maxHeight;
            return this;
        }

        public Builder setCompressFormat(Bitmap.CompressFormat compressFormat) {
            mCompressHelper.compressFormat = compressFormat;
            return this;
        }

        public Builder setBitmapConfig(Bitmap.Config bitmapConfig) {
            mCompressHelper.bitmapConfig = bitmapConfig;
            return this;
        }

        public Builder setQuality(int quality) {
            mCompressHelper.quality = quality;
            return this;
        }

        public Builder setDestinationDirectoryPath(String destinationDirectoryPath) {
            mCompressHelper.destinationDirectoryPath = destinationDirectoryPath;
            return this;
        }

        public Builder setFileNamePrefix(String prefix) {
            mCompressHelper.fileNamePrefix = prefix;
            return this;
        }

        public Builder setFileName(String fileName) {
            mCompressHelper.fileName = fileName;
            return this;
        }

        public CompressHelper build() {
            return mCompressHelper;
        }
    }
}
