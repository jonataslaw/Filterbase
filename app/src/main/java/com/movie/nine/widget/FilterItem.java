package com.movie.nine.widget;

import com.edit.photomovie.moviefilter.BlurMovieFilter;
import com.edit.photomovie.moviefilter.CrossMovieFilter;
import com.edit.photomovie.moviefilter.GrayMovieFilter;
import com.edit.photomovie.moviefilter.IMovieFilter;
import com.edit.photomovie.moviefilter.LutMovieFilter;
import com.edit.photomovie.moviefilter.OverlayMovieFilter;
import com.edit.photomovie.moviefilter.SnowMovieFilter;


public class FilterItem {

    public FilterItem(int imgRes, String name, FilterType type) {
        this.imgRes = imgRes;
        this.name = name;
        this.type = type;
    }

    public int imgRes;
    public String name;
    public FilterType type;

    public IMovieFilter initFilter() {
        switch (type) {
           case GRAY:
                return new GrayMovieFilter();
           case SNOW:
                return new SnowMovieFilter();
           case OVERLAY:
               return new OverlayMovieFilter();
           case BLUR:
               return new BlurMovieFilter();
           case CROSS:
               return new CrossMovieFilter();
            case LUT1:
                return new LutMovieFilter(LutMovieFilter.LutType.A);
            case LUT2:
                return new LutMovieFilter(LutMovieFilter.LutType.B);
            case LUT3:
                return new LutMovieFilter(LutMovieFilter.LutType.C);
            case LUT4:
                return new LutMovieFilter(LutMovieFilter.LutType.D);
            case LUT5:
                return new LutMovieFilter(LutMovieFilter.LutType.E);
            case NONE:
            default:
                return null;
        }
    }
}
