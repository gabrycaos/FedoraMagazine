package info.gabrycaos.fedoramagazine.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;

import cz.msebera.android.httpclient.Header;


/**
 * Created by gabrycaos on 02/10/16.
 */
public class WpApiParser {
    private static String WPURL = "http://fedoramagazine.org/wp-json/wp/v2/posts?page=";
    private static final String TAG = "WPAPIPARSER";
    private static String URLDATAIMAGEROOT = "https://fedoramagazine.org/wp-json/wp/v2/media/";
    private static String UPLOADFOLDER = "https://fedoramagazine.org/wp-content/uploads/";

    public static void getNews(final Context context, final DataTransferInterface dtf, int page) {

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.addHeader("Accept", "text/json");
        client.get(WPURL+page, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
//                Log.d(TAG, response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Pull out the first event on the public timeline
//               Log.d(TAG, response.toString());
                dtf.onJSONArrayDataArrived(response);


            }

        });
    }

    public static void getFeaturedImage(String idImage, final DataTransferInterface dti){
        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        String dataImageUrl = URLDATAIMAGEROOT+idImage;
        dti.onImageUrlArrived(dataImageUrl);
        client.get(dataImageUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
//                Log.d(TAG, response.toString());
                try {
                    JSONObject json = response.getJSONObject("media_details");
                    String file = json.getString("file");
                    String imageUrl = UPLOADFOLDER+file;
                    dti.onImageArrived(imageUrl);
                    AsyncHttpClient newClient = new AsyncHttpClient(true, 80, 443);
                    newClient.get(imageUrl, new AsyncHttpResponseHandler() {


                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                            // called when response HTTP status is "200 OK"
                            ByteArrayInputStream bytes = new ByteArrayInputStream(response);
                            BitmapDrawable bmd = new BitmapDrawable(bytes);
                            Bitmap bmp = bmd.getBitmap();
                            dti.onImageArrived(bmp);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                            Log.d(TAG, "errore");
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Pull out the first event on the public timeline
//               Log.d(TAG, response.toString());



            }

        });
    }



}
