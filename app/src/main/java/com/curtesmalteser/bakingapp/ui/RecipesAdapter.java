package com.curtesmalteser.bakingapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.curtesmalteser.bakingapp.R;
import com.curtesmalteser.bakingapp.data.db.FullRecipes;
import com.curtesmalteser.bakingapp.data.model.BakingModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by António "Curtes Malteser" Bastião on 24/03/2018.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder> {

    private Context mContext;
    private ArrayList<FullRecipes> mRecipesArrayList;
    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(FullRecipes bakingModel);
    }

    public RecipesAdapter(Context context, ArrayList<FullRecipes> recipesArrayList,
                          ListItemClickListener listener) {
        this.mContext = context;
        this.mRecipesArrayList = recipesArrayList;
        this.mOnClickListener = listener;

    }

    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.single_recipe_phone;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new RecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mRecipesArrayList.size();
    }

    public class RecipesViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.cakeImage)
        ImageView cakeImage;

        @BindView(R.id.recipeName)
        TextView recipeName;

        public RecipesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            FullRecipes bakingModel = mRecipesArrayList.get(clickedPosition);
            mOnClickListener.onListItemClick(bakingModel);
        }

        public void bind(int position) {
            final FullRecipes model = mRecipesArrayList.get(position);

            if (model.bakingModel.getImage() != null && !model.bakingModel.getImage().equals("")) {
                Picasso.with(mContext)
                        .load(model.bakingModel.getImage())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(cakeImage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                //Try again online if cache failed
                                Picasso.with(mContext)
                                        .load(model.bakingModel.getImage())
                                        .error(R.drawable.ic_pastry_cake)
                                        .into(cakeImage, new Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError() {
                                                Log.v("Picasso", "Could not fetch image");
                                            }
                                        });
                            }
                        });

            } else {
                cakeImage.setImageResource(R.drawable.ic_pastry_cake);
            }

            recipeName.setText(String.valueOf(model.bakingModel.getName()));
        }
    }
}
