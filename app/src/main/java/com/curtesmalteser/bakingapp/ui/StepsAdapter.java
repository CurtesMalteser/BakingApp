package com.curtesmalteser.bakingapp.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
    private SparseBooleanArray mSelectedItems;

    private static final int INGREDIENT_TV = 0;
    private static final int STEPS_VIEWS = 1;

    public interface ListItemClickListener {
        void onListItemClick(int step);
    }

    StepsAdapter(Context context, ArrayList<Step> stepsArrayList, ListItemClickListener listItemClickListener) {
        this.mContext = context;
        this.mStepsArrayList = stepsArrayList;
        this.mOnClickListener = listItemClickListener;
        mSelectedItems = new SparseBooleanArray();
    }


    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View v;
        if (viewType == INGREDIENT_TV) {
            v = inflater.inflate(R.layout.single_row_ingredients_tv, parent, false);
        } else {
            v = inflater.inflate(R.layout.single_row_step, parent, false);
        }
        return new StepsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return INGREDIENT_TV;
        } else
            return STEPS_VIEWS;
    }

    @Override
    public int getItemCount() {
        return mStepsArrayList.size();
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @Nullable
        @BindView(R.id.singleSelectIngredientsRow)
        FrameLayout singleSelectIngredientsRow;

        @Nullable
        @BindView(R.id.tvSelectIngredientsSingleRow)
        TextView tvIngredients;

        @Nullable
        @BindView(R.id.singleStepRow)
        ConstraintLayout singleStepRow;

        @Nullable
        @BindView(R.id.tvStep)
        TextView tvStep;

        public StepsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mSelectedItems.get(getAdapterPosition(), false)) {
                mSelectedItems.delete(getAdapterPosition());
                if (getAdapterPosition() == INGREDIENT_TV) {
                    singleSelectIngredientsRow.setSelected(false);
                } else{
                    singleStepRow.setSelected(false);
                }
            } else {
                mSelectedItems.put(getAdapterPosition(), true);
                if (getAdapterPosition() == INGREDIENT_TV) {
                    singleSelectIngredientsRow.setSelected(true);
                } else {
                    singleStepRow.setSelected(true);
                }
            }
            mOnClickListener.onListItemClick(getAdapterPosition());
        }

        public void bind(int position) {
            final Step step = mStepsArrayList.get(position);
            if (position != INGREDIENT_TV) {
                tvStep.setText(step.getShortDescription());
            }
        }
    }
}
