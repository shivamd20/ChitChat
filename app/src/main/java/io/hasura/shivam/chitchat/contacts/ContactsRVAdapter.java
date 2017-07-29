package io.hasura.shivam.chitchat.contacts;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.hasura.shivam.chitchat.R;
import io.hasura.shivam.chitchat.database.Person;

/**
 * Created by shivam on 13/7/17.
 */

public class ContactsRVAdapter extends RecyclerView.Adapter<ContactsRVAdapter.ViewHolder>{

    private List<Person>  mDataset;

    public List<Person> getmDataset() {
        return mDataset;
    }

    private View.OnClickListener mOnClickListener;

    public void setmOnClickListener(View.OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    public ContactsRVAdapter(List<Person> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ContactsRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_viewholder, parent, false);

        v.setOnClickListener(this.mOnClickListener);

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
        holder.mNameView.setText(mDataset.get(position).name);
        holder.mobileView.setText(mDataset.get(position).mobile);



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mNameView;
        public TextView mobileView;
        public ViewHolder(View v) {
            super(v);

//            v.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Snackbar.make(v,""+getLayoutPosition(),Snackbar.LENGTH_SHORT).show();
//
//                }
//            });

            mNameView = (TextView) v.findViewById(R.id.name_view_contact);

            mobileView=(TextView) v.findViewById(R.id.mobile_view_contact);
        }
    }
}
