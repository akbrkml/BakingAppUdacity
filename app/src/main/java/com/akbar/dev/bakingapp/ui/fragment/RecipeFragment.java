package com.akbar.dev.bakingapp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akbar.dev.bakingapp.R;

import butterknife.BindView;

/**
 * Created by akbar on 24/08/17.
 */

public class RecipeFragment extends Fragment {

    private static final String TAG = RecipeFragment.class.getSimpleName();

    @BindView(R.id.recycler_view_bake)
    RecyclerView mRecyclerViewBake;
    @BindView(R.id.refresh)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.tv_message_display)
    TextView mTextViewMessage;

    public RecipeFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        return view;
    }

}
