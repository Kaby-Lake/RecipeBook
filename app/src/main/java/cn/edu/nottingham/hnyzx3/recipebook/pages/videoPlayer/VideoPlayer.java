package cn.edu.nottingham.hnyzx3.recipebook.pages.videoPlayer;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import cn.edu.nottingham.hnyzx3.recipebook.R;

public class VideoPlayer extends AppCompatActivity {

    private VideoView videoView;

    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.page_video_player);
        getSupportActionBar().hide();

        videoView = findViewById(R.id.videoView);
        mediaController = new MediaController(this);

        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        Uri uri = getIntent().getParcelableExtra("videoUri");

        videoView.setVideoURI(uri);
        videoView.requestFocus();

        videoView.start();

        this.setVideoViewOnRotation();
    }

    /**
     * get called when rotation happens
     * will apply the new landscape/portrait layout
     * and restore the viewModels to the new layout
     */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.setVideoViewOnRotation();
    }

    private void setVideoViewOnRotation() {
        boolean isLandscape = this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        ViewGroup.LayoutParams layoutParams = videoView.getLayoutParams();
        if (isLandscape) {
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        } else {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        videoView.setLayoutParams(layoutParams);
    }

    public void onBackClick() {
        finish();
    }

    @Override
    public void onBackPressed() {
        this.onBackClick();
    }
}