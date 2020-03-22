package com.edit.photomovie.segment.strategy;

import com.edit.photomovie.PhotoMovie;
import com.edit.photomovie.model.PhotoData;
import com.edit.photomovie.segment.MovieSegment;

import java.util.List;

/**
 * Created by yellowcat on 2015/6/12.
 */
public interface RetryStrategy {
    List<PhotoData> getAvailableData(PhotoMovie photoMovie, MovieSegment movieSegment);
}
