package com.akbar.dev.bakingapp.api;


import com.akbar.dev.bakingapp.model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by kamal on 08/02/2017.
 */

public interface ApiInterface {
    @GET("baking.json")
    Call<ArrayList<Recipe>> getDataRecipe();
}
