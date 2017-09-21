package com.akbar.dev.bakingapp.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akbar.dev.bakingapp.R;
import com.akbar.dev.bakingapp.adapter.RecipeAdapter;
import com.akbar.dev.bakingapp.api.ApiClient;
import com.akbar.dev.bakingapp.api.ApiInterface;
import com.akbar.dev.bakingapp.model.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeActivity extends AppCompatActivity implements RecipeAdapter.OnRecipeClickListener {

    public static String ALL_RECIPES="All_Recipes";
    public static String SELECTED_RECIPES="Selected_Recipes";
    public static String SELECTED_STEPS="Selected_Steps";
    public static String SELECTED_INDEX="Selected_Index";

    private static final String TAG = RecipeActivity.class.getSimpleName();

    @BindView(R.id.recycler_view_bake)
    RecyclerView mRecyclerViewBake;
    @BindView(R.id.refresh)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.tv_message_display)
    TextView mTextViewMessage;
    @BindView(R.id.main_progress_layout)
    RelativeLayout mProgressLayout;

    private LinearLayoutManager layoutManager;
    public static ArrayList<Recipe> recipes;
    private RecipeAdapter recipeAdapter;

    ApiInterface apiService = ApiClient.Retrieve();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Baking");

        ButterKnife.bind(this);

        initComponents();

        if (savedInstanceState != null){
            recipes = savedInstanceState.getParcelableArrayList(ALL_RECIPES);
            recipeAdapter.setRecipeData(recipes, this);
        } else {
            showMovieDataView();
        }

        onRefresh();

    }

    private void onRefresh(){
        mRefreshLayout.setColorSchemeColors(Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE);
        mRefreshLayout.setOnRefreshListener(() -> {
            mRefreshLayout.setRefreshing(false);
            showMovieDataView();
        });
    }

    private void initComponents(){
        initRecyclerView();
    }

    private int numberOfColumns() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = getResources().getDisplayMetrics().density;
        float dpWidth = outMetrics.widthPixels / density;
        return Math.round(dpWidth / 360);
    }

    private void initRecyclerView(){
        recipeAdapter = new RecipeAdapter(this);
        layoutManager = new GridLayoutManager(getApplicationContext(), numberOfColumns(), LinearLayoutManager.VERTICAL, false);
        mRecyclerViewBake.setHasFixedSize(true);
        mRecyclerViewBake.setLayoutManager(layoutManager);
        mRecyclerViewBake.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewBake.setAdapter(recipeAdapter);
    }

    private void loadRecipes(){

        callRecipes().enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                recipes = response.body();
                Log.d(TAG + " onResponse: ", recipes.toString());

                recipeAdapter.setRecipeData(recipes, getApplicationContext());

                mProgressLayout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.d(TAG + " onFailure: ", t.getMessage());
                showErrorMessage();
                mProgressLayout.setVisibility(View.GONE);
            }
        });
    }

    private Call<ArrayList<Recipe>> callRecipes(){
        return apiService.getDataRecipe();
    }

    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mTextViewMessage.setVisibility(View.INVISIBLE);
        /* Then, make sure the movie data is visible */
        mRecyclerViewBake.setVisibility(View.VISIBLE);
        mProgressLayout.setVisibility(View.VISIBLE);

        loadRecipes();
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerViewBake.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mTextViewMessage.setVisibility(View.VISIBLE);
        mTextViewMessage.setText(R.string.error_message);
    }

    @Override
    public void onRecipeSelected(Recipe recipeClickedItem) {
        Bundle selectedRecipeBundle = new Bundle();
        ArrayList<Recipe> selectedRecipe = new ArrayList<>();
        selectedRecipe.add(recipeClickedItem);
        selectedRecipeBundle.putParcelableArrayList(SELECTED_RECIPES, selectedRecipe);

        Log.d("selected", selectedRecipe.toString());
        final Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtras(selectedRecipeBundle);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ALL_RECIPES, recipes);
    }
}
