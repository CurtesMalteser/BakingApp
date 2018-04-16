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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by António "Curtes Malteser" Bastião on 01/04/2018.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>{

    private final List<Ingredient> mIngredientsArrayList;

    IngredientsAdapter(List<Ingredient> mIngredientsArrayList) {
        this.mIngredientsArrayList = mIngredientsArrayList;
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

    public class IngredientsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvIngredient)
        TextView tvIngredient;

        @BindView(R.id.tvQuantity)
        TextView tvQuantity;

        @BindView(R.id.tvMeasure)
        TextView tvMeasure;

        IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int position) {
            final Ingredient ingredient = mIngredientsArrayList.get(position);
            tvIngredient.setText(ingredient.getIngredient());
            tvQuantity.setText(String.valueOf(ingredient.getQuantity()));
            tvMeasure.setText(ingredient.getMeasure());
        }
    }
}
