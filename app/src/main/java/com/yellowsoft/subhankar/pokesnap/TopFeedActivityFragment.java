package com.yellowsoft.subhankar.pokesnap;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class TopFeedActivityFragment extends Fragment {
    private SessionManager session;
    ArrayList<Post> posts = new ArrayList<Post>();
    private RecyclerView recyclerView;
    private FeedAdapter mAdapter;
    private String token;

    private static final String TAG = TopFeedActivityFragment.class.getSimpleName();

    public TopFeedActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(getContext());

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Log.d(TAG, "Before Call");

        token = session.getToken();

        Call<List<Post>> call = apiService.getTopposts(token);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                //Log.d(TAG, "Number of movies received: " + response.body().toString());

                if(!response.body().isEmpty()) {
                    for (int i = 0; i < response.body().size(); i++) {
                        Post post = response.body().get(i);
                        posts.add(post);
                        Log.d(TAG, "Number of movies received: " + post);
                    }
//                Post post = response.body().get(0).getPostBody();
                    Log.d(TAG, "Number of movies received: " + posts.get(0).getPostBody());
                    mAdapter = new FeedAdapter(getContext(), posts);
                    recyclerView.setAdapter(mAdapter);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

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


        return view;
    }

    public void customLoadMoreDataFromApi(String lastid) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<List<Post>> call = apiService.getMoreTopposts(lastid, token);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if (!response.body().isEmpty()) {

//                    for (int i = 0; i < response.body().size(); i++) {
//                        Post post = response.body().get(i);
//                        posts.add(post);
//                    }
                    posts.addAll(response.body());
                    int currSize = mAdapter.getItemCount();
//                Post post = response.body().get(0).getPostBody();
                    //mAdapter = new FeedAdapter(getContext(), posts);
                    //recyclerView.setAdapter(mAdapter);
//                    mAdapter.notifyItemInserted(posts.size()-1);
                    mAdapter.notifyItemRangeInserted(currSize, posts.size()-1);
                }

            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }
}