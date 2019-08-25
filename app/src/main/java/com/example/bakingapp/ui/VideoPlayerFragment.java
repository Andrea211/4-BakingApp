package com.example.bakingapp.ui;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bakingapp.R;
import com.example.bakingapp.models.Step;
import com.example.bakingapp.utils.ConstantsUtil;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoPlayerFragment extends Fragment {

    private static final String STEP_URI = "step_uri";
    private static final String STEP_VIDEO_POSITION = "step_video_position";
    private static final String STEP_PLAY_WHEN_READY = "step_play_when_ready";
    private static final String STEP_SINGLE = "step_single";

    @BindView(R.id.tv_step_title)
    TextView mStepTitle;

    @BindView(R.id.player_view)
    SimpleExoPlayerView mPlayerView;

    @BindView(R.id.tv_step_description)
    TextView mStepDescription;

    @BindView(R.id.iv_video_placeholder)
    ImageView mImageViewPlaceholder;

    private SimpleExoPlayer mSimpleExoPlayer;

    private Step mStep;
    private Uri mVideoUri;
    private boolean mShouldPlayWhenReady = true;
    private long mPlayerPosition;

    public VideoPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.video_player, container, false);

        ButterKnife.bind(this, root);

        if (savedInstanceState != null) {
            mStep = savedInstanceState.getParcelable(STEP_SINGLE);
            mShouldPlayWhenReady = savedInstanceState.getBoolean(STEP_PLAY_WHEN_READY);
            mPlayerPosition = savedInstanceState.getLong(STEP_VIDEO_POSITION);
            mVideoUri = Uri.parse(savedInstanceState.getString(STEP_URI));
        } else {
            if (getArguments() != null) {

                String mVideoThumbnail;
                Bitmap mVideoThumbnailImage;

                mImageViewPlaceholder.setVisibility(View.GONE);
                mPlayerView.setVisibility(View.VISIBLE);

                mStep = getArguments().getParcelable(ConstantsUtil.STEP_SINGLE);

                // If there's no video
                if (mStep.getVideoURL().equals("")) {
                    // Check thumbnail
                    if (mStep.getThumbnailURL().equals("")) {
                        // If there's no video or thumbnail, use placeholder image
                        mPlayerView.setUseArtwork(true);
                        mImageViewPlaceholder.setVisibility(View.VISIBLE);
                        mPlayerView.setUseController(false);
                    } else {
                        mImageViewPlaceholder.setVisibility(View.GONE);
                        mPlayerView.setVisibility(View.VISIBLE);
                        mVideoThumbnail = mStep.getThumbnailURL();
                        mVideoThumbnailImage = ThumbnailUtils.createVideoThumbnail(mVideoThumbnail, MediaStore.Video.Thumbnails.MICRO_KIND);
                        mPlayerView.setUseArtwork(true);
                        mPlayerView.setDefaultArtwork(mVideoThumbnailImage);
                    }
                } else {
                    mVideoUri = Uri.parse(mStep.getVideoURL());
                }
            }
        }
        return root;
    }

    private void initializeVideoPlayer(Uri videoUri) {

        mStepDescription.setText(mStep.getDescription());
        mStepDescription.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);

        mStepTitle.setText(mStep.getShortDescription());

        if (mSimpleExoPlayer == null) {

            mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl());

            mPlayerView.setPlayer(mSimpleExoPlayer);

            String userAgent = Util.getUserAgent(getActivity(), getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(videoUri,
                    new DefaultDataSourceFactory(getActivity(), userAgent),
                    new DefaultExtractorsFactory(),
                    null,
                    null);

            if (mPlayerPosition != C.TIME_UNSET) {
                mSimpleExoPlayer.seekTo(mPlayerPosition);
            }

            mSimpleExoPlayer.prepare(mediaSource);
            mSimpleExoPlayer.setPlayWhenReady(mShouldPlayWhenReady);
        }
    }

    private void releasePlayer() {
        if (mSimpleExoPlayer != null) {
            updateStartPosition();
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initializeVideoPlayer(mVideoUri);
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeVideoPlayer(mVideoUri);
        if (mSimpleExoPlayer != null) {
            mSimpleExoPlayer.setPlayWhenReady(mShouldPlayWhenReady);
            mSimpleExoPlayer.seekTo(mPlayerPosition);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSimpleExoPlayer != null) {
            updateStartPosition();
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mSimpleExoPlayer != null) {
            updateStartPosition();
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        updateStartPosition();
        outState.putString(STEP_URI, mStep.getVideoURL());
        outState.putParcelable(STEP_SINGLE, mStep);
        outState.putLong(STEP_VIDEO_POSITION, mPlayerPosition);
        outState.putBoolean(STEP_PLAY_WHEN_READY, mShouldPlayWhenReady);
    }

    private void updateStartPosition() {
        if (mSimpleExoPlayer != null) {
            mShouldPlayWhenReady = mSimpleExoPlayer.getPlayWhenReady();
            mPlayerPosition = mSimpleExoPlayer.getCurrentPosition();
        }
    }
}