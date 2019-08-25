package com.example.bakingapp.rest;

import com.example.bakingapp.models.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeService {

    //Get list of recipes
    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipes();
}
