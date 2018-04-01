package com.curtesmalteser.bakingapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curtesmalteser.bakingapp.R;
import com.curtesmalteser.bakingapp.data.model.Ingredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by António "Curtes Malteser" Bastião on 01/04/2018.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>{

    private ArrayList<Ingredient> mIngredientsArrayList;
    private ListItemClickListener mListItemClickListener;

    public interface ListItemClickListener{
        void onListItemClick(Ingredient ingredient);
    }

    IngredientsAdapter(ArrayList<Ingredient> mIngredientsArrayList, ListItemClickListener mListItemClickListener) {
        this.mIngredientsArrayList = mIngredientsArrayList;
        this.mListItemClickListener = mListItemClickListener;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutForSingleIngredient = R.layout.single_ingredient;
        LayoutInflater inflater = LayoutInflater.from(context);

        View v = inflater.inflate(layoutForSingleIngredient, parent, false);

        return new IngredientsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mIngredientsArrayList.size();
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.tvIngredient)
        TextView tvIngredient;

        @BindView(R.id.tvQuantity)
        TextView tvQuantity;

        @BindView(R.id.tvMeasure)
        TextView tvMeasure;

        public IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Ingredient ingredient = mIngredientsArrayList.get(getAdapterPosition());
            mListItemClickListener.onListItemClick(ingredient);

        }

        public void bind(int position) {
            final Ingredient ingredient = mIngredientsArrayList.get(position);
            tvIngredient.setText(ingredient.getIngredient());
            tvQuantity.setText(String.valueOf(ingredient.getQuantity()));
            tvMeasure.setText(ingredient.getMeasure());
        }
    }
}
