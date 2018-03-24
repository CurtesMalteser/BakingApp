package com.curtesmalteser.bakingapp.api.retrofit;

import com.curtesmalteser.bakingapp.api.model.BakingModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by António "Curtes Malteser" Bastião on 24/03/2018.
 */


public interface BakingAPIInterface {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<BakingModel>> getRecipes();
}
