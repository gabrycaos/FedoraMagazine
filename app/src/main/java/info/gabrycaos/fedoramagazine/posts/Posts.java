package info.gabrycaos.fedoramagazine.posts;

import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import info.gabrycaos.fedoramagazine.utility.DataTransferInterface;
import info.gabrycaos.fedoramagazine.utility.WpApiParser;

/**
 * Created by gabrycaos on 23/10/16.
 */
public class Posts extends ArrayList<Post> {
    private static Posts ourInstance = new Posts();

    public static Posts getInstance() {
        return ourInstance;
    }

    private Posts() {
    }



    public void addFromJSONArray(JSONArray posts){
        for(int i=0; i<posts.length(); i++){
            try {
                JSONObject post = posts.getJSONObject(i);
                String title = post.getJSONObject("title").getString("rendered");
                String content = post.getJSONObject("content").getString("rendered");
                String url = post.getJSONObject("guid").getString("rendered");
                String idMedia = post.getString("featured_media");
                String datetime = post.getString("date");
                Post p = new Post(title, content, url, idMedia, datetime.substring(0, 10));
                this.add(p);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
