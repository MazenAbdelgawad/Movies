package mazen_mohamed.movies;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import mazen_mohamed.movies.Parser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsFragment extends Fragment {

    @BindView(R.id.titleDetails) TextView titel;
    @BindView(R.id.DateDatils) TextView releasDate;
    @BindView(R.id.textView2) TextView overView;
    @BindView(R.id.ratingBar) RatingBar rate;
    @BindView(R.id.imageDetails) ImageView background;
    @BindView(R.id.button) Button review;
    @BindView(R.id.recyclerView) RecyclerView recyclerView1;
    @BindView(R.id.imageButtonPlay) ImageButton fab;
    @BindView(R.id.favorite_button)ImageButton imageButton;

    public static final String ARG_ITEM_ID = "item_id";
    private static Card model;
    private  String video_id;
    protected List<Review> reviewData;
    private reviewAdapter reviewAdapter;
    public LinearLayoutManager mLayoutManager;
    /* public static SharedPreferences settings;
     public static int index=0;*/
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //settings = getActivity().getSharedPreferences("favoritData", MODE_PRIVATE);
        reviewData=new ArrayList<>();

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            model = (Card) getArguments().getSerializable(ARG_ITEM_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.details_fragment, container, false);
        ButterKnife.bind(this, view);

        recyclerView1.setHasFixedSize(true);
        reviewAdapter = new reviewAdapter(reviewData);
        recyclerView1.setAdapter(reviewAdapter);

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestReviews(model.getID(),1);

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestReviews(model.getID(),0);
                watchYoutubeVideo(video_id);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                /*SharedPreferences.Editor myeditor = settings.edit();
                myeditor.putString("movieID"+index, model.getID());
                index++;
                myeditor.commit();*/

                new FavoriteStore(getActivity()).SaveMovieID(model.getID());
            }
        });

        titel.setText(model.getOriginal_title());
        releasDate.setText(model.getRelease_date());
        overView.setText(model.getOverview());
        rate.setRating(Float.parseFloat(model.getVote_average())/2);

        Picasso.with(getActivity()).load(model.getBackdrop_path()).into(background);
        return view;
    }

    /////////youtupe//////
    public void watchYoutubeVideo(String id){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            startActivity(intent);
        }
    }

    public void requestReviews(String id,int i) {
        String Url ="";
        if(i==0) {
            Url = "http://api.themoviedb.org/3/movie/" + id + "/videos?api_key=f78be50ecdcaa8f00e595d90e6281fc8";
        }
        else
        {
            Url = "http://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=f78be50ecdcaa8f00e595d90e6281fc8";
        }
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(Url);
        if (entry != null)
        {
            try                                   // fetch the data from cache
            {
                String data = new String(entry.data, "UTF-8");
                if(i==0)
                {
                    video_id=Parser.getVideoID(data);
                }
                clearDataSet();
                Iterator iterator = Parser.parseDataToReview(data).iterator();

                while (iterator.hasNext())
                {
                    Review rMovie = (Review)iterator.next();
                    reviewData.add(rMovie);
                    reviewAdapter.notifyItemInserted(reviewData.size() - 1);
                }

            } catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }

        }
        /////////////connection//////////
        StringRequest strReq = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d("response", response);
                clearDataSet();
                Iterator iterator = Parser.parseDataToReview(response).iterator();
                while (iterator.hasNext())
                {
                    Review rMovie = (Review)iterator.next();
                    reviewData.add(rMovie);
                    reviewAdapter.notifyItemInserted(reviewData.size() - 1);
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("response", error.toString());
            }
        });

        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(strReq);

    }

    private void clearDataSet()
    {
        if (reviewData != null){
            reviewData.clear();
            reviewAdapter.notifyDataSetChanged();
        }
    }

}