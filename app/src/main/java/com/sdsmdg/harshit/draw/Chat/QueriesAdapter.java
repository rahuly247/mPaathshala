package com.sdsmdg.harshit.draw.Chat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sdsmdg.harshit.draw.R;

import java.util.List;

public class QueriesAdapter extends RecyclerView.Adapter<QueriesAdapter.QueryViewHolder> {
    private List<Query> list;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class QueryViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        protected TextView questionTextView;
        protected TextView answerTextView;

        public QueryViewHolder (View view)
        {
            super(view);
            this.questionTextView = (TextView) view.findViewById(R.id.list_item_question);
            this.answerTextView = (TextView) view.findViewById(R.id.list_item_answer);
        }
    }

    public void swap(List<Query> templist)
    {
        if(templist!=null) {
            list.clear();
            list.addAll(templist);
            notifyDataSetChanged();
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public QueriesAdapter(Context context, List<Query> myDataset) {
        mContext = context;
        list = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public QueriesAdapter.QueryViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        QueryViewHolder vh = new QueryViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(QueryViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.questionTextView.setText(list.get(position).getmQuery());
        holder.questionTextView.setTag(holder);
        holder.questionTextView.setOnClickListener(clickListener);

        holder.answerTextView.setText(list.get(position).getmAnswer());
        holder.answerTextView.setTag(holder);
        holder.answerTextView.setOnClickListener(clickListener);

    }

    View.OnClickListener clickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            QueryViewHolder viewHolder = (QueryViewHolder)v.getTag();
            int position = viewHolder.getPosition();
            Query q = list.get(position);

            Intent i = new Intent(mContext, Answer.class);
            i.putExtra("QueryId", q.getId());
            i.putExtra("question", q.getmQuery());
            i.putExtra("answer", q.getmAnswer());
            mContext.startActivity(i);

        }
    };

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(list!=null)
        return list.size();
        return 0;
    }
}