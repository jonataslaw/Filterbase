package com.movie.nine.widget;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.movie.nine.R;

public class MovieBottomView extends ConstraintLayout implements View.OnClickListener {

    private MovieBottomCallback mCallback;

    public MovieBottomView(Context context) {
        super(context);
    }

    public MovieBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MovieBottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViewById(R.id.movie_next).setOnClickListener(this);
        findViewById(R.id.movie_filter).setOnClickListener(this);
        findViewById(R.id.movie_filter_txt).setOnClickListener(this);
        findViewById(R.id.movie_transfer).setOnClickListener(this);
        findViewById(R.id.movie_transfer_txt).setOnClickListener(this);
        findViewById(R.id.movie_music).setOnClickListener(this);
        findViewById(R.id.movie_music_txt).setOnClickListener(this);
    }

    public void setCallback(MovieBottomCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.movie_filter || id == R.id.movie_filter_txt) {
            if (mCallback != null) {
                mCallback.onFilterClick();
            }
        } else if (id == R.id.movie_transfer || id == R.id.movie_transfer_txt) {
            if (mCallback != null) {
                mCallback.onTransferClick();
            }
        } else if (id == R.id.movie_music || id == R.id.movie_music_txt) {
            if (mCallback != null) {
                mCallback.onMusicClick();
            }
        } else if (id == R.id.movie_next) {
            if (mCallback != null) {
                mCallback.onNextClick();
            }
        }
    }

    public static interface MovieBottomCallback{
        void onNextClick();
        void onMusicClick();
        void onTransferClick();
        void onFilterClick();
    }
}
