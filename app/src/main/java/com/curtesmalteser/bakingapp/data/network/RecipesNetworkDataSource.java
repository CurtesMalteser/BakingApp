package com.curtesmalteser.bakingapp.data.network;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.curtesmalteser.bakingapp.AppExecutors;
import com.curtesmalteser.bakingapp.data.model.BakingModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by António "Curtes Malteser" Bastião on 30/03/2018.
 */

public class RecipesNetworkDataSource {

    private static final String TAG = RecipesNetworkDataSource.class.getSimpleName();

    private static final Object LOCK = new Object();
    @SuppressLint("StaticFieldLeak")
    private static RecipesNetworkDataSource sInstance;

    private final MutableLiveData<List<BakingModel>> mDownloadedRecipes;
    private final AppExecutors mExecutors;

    public RecipesNetworkDataSource(AppExecutors mExecutors) {
        this.mExecutors = mExecutors;
        mDownloadedRecipes = new MutableLiveData<>();
    }

    public static RecipesNetworkDataSource getsInstance(AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new RecipesNetworkDataSource(executors);
            }
        }
        return sInstance;
    }

    public LiveData<List<BakingModel>> getRecipes() {
        return mDownloadedRecipes;
    }

    public void fetchRecipes() {
        mExecutors.networkIO().execute(() -> {
            BakingAPIInterface apiInterface = BakingAPIClient.getClient().create(BakingAPIInterface.class);
            Call<List<BakingModel>> call;

            call = apiInterface.getRecipes();

            call.enqueue(new Callback<List<BakingModel>>() {
                @Override
                public void onResponse(Call<List<BakingModel>> call, Response<List<BakingModel>> response) {
                    ArrayList<BakingModel> recipesResponse = new ArrayList<>();
                    if (response.code() == 200) {
                        Log.d(TAG, "Response from the API ");
                        for (BakingModel model : response.body()) {
                            recipesResponse.add(model);
                        }

                        mDownloadedRecipes.postValue(recipesResponse);
                    } else {
                        Log.d(TAG, "Response error from the API ");
                    }
                }

                @Override
                public void onFailure(Call<List<BakingModel>> call, Throwable t) {
                    Log.e(TAG, t.toString());
                }
            });
        });

    }
}
