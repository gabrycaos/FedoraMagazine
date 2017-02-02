package info.gabrycaos.fedoramagazine.posts;

import android.graphics.Bitmap;
import android.text.Html;

/**
 * Created by gabrycaos on 23/10/16.
 */
public class Post {
    private String title;
    private String content;
    private Bitmap image;
    private String imageUrl;
    private String url;
    private String idFeaturedImage;
    private String datetime;

    public Post(String t, String c, String u, String id, String dt){
        this.title = t;
        this.content = c;
        this.url = u;
        this.idFeaturedImage = id;
        this.datetime = dt;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getIdFeaturedImage() {
        return idFeaturedImage;
    }

    public void setIdFeaturedImage(String idFeaturedImage) {
        this.idFeaturedImage = idFeaturedImage;
    }

    public String getDateTime(){
        return this.datetime;
    }
}
