package com.curtesmalteser.bakingapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curtesmalteser.bakingapp.R;
import com.curtesmalteser.bakingapp.data.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by António "Curtes Malteser" Bastião on 01/04/2018.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {

    private Context mContext;
    private ArrayList<Step> mStepsArrayList;
    private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(Step step);
    }

    public StepsAdapter(Context mContext, ArrayList<Step> stepsArrayList, ListItemClickListener listItemClickListener) {
        this.mContext = mContext;
        this.mStepsArrayList = stepsArrayList;
        this.mOnClickListener = listItemClickListener;
    }


    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutForSingleStep = R.layout.single_step;
        LayoutInflater inflater = LayoutInflater.from(context);

        View v = inflater.inflate(layoutForSingleStep, parent, false);

        return new StepsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mStepsArrayList.size();
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.tvStep)
        TextView tvStep;

        public StepsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Step step = mStepsArrayList.get(getAdapterPosition());
            mOnClickListener.onListItemClick(step);

        }

        public void bind(int position) {

            final Step step = mStepsArrayList.get(position);
            tvStep.setText(step.getShortDescription());
        }
    }
}
