package com.edit.photomovie.timer;

 
public interface IMovieTimer {

    public void start();

    public void pause();

    public void setMovieListener(MovieListener movieListener);

    public int getCurrentPlayTime();

    void setLoop(boolean loop);

    public interface MovieListener {
        void onMovieUpdate(int elapsedTime);

        void onMovieStarted();

        void onMoviedPaused();

        void onMovieResumed();

        void onMovieEnd();
    }
}
