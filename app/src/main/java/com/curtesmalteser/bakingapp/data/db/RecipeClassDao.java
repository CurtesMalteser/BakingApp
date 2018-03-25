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
public abstract class RecipeClassDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertRecipes(ArrayList<BakingModel> insertRecipes);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertIngredients(ArrayList<Ingredient> insertRecipes);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertSteps(ArrayList<Step> insertRecipes);

    @Transaction
    @Query("SELECT * FROM Recipes")
    abstract LiveData<List<FullRecipes>> getRecipes();

    @Transaction
    @Query("SELECT * FROM Recipes WHERE id = :id")
    abstract LiveData<FullRecipes> getRecipeById(int id);

    @Query("DELETE FROM Recipes")
    abstract void deleteRecipes();

    @Query("DELETE FROM Ingredients")
    abstract void deleteIngredients();

    @Query("DELETE FROM Steps")
    abstract void deleteSteps();

    @Transaction
    void updateData(ArrayList<BakingModel> recipes, ArrayList<Ingredient> ingredients, ArrayList<Step> steps) {
        deleteRecipes();
        deleteIngredients();
        deleteSteps();
        insertRecipes(recipes);
        insertIngredients(ingredients);
        insertSteps(steps);

    }
}
