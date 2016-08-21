package com.yellowsoft.subhankar.pokesnap;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by subhankar on 7/17/2016.
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder> implements View.OnClickListener {

//    private final ArrayList<Integer> likedPositions = new ArrayList<>();
    private final Map<Integer, Integer> likesCount = new HashMap<>();

    private Context context;
    ArrayList<Post> ArrayListPosts;

    public SessionManager session;
    public String user;

    public FeedAdapter(Context context, ArrayList<Post> objects) {
        this.context = context;
        ArrayListPosts = objects;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, userName2, body;
        public ImageView imageFeedItem, ivUserProfilePic;
        public ImageButton btnLike;
        public TextSwitcher likesCounter;
        public LinearLayout postBody;

        public MyViewHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.text_user_name);
            userName2 = (TextView) view.findViewById(R.id.text_user_name2);
            body = (TextView) view.findViewById(R.id.text_body);
            imageFeedItem = (ImageView) view.findViewById(R.id.ivFeedCenter);
            btnLike = (ImageButton) view.findViewById(R.id.btnLike);
            likesCounter = (TextSwitcher) view.findViewById(R.id.tsLikesCounter);
            ivUserProfilePic = (ImageView) view.findViewById(R.id.ivUserProfilePic);
            postBody = (LinearLayout) view.findViewById(R.id.postBody);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_feed, parent, false);
//        MyViewHolder.btnLike.setOnClickListener(this);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        myViewHolder.btnLike.setOnClickListener(this);
        myViewHolder.userName.setOnClickListener(this);
        myViewHolder.likesCounter.setOnClickListener(this);

        // Session manager
        session = new SessionManager(context);
        user = session.getName();

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Post post = ArrayListPosts.get(position);
        String postedBy = post.getUserName();
        holder.userName.setText(postedBy);

        if(post.getPostBody() != "") {
            holder.postBody.setVisibility(View.VISIBLE);
            holder.body.setText(post.getPostBody());
            holder.userName2.setText(postedBy);
        } else {
            holder.postBody.setVisibility(View.INVISIBLE);
        }
//        Picasso.with(context).load(post.getImageUrl()).into(holder.imageFeedItem);
        if(post.getImageUrl() != "") {
//            Glide.with(context).load(post.getImageUrl())
//                    .thumbnail(0.5f)
//                    .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(holder.imageFeedItem);
            Picasso.with(context).load(post.getImageUrl()).resize(320
                    , 0).into(holder.imageFeedItem);
        } else {
            holder.imageFeedItem.setVisibility(View.INVISIBLE);
        }

        String likesCountText = context.getResources().getQuantityString(
                R.plurals.likes_count, post.getLikesCount(), post.getLikesCount()
        );
        holder.likesCounter.setText(likesCountText);
        holder.likesCounter.setTag(holder);
        likesCount.put(position, post.getLikesCount());
        holder.btnLike.setTag(holder);
        if(post.getLikes().contains(user)) {
            //likedPositions.add(holder.getAdapterPosition());
            updateHeartButton(holder, true);
        } else {
            holder.btnLike.setImageResource(R.drawable.ic_heart_outline_grey);
        }
        holder.userName.setTag(holder);
        if(post.getTeam().equals("Team Instinct")) {
            holder.ivUserProfilePic.setImageResource(R.drawable.pokemongoinstinct);
        } else if(post.getTeam().equals("Team Mystic")) {
            holder.ivUserProfilePic.setImageResource(R.drawable.pokemongomystic);
        } else {
            holder.ivUserProfilePic.setImageResource(R.drawable.pokemongovalor);
        }
    }

    @Override
    public int getItemCount() {
        return ArrayListPosts.size();
    }

    @Override
    public void onClick(View view) {
        MyViewHolder holder = (MyViewHolder) view.getTag();
        Integer position = holder.getAdapterPosition();
        final int viewId = view.getId();
        if (viewId == R.id.btnLike) {
            String id = ArrayListPosts.get(position).get_id();
            Post post = ArrayListPosts.get(position);
            if (!post.getLikes().contains(user)) {
//                likedPositions.add(position);
                post.addLike(user);
                increaseLikesCounter(holder, true);
                updateHeartButton(holder, true);
                likePost(user, id);
                if (context instanceof FeedActivity) {
                    ((FeedActivity) context).showLikedSnackbar();
                }
            }
            else {
//                likedPositions.remove(position);
                post.removeLike(user);
                decreaseLikesCounter(holder, true);
                holder.btnLike.setImageResource(R.drawable.ic_heart_outline_grey);
                likePost(user, id);
            }
            notifyItemChanged(position);
        }
        if(viewId == R.id.text_user_name) {
            Intent i = new Intent(context,
                    UserProfileActivity.class);
            i.putExtra("username", ArrayListPosts.get(position).getUserName());
            i.putExtra("team", ArrayListPosts.get(position).getTeam());
            context.startActivity(i);
        }
        if(viewId == R.id.tsLikesCounter) {
            Intent i = new Intent(context, LikersActivity.class);
            i.putExtra("likers", ArrayListPosts.get(position).getLikes());
            context.startActivity(i);
        }
    }

    private void increaseLikesCounter(MyViewHolder holder, boolean animated) {
        int currentLikesCount = likesCount.get(holder.getAdapterPosition()) + 1;

        ArrayListPosts.get(holder.getAdapterPosition()).setLikesCount(currentLikesCount);
        String likesCountText = context.getResources().getQuantityString(
                R.plurals.likes_count, currentLikesCount, currentLikesCount
        );

        if (animated) {
            holder.likesCounter.setText(likesCountText);
        } else {
            holder.likesCounter.setCurrentText(likesCountText);
        }

        likesCount.put(holder.getAdapterPosition(), currentLikesCount);
    }


    private void decreaseLikesCounter(MyViewHolder holder, boolean animated) {
        int currentLikesCount = likesCount.get(holder.getAdapterPosition()) - 1;
        ArrayListPosts.get(holder.getAdapterPosition()).setLikesCount(currentLikesCount);
        String likesCountText = context.getResources().getQuantityString(
                R.plurals.likes_count, currentLikesCount, currentLikesCount
        );

        if (animated) {
            holder.likesCounter.setText(likesCountText);
        } else {
            holder.likesCounter.setCurrentText(likesCountText);
        }

        likesCount.put(holder.getAdapterPosition(), currentLikesCount);
    }

    private void updateHeartButton(final MyViewHolder holder, boolean animated) {
        holder.btnLike.setImageResource(R.drawable.ic_heart_red);

    }

    private void likePost(String username, String postid) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<Result> call = apiService.like(postid, username);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

            }
        });
    }
}
