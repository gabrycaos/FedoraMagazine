package info.gabrycaos.fedoramagazine;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;

import java.net.URI;

import info.gabrycaos.fedoramagazine.posts.Posts;
import info.gabrycaos.fedoramagazine.utility.DataTransferInterface;
import info.gabrycaos.fedoramagazine.utility.WpApiParser;

/**
 * Created by gabrycaos on 11/09/16.
 */
public class FeedListItemAdapter extends RecyclerView.Adapter<FeedListItemAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    private Posts posts;
    private Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView datetime;
        ImageView thumb;
        LinearLayout container;
        String imageId;

        public ViewHolder(View v) {
            super(v);
            this.title = (TextView) v.findViewById(R.id.temp);
            this.datetime = (TextView) v.findViewById(R.id.datetime);
            this.thumb = (ImageView) v.findViewById(R.id.imageViewThumb);
            this.container = (LinearLayout) v.findViewById(R.id.container);
        }

        public void setFeaturedImage(final Context c) {
            Bitmap b = null;

            WpApiParser.getFeaturedImage(this.imageId, new DataTransferInterface() {
                @Override
                public void onJSONArrayDataArrived(JSONArray data) {

                }

                @Override
                public void onImageArrived(String imageUrl) {
                    Log.d(TAG, "IMAGE");
                   // thumb.setImageBitmap(image);
                    Log.d(TAG, "URL");
                    Log.d(TAG, imageUrl);
                    Glide
                        .with(c)
                        .load(imageUrl)
                        .into(thumb);

                }

                @Override
                public void onImageArrived(Bitmap image) {

                }

                @Override
                public void onImageUrlArrived(String imageUrl) {
//                            posts.get(position).setImage(image);

                }
            });

        }
    }


    public FeedListItemAdapter(Context c) {
        this.context = c;
        posts = Posts.getInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;

        v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.feed_list_item, viewGroup, false);

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final String idImage = posts.get(position).getIdFeaturedImage();
        final ViewHolder holder = (ViewHolder) viewHolder;
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, PostDetail.class);
                i.putExtra("positionPost", position);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
        holder.title.setText(Html.fromHtml(posts.get(position).getTitle()));
        holder.datetime.setText(posts.get(position).getDateTime());
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "Cantarell-Bold-webfont.ttf");
        holder.title.setTypeface(tf);
        if (posts.get(position).getImage() == null) {
            holder.imageId = idImage;
            holder.setFeaturedImage(context);
//            WpApiParser.getFeaturedImage(idImage, new DataTransferInterface() {
//                @Override
//                public void onJSONArrayDataArrived(JSONArray data) {
//
//                }
//
//                @Override
//                public void onImageArrived(Bitmap image) {
//
//                }
//
//                @Override
//                public void onImageUrlArrived(String imageUrl) {
////                            posts.get(position).setImage(image);
//                    holder.imageUrl = imageUrl;
//                    holder.setFeaturedImage(context);
//                }
//            });
        } else {
            holder.thumb.setImageBitmap(posts.get(position).getImage());
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

}
