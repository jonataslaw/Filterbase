package com.movie.nine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;
import com.edit.photomovie.PhotoMovie;
import com.edit.photomovie.PhotoMovieFactory;
import com.edit.photomovie.PhotoMoviePlayer;
import com.edit.photomovie.model.PhotoData;
import com.edit.photomovie.model.PhotoSource;
import com.edit.photomovie.model.SimplePhotoData;
import com.edit.photomovie.record.GLMovieRecorder;
import com.edit.photomovie.render.GLSurfaceMovieRenderer;
import com.edit.photomovie.render.GLTextureMovieRender;
import com.edit.photomovie.render.GLTextureView;
import com.edit.photomovie.timer.IMovieTimer;
import com.edit.photomovie.util.MLog;
import com.movie.nine.widget.FilterItem;
import com.movie.nine.widget.FilterType;
import com.movie.nine.widget.MovieFilterView;
import com.movie.nine.widget.MovieTransferView;
import com.movie.nine.widget.TransferItem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NinePresenter implements MovieFilterView.FilterCallback, IMovieTimer.MovieListener, MovieTransferView.TransferCallback {

    static final int ACTIVITY_2_REQUEST = 444;

    private IDemoView mDemoView;
    private PhotoMovie mPhotoMovie;
    private PhotoMoviePlayer mPhotoMoviePlayer;
    private GLSurfaceMovieRenderer mMovieRenderer;
    private Uri mMusicUri;
    private PhotoMovieFactory.PhotoMovieType mMovieType = PhotoMovieFactory.PhotoMovieType.THAW;

    public void attachView(IDemoView demoView) {
        mDemoView = demoView;
        initFilters();
        initTransfers();
        initMoviePlayer();
    }

    private void initTransfers() {
        List<TransferItem> items = new LinkedList<TransferItem>();
      //  items.add(new TransferItem(R.drawable.ic_movie_transfer, "LeftRight", PhotoMovieFactory.PhotoMovieType.HORIZONTAL_TRANS));
      //  items.add(new TransferItem(R.drawable.ic_movie_transfer, "UpDown", PhotoMovieFactory.PhotoMovieType.VERTICAL_TRANS));
        items.add(new TransferItem(R.drawable.ic_movie_transfer, "Thaw", PhotoMovieFactory.PhotoMovieType.THAW));
        items.add(new TransferItem(R.drawable.ic_movie_transfer, "Window", PhotoMovieFactory.PhotoMovieType.WINDOW));
        items.add(new TransferItem(R.drawable.ic_movie_transfer, "Gradient", PhotoMovieFactory.PhotoMovieType.GRADIENT));
        items.add(new TransferItem(R.drawable.ic_movie_transfer, "Tranlation", PhotoMovieFactory.PhotoMovieType.SCALE_TRANS));
        items.add(new TransferItem(R.drawable.ic_movie_transfer, "Scale", PhotoMovieFactory.PhotoMovieType.SCALE));
        mDemoView.setTransfers(items);
    }

    private void initFilters() {
        List<FilterItem> items = new LinkedList<FilterItem>();
        items.add(new FilterItem(R.drawable.none, "None", FilterType.NONE));
        items.add(new FilterItem(R.drawable.mono, "BlackWhite", FilterType.GRAY));
        items.add(new FilterItem(R.drawable.overlay, "Overlay", FilterType.OVERLAY));
        items.add(new FilterItem(R.drawable.blur, "Blur", FilterType.BLUR));
        items.add(new FilterItem(R.drawable.cross, "Cross", FilterType.CROSS));
        items.add(new FilterItem(R.drawable.snow, "Snow", FilterType.SNOW));
        items.add(new FilterItem(R.drawable.lut1, "Lut_1", FilterType.LUT1));
        items.add(new FilterItem(R.drawable.lut2, "Lut_2", FilterType.LUT2));
        items.add(new FilterItem(R.drawable.lut3, "Lut_3", FilterType.LUT3));
        items.add(new FilterItem(R.drawable.lut4, "Lut_4", FilterType.LUT4));
        items.add(new FilterItem(R.drawable.lut5, "Lut_5", FilterType.LUT5));
        mDemoView.setFilters(items);
    }

    private void initMoviePlayer() {
        final GLTextureView glTextureView = mDemoView.getGLView();


        mMovieRenderer = new GLTextureMovieRender(glTextureView);
        addWaterMark();
        mPhotoMoviePlayer = new PhotoMoviePlayer(mDemoView.getActivity().getApplicationContext());
        mPhotoMoviePlayer.setMovieRenderer(mMovieRenderer);
        mPhotoMoviePlayer.setMovieListener(this);
        mPhotoMoviePlayer.setLoop(true);
        mPhotoMoviePlayer.setOnPreparedListener(new PhotoMoviePlayer.OnPreparedListener() {
            @Override
            public void onPreparing(PhotoMoviePlayer moviePlayer, float progress) {
            }

            @Override
            public void onPrepared(PhotoMoviePlayer moviePlayer, int prepared, int total) {
                mDemoView.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPhotoMoviePlayer.start();
                    }
                });

            }

            @Override
            public void onError(PhotoMoviePlayer moviePlayer) {
                MLog.i("onPrepare", "onPrepare error");
            }
        });
    }

    private void addWaterMark(){
//        Bitmap waterMark = BitmapFactory.decodeResource(mDemoView.getActivity().getResources(),R.drawable.watermark);
//        DisplayMetrics displayMetrics = mDemoView.getActivity().getResources().getDisplayMetrics();
//        mMovieRenderer.setWaterMark(waterMark,new RectF(displayMetrics.widthPixels-waterMark.getWidth(),0,displayMetrics.widthPixels,waterMark.getHeight()),0.5f);
//
//        mMovieRenderer.setWaterMark("Watermark",40, Color.argb(100,255,0,0),100,100);
    }

    private void startPlay(PhotoSource photoSource) {
        mPhotoMovie = PhotoMovieFactory.generatePhotoMovie(photoSource, mMovieType);
        mPhotoMoviePlayer.setDataSource(mPhotoMovie);
        mPhotoMoviePlayer.prepare();
    }

    public void detachView() {
        mDemoView = null;
    }

    @Override
    public void onFilterSelect(FilterItem item) {
        mMovieRenderer.setMovieFilter(item.initFilter());
    }

    @Override
    public void onMovieUpdate(int elapsedTime) {

    }

    @Override
    public void onMovieStarted() {

    }

    @Override
    public void onMoviedPaused() {

    }

    @Override
    public void onMovieResumed() {

    }

    @Override
    public void onMovieEnd() {

    }

    @Override
    public void onTransferSelect(TransferItem item) {
        mMovieType = item.type;
        mPhotoMoviePlayer.stop();
        mPhotoMovie = PhotoMovieFactory.generatePhotoMovie(mPhotoMovie.getPhotoSource(), mMovieType);
        mPhotoMoviePlayer.setDataSource(mPhotoMovie);
        if (mMusicUri != null) {
            mPhotoMoviePlayer.setMusic(mDemoView.getActivity(), mMusicUri);
        }
        mPhotoMoviePlayer.setOnPreparedListener(new PhotoMoviePlayer.OnPreparedListener() {
            @Override
            public void onPreparing(PhotoMoviePlayer moviePlayer, float progress) {
            }

            @Override
            public void onPrepared(PhotoMoviePlayer moviePlayer, int prepared, int total) {
                mDemoView.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPhotoMoviePlayer.start();
                    }
                });
            }

            @Override
            public void onError(PhotoMoviePlayer moviePlayer) {
                MLog.i("onPrepare", "onPrepare error");
            }
        });
        mPhotoMoviePlayer.prepare();
    }

    public void setMusic(Uri uri) {
        mMusicUri = uri;
        mPhotoMoviePlayer.setMusic(mDemoView.getActivity(), uri);
    }

    public void saveVideo() {
        mPhotoMoviePlayer.pause();
        final ProgressDialog dialog = new ProgressDialog(mDemoView.getActivity());
        dialog.setMessage("saving video...");
        dialog.setMax(100);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);
        dialog.show();
        final long startRecodTime = System.currentTimeMillis();
        final GLMovieRecorder recorder = new GLMovieRecorder(mDemoView.getActivity());
        final File file = initVideoFile();
      //  GLTextureView glTextureView = mDemoView.getGLView();
        int bitrate = 3000000;
      //  recorder.configOutput(glTextureView.getWidth(), glTextureView.getHeight(), bitrate, 30, 1, file.getAbsolutePath());
        recorder.configOutput(720, 1280, bitrate, 30, 1, file.getAbsolutePath());
        //生成一个全新的MovieRender，不然与现有的GL环境不一致，相互干扰容易出问题
        PhotoMovie newPhotoMovie = PhotoMovieFactory.generatePhotoMovie(mPhotoMovie.getPhotoSource(), mMovieType);
        GLSurfaceMovieRenderer newMovieRenderer = new GLSurfaceMovieRenderer(mMovieRenderer);
        newMovieRenderer.setPhotoMovie(newPhotoMovie);
        String audioPath = null;
        if(mMusicUri!=null) {
            audioPath = UriUtil.getPath(mDemoView.getActivity(), mMusicUri);
        }
        if (!TextUtils.isEmpty(audioPath)) {
            if (Build.VERSION.SDK_INT < 18) {
                Toast.makeText(mDemoView.getActivity().getApplicationContext(), "Mix audio needs api18!", Toast.LENGTH_LONG).show();
            } else {
                recorder.setMusic(audioPath);
            }
        }
        recorder.setDataSource(newMovieRenderer);
        recorder.startRecord(new GLMovieRecorder.OnRecordListener() {
            @Override
            public void onRecordFinish(boolean success) {
                File outputFile = file;
                long recordEndTime = System.currentTimeMillis();
                MLog.i("Record", "record:" + (recordEndTime - startRecodTime));
                dialog.dismiss();
                if (success) {
                    Toast.makeText(mDemoView.getActivity().getApplicationContext(), "Video save to path:" + outputFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                    
                    mDemoView.getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

//
//                    Intent intent = new Intent();
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.setAction(Intent.ACTION_VIEW);
//                    String type = "video/*";
//                    intent.setDataAndType(Uri.fromFile(outputFile), type);
//                    mDemoView.getActivity().startActivity(intent);

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("resultado",outputFile.getAbsolutePath());
                    mDemoView.getActivity().setResult( mDemoView.getActivity().RESULT_OK,returnIntent);
                    mDemoView.getActivity().finish();


                } else {
                    Toast.makeText(mDemoView.getActivity().getApplicationContext(), "record error!", Toast.LENGTH_LONG).show();
                }
                if(recorder.getAudioRecordException()!=null){
                    Toast.makeText(mDemoView.getActivity().getApplicationContext(), "record audio failed:"+recorder.getAudioRecordException().toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onRecordProgress(int recordedDuration, int totalDuration) {
                dialog.setProgress((int) (recordedDuration / (float) totalDuration * 100));
            }
        });
    }

    private File initVideoFile() {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (!dir.exists()) {
            dir = mDemoView.getActivity().getCacheDir();
        }
        return new File(dir, String.format("poprize_9ine_%s.mp4",
                new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(System.currentTimeMillis())));
    }

    public void onPause() {
        mPhotoMoviePlayer.pause();
    }

    public void onResume() {
        mPhotoMoviePlayer.start();
    }

    public void onPhotoPick(ArrayList<String> photos) {
        List<PhotoData> photoDataList = new ArrayList<PhotoData>(photos.size());
        for (String path : photos) {
            PhotoData photoData = new SimplePhotoData(mDemoView.getActivity(), path, PhotoData.STATE_LOCAL);
            photoDataList.add(photoData);
        }
        PhotoSource photoSource = new PhotoSource(photoDataList);
        if (mPhotoMoviePlayer == null) {
            startPlay(photoSource);
        } else {
            mPhotoMoviePlayer.stop();
            mPhotoMovie = PhotoMovieFactory.generatePhotoMovie(photoSource, PhotoMovieFactory.PhotoMovieType.THAW);
            mPhotoMoviePlayer.setDataSource(mPhotoMovie);
            if (mMusicUri != null) {
                mPhotoMoviePlayer.setMusic(mDemoView.getActivity(), mMusicUri);
            }
            mPhotoMoviePlayer.setOnPreparedListener(new PhotoMoviePlayer.OnPreparedListener() {
                @Override
                public void onPreparing(PhotoMoviePlayer moviePlayer, float progress) {
                }

                @Override
                public void onPrepared(PhotoMoviePlayer moviePlayer, int prepared, int total) {
                    mDemoView.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPhotoMoviePlayer.start();
                        }
                    });
                }

                @Override
                public void onError(PhotoMoviePlayer moviePlayer) {
                    MLog.i("onPrepare", "onPrepare error");
                }
            });
            mPhotoMoviePlayer.prepare();
        }
    }
}
