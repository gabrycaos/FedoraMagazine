package info.gabrycaos.fedoramagazine;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import info.gabrycaos.fedoramagazine.posts.Posts;
import info.gabrycaos.fedoramagazine.utility.DataTransferInterface;
import info.gabrycaos.fedoramagazine.utility.WpApiParser;

import static android.R.attr.data;
import static android.support.v7.widget.helper.ItemTouchHelper.DOWN;
import static info.gabrycaos.fedoramagazine.R.attr.layoutManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private FeedListItemAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private Posts list;
    private DataTransferInterface dti;
    private int page = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(MainActivity.this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        list = Posts.getInstance();
        final ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);
        dti = new DataTransferInterface() {
            @Override
            public void onJSONArrayDataArrived(JSONArray data) {
                int position = mLayoutManager.findLastCompletelyVisibleItemPosition();
                Posts.getInstance().addFromJSONArray(data);
                mAdapter = new FeedListItemAdapter(getApplicationContext());
//                progress.setVisibility(View.GONE);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                mLayoutManager.scrollToPosition(position-1); //avoid jump scroll to top after notifyDataSetChanged()
            }

            @Override
            public void onImageArrived(String image) {

            }

            @Override
            public void onImageArrived(Bitmap image) {

            }

            @Override
            public void onImageUrlArrived(String imageUrl) {

            }

        };
        WpApiParser.getNews(getApplicationContext(), dti, page);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //Tocheck if  recycler is on bottom
                if(!mRecyclerView.canScrollVertically(DOWN)){
                    Log.d(TAG, "CARICO");
                    page++;
                    WpApiParser.getNews(getApplicationContext(), dti, page);

                }
            }
        });


    }

    public void more(){
        page++;
        WpApiParser.getNews(getApplicationContext(), dti, page);
    }


}