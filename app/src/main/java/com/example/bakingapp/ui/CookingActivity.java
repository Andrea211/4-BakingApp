package com.example.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentManager;

import com.example.bakingapp.R;
import com.example.bakingapp.adapters.StepsAdapter;
import com.example.bakingapp.models.Step;
import com.example.bakingapp.utils.ConstantsUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CookingActivity extends AppCompatActivity implements View.OnClickListener
        , StepsAdapter.OnStepClick {

    private static final String STEP_LIST_STATE = "step_list_state";
    private static final String STEP_NUMBER_STATE = "step_number_state";
    private static final String STEP_LIST_JSON_STATE = "step_list_json_state";

    @BindView(R.id.btn_next_step)
    Button mButtonNextStep;

    @BindView(R.id.btn_previous_step)
    Button mButtonPreviousStep;

    private ArrayList<Step> mStepArrayList = new ArrayList<>();
    private String mJsonResult;
    private int mVideoNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking);

        // Navigation to go up
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(ConstantsUtil.STEP_INTENT_EXTRA)) {
                mStepArrayList = getIntent().getParcelableArrayListExtra(ConstantsUtil.STEP_INTENT_EXTRA);
            }
            if (intent.hasExtra(ConstantsUtil.JSON_RESULT_EXTRA)) {
                mJsonResult = getIntent().getStringExtra(ConstantsUtil.JSON_RESULT_EXTRA);
            }
        }

        if (savedInstanceState == null) {
            playVideo(mStepArrayList.get(mVideoNumber));
        }

        ButterKnife.bind(this);

        handleUiForDevice();
    }

    // Initialize playVideo
    private void playVideo(Step step) {
        VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
        Bundle stepsBundle = new Bundle();
        stepsBundle.putParcelable(ConstantsUtil.STEP_SINGLE, step);
        videoPlayerFragment.setArguments(stepsBundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fl_player_container, videoPlayerFragment)
                .addToBackStack(null)
                .commit();
    }

    // Initialize playVideoReplace
    private void playVideoReplace(Step step) {
        VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
        Bundle stepsBundle = new Bundle();
        stepsBundle.putParcelable(ConstantsUtil.STEP_SINGLE, step);
        videoPlayerFragment.setArguments(stepsBundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_player_container, videoPlayerFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STEP_LIST_STATE, mStepArrayList);
        outState.putString(STEP_LIST_JSON_STATE, mJsonResult);
        outState.putInt(STEP_NUMBER_STATE, mVideoNumber);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Handle buttons clicking - navigation between steps
    @Override
    public void onClick(View v) {

        //if it's last step show - cooking is over
        if (mVideoNumber == mStepArrayList.size() - 1) {
            Toast.makeText(this, R.string.cooking_is_over, Toast.LENGTH_SHORT).show();
        } else {
            if (v.getId() == mButtonPreviousStep.getId()) {
                mVideoNumber--;
                if (mVideoNumber < 0) {
                    Toast.makeText(this, R.string.you_better_see_next_step, Toast.LENGTH_SHORT).show();
                } else
                    playVideoReplace(mStepArrayList.get(mVideoNumber));
            } else if (v.getId() == mButtonNextStep.getId()) {
                mVideoNumber++;
                playVideoReplace(mStepArrayList.get(mVideoNumber));
            }
        }
    }

    @Override
    public void onStepClick(int position) {
        mVideoNumber = position;
        playVideoReplace(mStepArrayList.get(position));
    }

    private void handleUiForDevice() {
        // Set button listeners
        mButtonNextStep.setOnClickListener(this);
        mButtonPreviousStep.setOnClickListener(this);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mStepArrayList = savedInstanceState.getParcelableArrayList(STEP_LIST_STATE);
            mJsonResult = savedInstanceState.getString(STEP_LIST_JSON_STATE);
            mVideoNumber = savedInstanceState.getInt(STEP_NUMBER_STATE);
        }
    }

}
