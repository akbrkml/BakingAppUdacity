package com.akbar.dev.bakingapp.ui.activity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.akbar.dev.bakingapp.R;
import com.akbar.dev.bakingapp.adapter.RecipeWidgetAdapter;
import com.akbar.dev.bakingapp.api.ApiClient;
import com.akbar.dev.bakingapp.api.ApiInterface;
import com.akbar.dev.bakingapp.model.Ingredient;
import com.akbar.dev.bakingapp.model.Recipe;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.akbar.dev.bakingapp.ui.activity.RecipeActivity.recipes;

public class RecipeWidgetActivity extends AppCompatActivity implements RecipeWidgetAdapter.OnRecipeClickListener {

    @BindView(R.id.recycler_view_bake)
    RecyclerView mRecyclerViewBake;

    private RecipeWidgetAdapter adapter;
    private int mWidgetId;
    private ArrayList<Recipe> recipes;

    ApiInterface apiService = ApiClient.Retrieve();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_widget);
        ButterKnife.bind(this);

        initComponents();
    }

    @Override
    public void onRecipeSelected(Recipe recipeClickedItem) {
        putRecipeToWidget(recipeClickedItem);
    }

    private void initComponents(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewBake.setLayoutManager(layoutManager);
        mRecyclerViewBake.setHasFixedSize(true);

        adapter = new RecipeWidgetAdapter(this);
        mRecyclerViewBake.setAdapter(adapter);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        loadRecipes();
    }

    private void putRecipeToWidget(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("recipe", new Gson().toJson(recipe));
        intent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getBaseContext());

        RemoteViews views = new RemoteViews(getBaseContext().getPackageName(), R.layout.baking_widget_provider);

        views.setTextViewText(R.id.widget_ingredients_title, getString(R.string.widget_ingredients_title, recipe.getName()));

        String strIngredient = "";
        for (Ingredient ingredient : recipe.getIngredients()) {
            DecimalFormat format = new DecimalFormat("#.##");

            strIngredient += "- " + format.format(ingredient.getQuantity())
                    + " " + ingredient.getMeasure() + " of " + ingredient.getIngredient() + ".";
            strIngredient += "\n";
        }

        views.setTextViewText(R.id.widget_detail_ingredients, strIngredient);

//        views.setOnClickPendingIntent(R.id.widget_detail_ingredients, pendingIntent);

        appWidgetManager.updateAppWidget(mWidgetId, views);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    private void loadRecipes(){

        callRecipes().enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                recipes = response.body();

                adapter.setRecipeWidgetData(recipes, getApplicationContext());
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
            }
        });
    }

    private Call<ArrayList<Recipe>> callRecipes(){
        return apiService.getDataRecipe();
    }

}
