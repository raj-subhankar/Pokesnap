package com.yellowsoft.subhankar.pokesnap;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by subhankar on 7/30/2016.
 */
public class UserProfileActivity extends AppCompatActivity {

    private ImageView ivUserProfilePhoto;
    private TextView tvUserName;
    private TextView tvTeam;
    private RecyclerView recyclerView;
    ArrayList<Post> posts = new ArrayList<Post>();
    private UserProfileAdapter mAdapter;

    private static final String TAG = NewFeedActivityFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        ivUserProfilePhoto = (ImageView) findViewById(R.id.ivUserProfilePhoto);
        tvUserName  = (TextView) findViewById(R.id.tvUserName);
        tvTeam = (TextView) findViewById(R.id.tvTeam);

        String userName = getIntent().getStringExtra("username");
        String team = getIntent().getStringExtra("team");



        tvUserName.setText(userName);
        tvTeam.setText(team);
        if(team.equals("Team Instinct")) {
            ivUserProfilePhoto.setImageResource(R.drawable.pokemongoinstinct);
        } else if(team.equals("Team Mystic")) {
            ivUserProfilePhoto.setImageResource(R.drawable.pokemongomystic);
        } else {
            ivUserProfilePhoto.setImageResource(R.drawable.pokemongovalor);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());

//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
//                customLoadMoreDataFromApi(page);
                int curSize = mAdapter.getItemCount();
                customLoadMoreDataFromApi(posts.get(curSize - 1).get_id().toString());
            }
        });

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<List<Post>> call = apiService.getUserPosts(userName);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if(!response.body().isEmpty()) {
                    for (int i = 0; i < response.body().size(); i++) {
                        Post post = response.body().get(i);
                        posts.add(post);
                    }
//                Post post = response.body().get(0).getPostBody();
                    mAdapter = new UserProfileAdapter(getApplicationContext(), posts);
                    recyclerView.setAdapter(mAdapter);
                }

            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });


        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(this.getResources().getColor(R.color.my_statusbar_color));
    }

    public void customLoadMoreDataFromApi(String lastid) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<List<Post>> call = apiService.getMoreUserPosts(lastid);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if (!response.body().isEmpty()) {

                    for (int i = 0; i < response.body().size(); i++) {
                        Post post = response.body().get(i);
                        posts.add(post);
                    }
//                Post post = response.body().get(0).getPostBody();
                    //mAdapter = new FeedAdapter(getContext(), posts);
                    //recyclerView.setAdapter(mAdapter);
//                    mAdapter.notifyItemInserted(posts.size()-1);
                    mAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            Intent i = new Intent(getApplicationContext(),
                    FeedActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
