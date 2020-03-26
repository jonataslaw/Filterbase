package com.movie.nine;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import com.edit.photomovie.render.GLTextureView;
import com.edit.photomovie.util.AppResources;
import com.movie.nine.widget.FilterItem;
import com.movie.nine.widget.MovieBottomView;
import com.movie.nine.widget.MovieFilterView;
import com.movie.nine.widget.MovieTransferView;
import com.movie.nine.widget.TransferItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.opensooq.supernova.gligar.GligarPicker;

/**
 * Created by huangwei on 2018/9/9.
 */
public class NineActivity extends AppCompatActivity implements IDemoView, MovieBottomView.MovieBottomCallback {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    private static final int REQUEST_MUSIC = 234;

    private static final int PICKER_REQUEST_CODE = 777;
    private NinePresenter mNinePresenter = new NinePresenter();
    private GLTextureView mGLTextureView;
    private MovieFilterView mFilterView;
    private MovieTransferView mTransferView;
    private MovieBottomView mBottomView;
    private View mSelectView;
    private List<FilterItem> mFilters;
    private List<TransferItem> mTransfers;
    private View mFloatAddView;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppResources.getInstance().init(getResources());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        mGLTextureView = findViewById(R.id.gl_texture);
        mBottomView = findViewById(R.id.movie_bottom_layout);
        mSelectView = findViewById(R.id.movie_add);
        mFloatAddView = findViewById(R.id.movie_add_float);
        mNinePresenter.attachView(this);
        mBottomView.setCallback(this);
        verifyStoragePermissions(this);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPhotos();
            }
        };
        mSelectView.setOnClickListener(onClickListener);
        mFloatAddView.setOnClickListener(onClickListener);
    }

    private void requestPhotos() {
        new GligarPicker()
                .limit(9)
                .requestCode(PICKER_REQUEST_CODE).withActivity(this).show();
    }

    @Override
    public GLTextureView getGLView() {
        return mGLTextureView;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNinePresenter.detachView();
    }

    private boolean checkInit() {
        if (mSelectView.getVisibility() == View.VISIBLE) {
            Toast.makeText(this, "please select photos", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    @Override
    public void onNextClick() {
        if (checkInit()) {
            return;
        }
        mNinePresenter.saveVideo();
    }

    @Override
    public void onMusicClick() {
        if (checkInit()) {
            return;
        }
        Intent i = new Intent();
        i.setType("audio/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, REQUEST_MUSIC);
    }

    @Override
    public void onTransferClick() {
        if (checkInit()) {
            return;
        }
        if (mTransferView == null) {
            ViewStub stub = findViewById(R.id.movie_menu_transfer_stub);
            mTransferView = (MovieTransferView) stub.inflate();
            mTransferView.setVisibility(View.GONE);
            mTransferView.setItemList(mTransfers);
            mTransferView.setTransferCallback(mNinePresenter);
        }
        mBottomView.setVisibility(View.GONE);
        mTransferView.show();
    }

    @Override
    public void onFilterClick() {
        if (checkInit()) {
            return;
        }
        if (mFilterView == null) {
            ViewStub stub = findViewById(R.id.movie_menu_filter_stub);
            mFilterView = (MovieFilterView) stub.inflate();
            mFilterView.setVisibility(View.GONE);
            mFilterView.setItemList(mFilters);
            mFilterView.setFilterCallback(mNinePresenter);
        }
        mBottomView.setVisibility(View.GONE);
        mFilterView.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_MUSIC) {
            Uri uri = data.getData();
            mNinePresenter.setMusic(uri);
        } else if (resultCode == RESULT_OK && requestCode == PICKER_REQUEST_CODE) {
            if (data != null) {
                String pathsList[]= data.getExtras().getStringArray(GligarPicker.IMAGES_RESULT);
                ArrayList<String> photos =  new ArrayList<String>(Arrays.asList(pathsList));
                final File externalFilesDirectory =
                        NineActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                final ExifDataCopier exifDataCopier = new ExifDataCopier();
                final ImageResizer imageResizer = new ImageResizer(externalFilesDirectory, exifDataCopier);

                for(int i = 0; i < photos.size(); i++) {
                    photos.set(i, imageResizer.resizeImageIfNeeded( photos.get(i), 1200.0, 1200.0, 60));
                }

//               int i = photos.size();
//               int ban = 0;
//               if(i>3 &&(i<6)){
//                    ban = 8;
//               }else if(i>6){
//                    ban = 20;
//               } else {
//                    ban = 6;
//               }
                mNinePresenter.onPhotoPick(photos);
                mFloatAddView.setVisibility(View.VISIBLE);
                mSelectView.setVisibility(View.GONE);
            }
        }
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (mFilterView != null && mFilterView.getVisibility() == View.VISIBLE
                    && !checkInArea(mFilterView, ev)) {
                mFilterView.hide();
                mBottomView.setVisibility(View.VISIBLE);
                return true;
            } else if (mTransferView != null && mTransferView.getVisibility() == View.VISIBLE
                    && !checkInArea(mTransferView, ev)) {
                mTransferView.hide();
                mBottomView.setVisibility(View.VISIBLE);
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean checkInArea(View view, MotionEvent event) {
        int loc[] = new int[2];
        view.getLocationInWindow(loc);
        return event.getRawY() > loc[1];
    }

    @Override
    public void setFilters(List<FilterItem> filters) {
        mFilters = filters;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void setTransfers(List<TransferItem> items) {
        mTransfers = items;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mNinePresenter.onPause();
        mGLTextureView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNinePresenter.onResume();
        mGLTextureView.onResume();
    }
}
