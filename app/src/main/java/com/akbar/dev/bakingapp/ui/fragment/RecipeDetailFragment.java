package com.akbar.dev.bakingapp.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akbar.dev.bakingapp.R;
import com.akbar.dev.bakingapp.adapter.IngredientAdapter;
import com.akbar.dev.bakingapp.adapter.StepAdapter;
import com.akbar.dev.bakingapp.model.Recipe;
import com.akbar.dev.bakingapp.ui.activity.RecipeDetailActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.akbar.dev.bakingapp.ui.activity.RecipeActivity.SELECTED_RECIPES;

public class RecipeDetailFragment extends Fragment {

    @BindView(R.id.recipe_ingredient_recycler)RecyclerView mRecyclerIngredient;
    @BindView(R.id.recipe_step_recycler)RecyclerView mRecyclerStep;

    private ArrayList<Recipe> recipe;
    private String recipeName;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        ButterKnife.bind(this, view);

        initComponents(savedInstanceState);

        return view;
    }

    private void initComponents(Bundle savedInstanceState){
        recipe = new ArrayList<>();

        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelableArrayList(SELECTED_RECIPES);
        } else {
            recipe = getArguments().getParcelableArrayList(SELECTED_RECIPES);
        }

        recipeName = recipe.get(0).getName();

        initRecyclerViews();
    }

    private void initRecyclerViews(){
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerIngredient.setLayoutManager(mLayoutManager);
        mRecyclerStep.setLayoutManager(layoutManager);

        IngredientAdapter ingredientAdapter = new IngredientAdapter(getActivity());
        mRecyclerIngredient.setAdapter(ingredientAdapter);
        ingredientAdapter.setIngredientData(recipe, getActivity());

        StepAdapter stepAdapter = new StepAdapter((RecipeDetailActivity)getActivity());
        mRecyclerStep.setAdapter(stepAdapter);
        stepAdapter.setStepData(recipe, getActivity());
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList(SELECTED_RECIPES, recipe);
        currentState.putString("Title", recipeName);
    }

}
