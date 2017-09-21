package com.akbar.dev.bakingapp.ui.activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.akbar.dev.bakingapp.R;
import com.akbar.dev.bakingapp.adapter.StepAdapter;
import com.akbar.dev.bakingapp.model.Recipe;
import com.akbar.dev.bakingapp.model.Step;
import com.akbar.dev.bakingapp.ui.fragment.RecipeDetailFragment;
import com.akbar.dev.bakingapp.ui.fragment.RecipeStepFragment;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeStepFragment.StepItemClickListener, StepAdapter.StepItemClickListener {

    private ArrayList<Recipe> recipe;
    private String recipeName;

    static String STACK_RECIPE_DETAIL="STACK_RECIPE_DETAIL";
    static String STACK_RECIPE_STEP_DETAIL="STACK_RECIPE_STEP_DETAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (savedInstanceState == null) {

            Bundle selectedRecipeBundle = getIntent().getExtras();

            recipe = new ArrayList<>();
            recipe = selectedRecipeBundle.getParcelableArrayList(RecipeActivity.SELECTED_RECIPES);
            recipeName = recipe.get(0).getName();

            RecipeDetailFragment fragment = new RecipeDetailFragment();
            fragment.setArguments(selectedRecipeBundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment).addToBackStack(STACK_RECIPE_DETAIL)
                    .commit();

            if (findViewById(R.id.recipe_layout).getTag() != null && findViewById(R.id.recipe_layout).getTag().equals("tablet-view")) {

                RecipeStepFragment stepFragment = new RecipeStepFragment();
                stepFragment.setArguments(selectedRecipeBundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.recipe_step_container, stepFragment).addToBackStack(STACK_RECIPE_STEP_DETAIL)
                        .commit();

            }
        } else {
            recipeName = savedInstanceState.getString("Title");
        }

        getSupportActionBar().setTitle(recipeName);
    }

    @Override
    public void onStepItemClick(List<Step> stepList, int position, String recipeName) {
        RecipeStepFragment fragment = new RecipeStepFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        getSupportActionBar().setTitle(recipeName);

        Bundle stepBundle = new Bundle();
        stepBundle.putParcelableArrayList(RecipeActivity.SELECTED_STEPS, (ArrayList<Step>) stepList);
        stepBundle.putInt(RecipeActivity.SELECTED_INDEX, position);
        stepBundle.putString("Title", recipeName);
        fragment.setArguments(stepBundle);

        if (findViewById(R.id.recipe_layout).getTag() != null && findViewById(R.id.recipe_layout).getTag().equals("tablet-view")) {
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_step_container, fragment).addToBackStack(STACK_RECIPE_STEP_DETAIL)
                    .commit();

        }
        else {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment).addToBackStack(STACK_RECIPE_STEP_DETAIL)
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Title",recipeName);
    }
}
