package com.edit.photomovie.segment.strategy;

import com.edit.photomovie.PhotoMovie;
import com.edit.photomovie.model.PhotoData;
import com.edit.photomovie.segment.MovieSegment;

import java.util.List;

/**
 * Created by Administrator on 2015/6/12.
 */
public class NotRetryStrategy implements RetryStrategy {
    @Override
    public List<PhotoData> getAvailableData(PhotoMovie photoMovie, MovieSegment movieSegment) {
        return movieSegment==null?null:movieSegment.getAllocatedPhotos();
    }
}
