package info.gabrycaos.fedoramagazine.utility;

import android.graphics.Bitmap;

import org.json.JSONArray;

/**
 * Created by gabrycaos on 15/10/16.
 */
public interface DataTransferInterface {

    void onJSONArrayDataArrived(JSONArray data);
    void onImageArrived(String imageUrl);
    void onImageArrived(Bitmap image);
    void onImageUrlArrived(String jsonImageUrl);

}
