package com.yellowsoft.subhankar.pokesnap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by subhankar on 8/17/2016.
 */
public class LikersAdapter extends RecyclerView.Adapter<LikersAdapter.LikersViewHolder> {

    private Context context;
    private ArrayList<String> likers = new ArrayList<>();

    public LikersAdapter(Context context, ArrayList<String> objects) {
        this.context = context;
        likers = objects;
    }

    public class LikersViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;

        public LikersViewHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.username);
        }
    }

    @Override
    public LikersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_liker, parent, false);
        LikersViewHolder myViewHolder = new LikersViewHolder(itemView);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final LikersViewHolder holder, int position) {
        String name = likers.get(position);
        if(name != null)
            holder.userName.setText(name);

    }

    @Override
    public int getItemCount() {
        return likers.size();
    }

}
