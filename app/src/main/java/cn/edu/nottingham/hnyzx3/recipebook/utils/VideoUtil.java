package cn.edu.nottingham.hnyzx3.recipebook.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

public class VideoUtil {

    public static Bitmap getThumbVideo(Context context, Uri videoUri) {
        Bitmap bitmap;
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(context, videoUri);
        bitmap = mediaMetadataRetriever.getFrameAtTime(1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        mediaMetadataRetriever.release();
        return bitmap;
    }
}
