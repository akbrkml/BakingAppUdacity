package com.akbar.dev.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akbar.dev.bakingapp.R;
import com.akbar.dev.bakingapp.model.Ingredient;
import com.akbar.dev.bakingapp.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by akbar on 08/09/17.
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientHolder> {

    private Context mContext;
    private List<Ingredient> mListIngredients;

    public IngredientAdapter(Context context){
        this.mContext = context;
    }

    public void setIngredientData(List<Recipe> mListRecipe, Context context) {
        this.mListIngredients = mListRecipe.get(0).getIngredients();
        this.mContext = context;
        notifyDataSetChanged();
    }

    @Override
    public IngredientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_ingredient, parent,  false);
        IngredientHolder ingredientHolder = new IngredientHolder(view);
        return ingredientHolder;
    }

    @Override
    public void onBindViewHolder(IngredientHolder holder, int position) {
        holder.mTextViewIngredient.setText(
                mListIngredients.get(position).getIngredient() + "  " +
                mListIngredients.get(position).getQuantity().toString() + " " +
                mListIngredients.get(position).getMeasure()
        );
    }

    @Override
    public int getItemCount() {
        return mListIngredients != null ? mListIngredients.size():0;
    }

    public class IngredientHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view_ingredient)
        TextView mTextViewIngredient;
        public IngredientHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
