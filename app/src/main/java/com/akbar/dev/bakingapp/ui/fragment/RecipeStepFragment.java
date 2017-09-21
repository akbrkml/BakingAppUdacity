package com.akbar.dev.bakingapp.ui.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akbar.dev.bakingapp.R;
import com.akbar.dev.bakingapp.model.Recipe;
import com.akbar.dev.bakingapp.model.Step;
import com.akbar.dev.bakingapp.ui.activity.RecipeDetailActivity;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.akbar.dev.bakingapp.ui.activity.RecipeActivity.SELECTED_INDEX;
import static com.akbar.dev.bakingapp.ui.activity.RecipeActivity.SELECTED_RECIPES;
import static com.akbar.dev.bakingapp.ui.activity.RecipeActivity.SELECTED_STEPS;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeStepFragment extends Fragment {

    @BindView(R.id.playerView)SimpleExoPlayerView mPlayerView;
    @BindView(R.id.previousStep)Button mButtonPrevious;
    @BindView(R.id.nextStep)Button mButtonNext;
    @BindView(R.id.recipe_step_detail_text)TextView mTextStep;
    @BindView(R.id.thumbImage)ImageView mImageThumb;

    private SimpleExoPlayer player;
    private BandwidthMeter bandwidthMeter;
    private ArrayList<Step> steps = new ArrayList<>();
    private int selectedIndex;
    private Handler mainHandler;
    private ArrayList<Recipe> recipe;
    private String recipeName, videoUrl, imageUrl;

    private StepItemClickListener stepItemClickListener;

    public interface StepItemClickListener{
        void onStepItemClick(List<Step> stepList, int position, String recipeName);
    }

    public RecipeStepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_step, container, false);

        ButterKnife.bind(this, view);

        getBundle(savedInstanceState);

        return view;
    }

    private void bindData(){
        mTextStep.setText(steps.get(selectedIndex).getDescription());
        mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        videoUrl = steps.get(selectedIndex).getVideoURL();
        imageUrl = steps.get(selectedIndex).getThumbnailURL();

        if (imageUrl != "") {
            Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
            Glide.with(getContext()).load(builtUri).into(mImageThumb);
        }

        if (!videoUrl.isEmpty()) {
            initializePlayerView(Uri.parse(steps.get(selectedIndex).getVideoURL()));
        } else {
            player = null;
            mPlayerView.setVisibility(View.GONE);
            mPlayerView.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
        }
    }

    private void navigationStep(){
        stepItemClickListener = (RecipeDetailActivity)getActivity();

        mButtonPrevious.setOnClickListener(view -> {
            if (steps.get(selectedIndex).getId() > 0) {
                if (player!=null){
                    player.stop();
                }
                stepItemClickListener.onStepItemClick(steps,steps.get(selectedIndex).getId() - 1, recipeName);
            }
            else {
                Toast.makeText(getActivity(),"You already are in the First step of the recipe", Toast.LENGTH_SHORT).show();

            }
        });

        mButtonNext.setOnClickListener(view -> {
            int lastIndex = steps.size()-1;
            if (steps.get(selectedIndex).getId() < steps.get(lastIndex).getId()) {
                if (player!=null){
                    player.stop();
                }
                stepItemClickListener.onStepItemClick(steps,steps.get(selectedIndex).getId() + 1, recipeName);
            }
            else {
                Toast.makeText(getContext(),"You already are in the Last step of the recipe", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getBundle(Bundle savedInstanceState){
        recipe = new ArrayList<>();

        if (savedInstanceState != null) {
            steps = savedInstanceState.getParcelableArrayList(SELECTED_STEPS);
            selectedIndex = savedInstanceState.getInt(SELECTED_INDEX);
            recipeName = savedInstanceState.getString("Title");
        } else {
            steps = getArguments().getParcelableArrayList(SELECTED_STEPS);
            if (steps != null) {
                steps = getArguments().getParcelableArrayList(SELECTED_STEPS);
                selectedIndex = getArguments().getInt(SELECTED_INDEX);
                recipeName = getArguments().getString("Title");
            } else {
                recipe = getArguments().getParcelableArrayList(SELECTED_RECIPES);
                //casting List to ArrayList
                steps = (ArrayList<Step>)recipe.get(0).getSteps();
                selectedIndex = 0;
            }
        }

        bindData();

        navigationStep();
    }

    private void initializePlayerView(Uri mediaUri) {
        mainHandler = new Handler();
        bandwidthMeter = new DefaultBandwidthMeter();
        if (player == null) {
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();

            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(player);

            String userAgent = Util.getUserAgent(getContext(), "Baking App");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList(SELECTED_STEPS,steps);
        currentState.putInt(SELECTED_INDEX,selectedIndex);
        currentState.putString("Title",recipeName);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (player!=null) {
            player.stop();
            player.release();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (player!=null) {
            player.stop();
            player.release();
            player=null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (player!=null) {
            player.stop();
            player.release();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player!=null) {
            player.stop();
            player.release();
        }
    }

}
