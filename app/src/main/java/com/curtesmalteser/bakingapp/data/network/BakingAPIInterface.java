package com.curtesmalteser.bakingapp.data.network;

import com.curtesmalteser.bakingapp.data.model.BakingModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by António "Curtes Malteser" Bastião on 24/03/2018.
 */


interface BakingAPIInterface {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<BakingModel>> getRecipes();
}
