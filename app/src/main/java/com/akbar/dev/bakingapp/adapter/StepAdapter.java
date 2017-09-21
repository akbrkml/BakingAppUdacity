package com.akbar.dev.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akbar.dev.bakingapp.R;
import com.akbar.dev.bakingapp.model.Recipe;
import com.akbar.dev.bakingapp.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by akbar on 08/09/17.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepHolder> {

    private Context mContext;
    private List<Step> mListSteps;
    private String recipeName;

    private StepItemClickListener stepItemClickListener;

    public interface StepItemClickListener{
        void onStepItemClick(List<Step> stepList, int position, String recipeName);
    }

    public StepAdapter(StepItemClickListener listener){
        this.stepItemClickListener = listener;
    }

    public void setStepData(List<Recipe> mListRecipe, Context context) {
        this.mListSteps = mListRecipe.get(0).getSteps();
        this.recipeName = mListRecipe.get(0).getName();
        this.mContext = context;
        notifyDataSetChanged();
    }

    @Override
    public StepAdapter.StepHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_step, parent,  false);
        StepHolder stepHolder = new StepHolder(view);
        return stepHolder;
    }

    @Override
    public void onBindViewHolder(StepAdapter.StepHolder holder, int position) {
        holder.mTextViewStep.setText(mListSteps.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return mListSteps != null ? mListSteps.size():0;
    }

    public class StepHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.text_view_step)
        TextView mTextViewStep;
        public StepHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getAdapterPosition();
            stepItemClickListener.onStepItemClick(mListSteps, itemPosition, recipeName);
        }
    }
}
