package com.akbar.dev.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akbar.dev.bakingapp.R;
import com.akbar.dev.bakingapp.model.Recipe;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by akbar on 23/08/17.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {

    private Context mContext;
    private ArrayList<Recipe> mListRecipe;
    private OnRecipeClickListener mCallback;

    // OnImageClickListener interface, calls a method in the host activity named onRecipeSelected
    public interface OnRecipeClickListener {
        void onRecipeSelected(Recipe recipeClickedItem);
    }

    public RecipeAdapter(OnRecipeClickListener listener) {
        this.mCallback = listener;
    }

    public void setRecipeData(ArrayList<Recipe> listRecipe, Context context) {
        this.mListRecipe = listRecipe;
        this.mContext = context;
        notifyDataSetChanged();
    }

    public class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.text_view_name)
        TextView mTextViewName;
        @BindView(R.id.thumbImage)
        ImageView mImageViewThumb;
        public RecipeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getAdapterPosition();
            mCallback.onRecipeSelected(mListRecipe.get(itemPosition));
        }
    }

    public RecipeAdapter.RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_bake, parent,  false);
        RecipeHolder recipeHolder = new RecipeHolder(view);
        return recipeHolder;
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.RecipeHolder holder, int position) {
        Recipe recipe = mListRecipe.get(position);

        holder.mTextViewName.setText(recipe.getName());

        if (!TextUtils.isEmpty(recipe.getImage())){
            Glide.with(mContext)
                    .load(recipe.getImage())
                    .into(holder.mImageViewThumb);
        } else {
            Glide.with(mContext)
                    .load(R.drawable.dummy_recipe_preview)
                    .into(holder.mImageViewThumb);
        }
    }

    @Override
    public int getItemCount() {
        return mListRecipe != null ? mListRecipe.size():0;
    }
}
