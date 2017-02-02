package info.gabrycaos.fedoramagazine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import org.json.JSONArray;

import info.gabrycaos.fedoramagazine.posts.Post;
import info.gabrycaos.fedoramagazine.posts.Posts;
import info.gabrycaos.fedoramagazine.utility.DataTransferInterface;
import info.gabrycaos.fedoramagazine.utility.WpApiParser;

public class PostDetail extends AppCompatActivity {

    private static final String TAG = "POSTDETAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        final int position = getIntent().getExtras().getInt("positionPost");
        final Post p = Posts.getInstance().get(position);
        String title = p.getTitle();
        String content = p.getContent();
        WebView contentPost = (WebView) findViewById(R.id.contentPost);
        final CollapsingToolbarLayout top = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        setTitle(Html.fromHtml(title));
        Typeface tf = Typeface.createFromAsset(getAssets(), "Cantarell-Regular-webfont.ttf");
//        contentPost.setTypeface(tf);
//        contentPost.setText(Html.fromHtml(content));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            contentPost.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            contentPost.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
//        contentPost.loadData(content,  "text/html; charset=UTF-8", null);

        content = "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" /><div id=\"textContainer\"><br>" + content + "<br></div>";
        // lets assume we have /assets/style.css file
        contentPost.loadDataWithBaseURL("file:///android_asset/", content, "text/html", "UTF-8", null);


        Log.d(TAG, title);
        Log.d(TAG, content);

        if (p.getImage() != null) {
            Drawable d = new BitmapDrawable(getResources(), p.getImage());
            top.setBackground(d);
        } else {
            WpApiParser.getFeaturedImage(p.getIdFeaturedImage(), new DataTransferInterface() {
                @Override
                public void onJSONArrayDataArrived(JSONArray data) {

                }

                @Override
                public void onImageArrived(String imageUrl) {

                }

                @Override
                public void onImageArrived(Bitmap image) {
                    Posts.getInstance().get(position).setImage(image);
                    Drawable d = new BitmapDrawable(image);
                    top.setBackground(d);
                }

                @Override
                public void onImageUrlArrived(String imageUrl) {

                }
            });
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                shareIntent.putExtra(Intent.EXTRA_TEXT, p.getUrl());
                startActivity(Intent.createChooser(shareIntent, "Share on:"));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
