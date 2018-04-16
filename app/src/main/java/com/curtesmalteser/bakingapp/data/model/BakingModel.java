
package com.curtesmalteser.bakingapp.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "Recipes")
public class BakingModel {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private final Integer id;
    @SerializedName("name")
    @Expose
    private final String name;
    @Ignore
    @SerializedName("ingredients")
    @Expose
    private final List<Ingredient> ingredients = null;
    @Ignore
    @SerializedName("steps")
    @Expose
    private final List<Step> steps = null;
    @SerializedName("servings")
    @Expose
    private final Integer servings;
    @SerializedName("image")
    @Expose
    private final String image;

    public BakingModel(Integer id, String name, Integer servings, String image) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public Integer getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }
}
