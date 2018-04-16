
package com.curtesmalteser.bakingapp.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "Steps",
        foreignKeys = @ForeignKey(entity = BakingModel.class,
                parentColumns = "id",
                childColumns = "recipeId",
                onDelete = CASCADE),
        indices = @Index(value = "recipeId"))
public class Step {

    @PrimaryKey(autoGenerate = true)
    private int columnId;

    @SerializedName("id")
    @Expose
    private final Integer stepNumber;
    @SerializedName("shortDescription")
    @Expose
    private final String shortDescription;
    @SerializedName("description")
    @Expose
    private final String description;
    @SerializedName("videoURL")
    @Expose
    private final String videoURL;
    @SerializedName("thumbnailURL")
    @Expose
    private final String thumbnailURL;

    private final int recipeId;

    @Ignore
    public Step(Integer stepNumber, String shortDescription, String description, String videoURL, String thumbnailURL, int recipeId) {
        this.stepNumber = stepNumber;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
        this.recipeId = recipeId;
    }

    public Step(int columnId, Integer stepNumber, String shortDescription, String description, String videoURL, String thumbnailURL, int recipeId) {
        this.columnId = columnId;
        this.stepNumber = stepNumber;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
        this.recipeId = recipeId;
    }

    public int getColumnId() {
        return columnId;
    }

    public Integer getStepNumber() {
        return stepNumber;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public int getRecipeId() {
        return recipeId;
    }
}


