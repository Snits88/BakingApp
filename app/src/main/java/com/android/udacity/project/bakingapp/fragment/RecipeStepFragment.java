package com.android.udacity.project.bakingapp.fragment;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.udacity.project.bakingapp.R;
import com.android.udacity.project.bakingapp.model.Recipe;
import com.android.udacity.project.bakingapp.model.Step;
import com.android.udacity.project.bakingapp.utils.BakingAppConstants;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;


public class RecipeStepFragment extends Fragment {

    private AssetManager am;
    private Typeface tfDescription;
    private Typeface tfTitle;

    private Recipe recipe;
    private Step clickedStep;

    private boolean mTwoPane;
    private TextView title;
    private SimpleExoPlayerView playerView;
    private TextView description;
    private Button pBtnStep;
    private Button nBtnStep;
    private SimpleExoPlayer player;
    private long curPosition;
    private MediaSource mediaSource;

    public RecipeStepFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Font for Title and Description
        am = getContext().getAssets();
        tfDescription = Typeface.createFromAsset(am, "fonts/GarmentDistrict-Regular.otf");
        tfTitle = Typeface.createFromAsset(am, "fonts/GarmentDistrict-Regular.otf");
        //Get Recipe and Selectd Step
        recipe = getArguments().getParcelable(BakingAppConstants.RECIPE);
        clickedStep = getArguments().getParcelable(BakingAppConstants.STEP);
        mTwoPane = getArguments().getBoolean(BakingAppConstants.TWO_PANE);
        // Menage  player for Video
        buildSimpleExoPlayer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        // Retrieve all View
        title = rootView.findViewById(R.id.recipe_title_detail);
        playerView = rootView.findViewById(R.id.playerView);
        description = rootView.findViewById(R.id.recipe_step_description);
        pBtnStep = rootView.findViewById(R.id.previous_step_btn);
        nBtnStep = rootView.findViewById(R.id.next_step_btn);
        // Populate Title
        title.setTypeface(tfTitle);
        title.setText(getString(R.string.no_content_title));
        if(!StringUtils.isEmpty(clickedStep.getShortDescription())){
            title.setText(clickedStep.getShortDescription());
        }
        // Populate Player
        Bitmap icon = BitmapFactory.decodeResource(this.getResources(), R.drawable.no_video);
        playerView.setDefaultArtwork(icon);
        // Populate Descritpion
        title.setTypeface(tfTitle);
        description.setText(getString(R.string.no_content_description));
        description.setTypeface(tfDescription);
        if(!StringUtils.isEmpty(clickedStep.getDescription())){
            description.setText(clickedStep.getDescription());
        }
        // Set Buttons
        menageStepButtons();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();

    }


    @Override
    public void onStop() {
        super.onStop();
        if (player != null) {
            curPosition = player.getCurrentPosition();
            player.stop();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (player != null) {
            player.seekTo(curPosition);
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
        }
    }

    private void buildSimpleExoPlayer() {
        if (clickedStep != null && !StringUtils.isEmpty(clickedStep.getVideoURL())) {
            if (player == null) {
                player = ExoPlayerFactory.newSimpleInstance(getActivity(), new DefaultTrackSelector());
            }
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getActivity(), "");
            DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            mediaSource = new ExtractorMediaSource(Uri.parse(clickedStep.getVideoURL()), dataSourceFactory, extractorsFactory, null, null);
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
        }
    }

    private void menageStepButtons() {
        pBtnStep.setVisibility(View.INVISIBLE);
        pBtnStep.setClickable(false);
        nBtnStep.setVisibility(View.INVISIBLE);
        nBtnStep.setClickable(false);
        if(!mTwoPane && recipe != null && !CollectionUtils.isEmpty(recipe.getSteps()) && clickedStep.getId() != null){
            if (clickedStep.getId() > 0) {
                pBtnStep.setVisibility(View.VISIBLE);
                pBtnStep.setClickable(true);
                pBtnStep.setTypeface(tfDescription);
                pBtnStep.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        RecipeStepFragment nextFragment = new RecipeStepFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(BakingAppConstants.RECIPE, recipe);
                        bundle.putParcelable(BakingAppConstants.STEP, recipe.getSteps().get((clickedStep.getId()-1)));
                        bundle.putBoolean(BakingAppConstants.TWO_PANE, mTwoPane);
                        nextFragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.recipe_detail_container, nextFragment).commit();
                    }
                });
            }
            if(clickedStep.getId() < (recipe.getSteps().size() - 1)){
                nBtnStep.setVisibility(View.VISIBLE);
                nBtnStep.setClickable(true);
                nBtnStep.setTypeface(tfDescription);
                nBtnStep.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        RecipeStepFragment nextFragment = new RecipeStepFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(BakingAppConstants.RECIPE, recipe);
                        bundle.putParcelable(BakingAppConstants.STEP, recipe.getSteps().get((clickedStep.getId()+1)));
                        nextFragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.recipe_detail_container, nextFragment).commit();
                    }
                });
            }
        }
    }
}
