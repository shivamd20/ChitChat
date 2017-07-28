package io.hasura.shivam.chitchat.recent.screens;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.hasura.shivam.chitchat.R;
import io.hasura.shivam.chitchat.database.Conversation;

/**
 * Created by shivam on 18/7/17.
 */

public class RecentRVAdapter  extends RecyclerView.Adapter<RecentRVAdapter.ViewHolder>{

    private List<Conversation> mDataset;

    public List<Conversation> getmDataset() {
        return mDataset;
    }

    private View.OnClickListener mOnClickListener;

    public void setmOnClickListener(View.OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }



    public void swap(List<Conversation> datas){
        mDataset.clear();
        mDataset.addAll(datas);
        notifyDataSetChanged();
    }


    public RecentRVAdapter(List<Conversation> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecentRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_recent_list, parent, false);
        try {
            v.setOnClickListener(mOnClickListener);
        }catch (NullPointerException nie)
        {
            nie.printStackTrace();
        }
        // set the view's size, margins, paddings and layout parameters
        // ...
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(mDataset.get(position).with!=null)
        holder.mNameView.setText(mDataset.get(position).with.name);
        else
        {
            holder.mNameView.setText("unKnown");
        }
        holder.lastMessege.setText(mDataset.get(position).message);
        holder.timeView.setText(mDataset.get(position).date.toString().split("G")[0]);
       // holder
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mNameView;
        public TextView lastMessege;
        public  TextView timeView;
        public ViewHolder(View v) {
            super(v);

            mNameView = (TextView) v.findViewById(R.id.name_view_contact);

            lastMessege=(TextView) v.findViewById(R.id.mobile_view_contact);

            timeView=(TextView) v.findViewById(R.id.time_recent);
        }
    }

}
