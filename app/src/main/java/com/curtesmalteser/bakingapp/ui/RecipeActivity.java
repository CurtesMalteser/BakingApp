package com.curtesmalteser.bakingapp.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.curtesmalteser.bakingapp.R;
import com.curtesmalteser.bakingapp.data.model.BakingModel;
import com.curtesmalteser.bakingapp.data.retrofit.BakingAPIClient;
import com.curtesmalteser.bakingapp.data.retrofit.BakingAPIInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeActivity extends AppCompatActivity
        implements RecipesAdapter.ListItemClickListener {

    private static final String TAG = RecipeActivity.class.getSimpleName();

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private ArrayList<BakingModel> mResultList = new ArrayList<>();

    private RecipesAdapter mRecipesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        ButterKnife.bind(this);

        boolean isTablet = getResources().getBoolean(R.bool.is_tablet);

        mRecipesAdapter = new RecipesAdapter(this, mResultList, this);
        mRecyclerView.setAdapter(mRecipesAdapter);
        mRecyclerView.setHasFixedSize(true);

        if (isTablet) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }

        BakingAPIInterface apiInterface = BakingAPIClient.getClient().create(BakingAPIInterface.class);
        Call<List<BakingModel>> call;

        call = apiInterface.getRecipes();

        call.enqueue(new Callback<List<BakingModel>>() {
            @Override
            public void onResponse
                    (Call<List<BakingModel>> call, Response<List<BakingModel>> response) {

                for (BakingModel model : response.body()) {
                    mResultList.add(model);
                    mRecipesAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<List<BakingModel>> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    @Override
    public void onListItemClick(BakingModel bakingModel) {
        Intent i = new Intent(this, DetailActivity.class);
        startActivity(i);
    }
}
