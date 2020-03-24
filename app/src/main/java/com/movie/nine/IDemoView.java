package com.movie.nine;

import android.app.Activity;
import com.edit.photomovie.render.GLTextureView;
import com.movie.nine.widget.FilterItem;
import com.movie.nine.widget.TransferItem;

import java.util.List;

/**
 * Created by huangwei on 2018/9/9.
 */
public interface IDemoView {
    public GLTextureView getGLView();
    void setFilters(List<FilterItem> filters);
    Activity getActivity();

    void setTransfers(List<TransferItem> items);
}
