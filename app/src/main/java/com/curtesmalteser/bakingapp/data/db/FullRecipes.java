package com.curtesmalteser.bakingapp.data.db;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Relation;

import com.curtesmalteser.bakingapp.data.model.BakingModel;
import com.curtesmalteser.bakingapp.data.model.Ingredient;
import com.curtesmalteser.bakingapp.data.model.Step;

import java.util.List;

/**
 * Created by António "Curtes Malteser" Bastião on 25/03/2018.
 */

class FullRecipes {

    @Embedded
    public BakingModel bakingModel;

    @Relation(parentColumn = "id",
            entityColumn = "recipeId")
    public List<Ingredient> ingredientList;

    @Relation(parentColumn = "id",
             entityColumn = "recipeId")
    public List<Step> stepList;
}
