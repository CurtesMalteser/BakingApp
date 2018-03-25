package com.curtesmalteser.bakingapp.data.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.curtesmalteser.bakingapp.data.model.BakingModel;
import com.curtesmalteser.bakingapp.data.model.Ingredient;
import com.curtesmalteser.bakingapp.data.model.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by António "Curtes Malteser" Bastião on 25/03/2018.
 */

@Dao
public interface RecipeDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipes(ArrayList<BakingModel> insertRecipes);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIngredients(ArrayList<Ingredient> insertRecipes);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSteps(ArrayList<Step> insertRecipes);

    @Transaction
    @Query("SELECT * FROM Recipes")
    LiveData<List<FullRecipes>> getRecipes();

    @Transaction
    @Query("SELECT * FROM Recipes WHERE id = :id")
    LiveData<FullRecipes> getRecipeById(int id);

    @Query("DELETE FROM Recipes")
    void deleteRecipes();

    @Query("DELETE FROM Ingredients")
    void deleteIngredients();

    @Query("DELETE FROM Steps")
    void deleteSteps();
}

