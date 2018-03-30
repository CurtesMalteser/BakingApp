
package com.curtesmalteser.bakingapp.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "Ingredients",
        foreignKeys = @ForeignKey(entity = BakingModel.class,
                parentColumns = "id",
                childColumns = "recipeId",
                onDelete = CASCADE),
        indices = @Index(value = "recipeId"))
public class Ingredient {

    @PrimaryKey(autoGenerate = true)
    private int columnId;

    @SerializedName("quantity")
    @Expose
    private Double quantity;
    @SerializedName("measure")
    @Expose
    private String measure;
    @SerializedName("ingredient")
    @Expose
    private String ingredient;

    private int recipeId;

    @Ignore
    public Ingredient(Double quantity, String measure, String ingredient, int recipeId) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
        this.recipeId = recipeId;
    }

    public Ingredient(int columnId, Double quantity, String measure, String ingredient, int recipeId) {
        this.columnId = columnId;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
        this.recipeId = recipeId;
    }

    public int getColumnId() {
        return columnId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public int getRecipeId() {
        return recipeId;
    }
}
