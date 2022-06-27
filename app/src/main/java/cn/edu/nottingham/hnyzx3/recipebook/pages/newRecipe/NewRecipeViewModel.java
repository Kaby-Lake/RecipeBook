package cn.edu.nottingham.hnyzx3.recipebook.pages.newRecipe;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.google.android.material.textfield.TextInputEditText;

import cn.edu.nottingham.hnyzx3.recipebook.utils.VideoUtil;

public class NewRecipeViewModel extends ViewModel {

    public NewRecipeViewModel(Activity activity) {
    }

    public ObservableField<String> title = new ObservableField<>(null);
    public ObservableField<String> instruction = new ObservableField<>(null);
    public ObservableField<String> ingredients = new ObservableField<>(null);
    public ObservableField<Double> rating = new ObservableField<>(null);
    public ObservableField<Uri> videoUri = new ObservableField<>(null);
    public ObservableField<Bitmap> videoPreview = new ObservableField<>(null);


    // BindingAdapter for TextInputEditText to set the TextInputEditText with double
    @BindingAdapter("android:text")
    public static void setRating(TextInputEditText view, Double rating) {
        if (rating != null) {
            view.setText(String.valueOf(rating));
        } else {
            view.setText("");
        }
    }

    // BindingAdapter for ImageView to set the ImageView with Bitmap
    @BindingAdapter("android:src")
    public static void setVideoUrl(ImageView view, Bitmap bitmap) {
        view.setImageBitmap(bitmap);
    }

    // set the video preview and the video uri
    public void onVideoSelect(Context context, Uri uri) {
        try {
            Bitmap preview = VideoUtil.getThumbVideo(context, uri);
            videoPreview.set(preview);
            videoUri.set(uri);
        } catch (Exception e) {
            videoPreview.set(null);
            videoUri.set(null);
        }
    }

}
