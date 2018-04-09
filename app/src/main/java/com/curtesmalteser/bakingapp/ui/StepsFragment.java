package com.curtesmalteser.bakingapp.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.curtesmalteser.bakingapp.R;
import com.curtesmalteser.bakingapp.data.InjectorUtils;
import com.curtesmalteser.bakingapp.data.model.Step;
import com.curtesmalteser.bakingapp.viewmodel.DetailsActivityViewModel;
import com.curtesmalteser.bakingapp.viewmodel.DetailsActivityViewModelFactory;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsFragment extends Fragment
        implements Player.EventListener {

    public StepsFragment() {
        // Required empty public constructor
    }

    private static final String TAG = StepsFragment.class.getSimpleName();

    @BindView(R.id.playerView)
    PlayerView mPlayerView;

    @BindView(R.id.stepDescription)
    TextView stepDescription;

    @BindView(R.id.my_exo_prev)
    ImageButton exoPrev;

    @BindView(R.id.my_exo_next)
    ImageButton exoNext;

    @BindView(R.id.placeHolderOnMovieError)
    ImageView placeHolderOnMovieError;

    private SimpleExoPlayer mExoPlayer;

    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    private int numberOfSteps;
    private int currentStep;
    List<Step> steps = new ArrayList<>();

    private DetailsActivityViewModel mViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ui_step, container, false);
        releasePlayer();

        ButterKnife.bind(this, v);

        DetailsActivityViewModelFactory factory = InjectorUtils.provideDetailsActivityViewModelFactory(getActivity().getApplicationContext());
        mViewModel = ViewModelProviders.of(getActivity(), factory).get(DetailsActivityViewModel.class);

        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.ic_pastry_cake));

        mMediaSession = new MediaSessionCompat(getActivity().getApplicationContext(), TAG);
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);

        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                );

        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new MySessionCallback());
        mMediaSession.setActive(true);

        mViewModel.getRecipeById().observe(StepsFragment.this, fullRecipes -> {
            numberOfSteps = fullRecipes.stepList.size() - 1;
            steps = fullRecipes.stepList;

            mViewModel.getStepScreen().observe(StepsFragment.this, position -> {
                {
                    if (position != null) {
                        currentStep = position;
                        if (position == 0) {
                            exoPrev.setVisibility(View.INVISIBLE);
                            exoNext.setVisibility(View.VISIBLE);
                        }
                        if (position == numberOfSteps) {
                            exoNext.setVisibility(View.INVISIBLE);
                            exoPrev.setVisibility(View.VISIBLE);
                        }

                        if (steps.get(position).getVideoURL() != null && !steps.get(position).getVideoURL().equals("")) {
                            mPlayerView.setVisibility(View.VISIBLE);
                            placeHolderOnMovieError.setVisibility(View.INVISIBLE);
                            initializePlayer(steps.get(position).getVideoURL());
                        } else {
                            releasePlayer();

                            placeHolderOnMovieError.setVisibility(View.VISIBLE);
                            mPlayerView.setVisibility(View.INVISIBLE);
                            if (steps.get(position).getThumbnailURL() != null && !steps.get(position).getThumbnailURL().equals("")) {
                                Picasso.with(getContext())
                                        .load(steps.get(position).getThumbnailURL())
                                        .networkPolicy(NetworkPolicy.OFFLINE)
                                        .into(placeHolderOnMovieError, new Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError() {
                                                //Try again online if cache failed
                                                Picasso.with(getContext())
                                                        .load(steps.get(position).getThumbnailURL())
                                                        .error(R.drawable.ic_pastry_cake)
                                                        .into(placeHolderOnMovieError, new Callback() {
                                                            @Override
                                                            public void onSuccess() {

                                                            }

                                                            @Override
                                                            public void onError() {
                                                                Log.v("Picasso", "Could not fetch image");
                                                            }
                                                        });
                                            }
                                        });

                            } else {
                                placeHolderOnMovieError.setImageResource(R.drawable.ic_pastry_cake);
                            }

                        }
                        stepDescription.setText(steps.get(position).getDescription());
                    }
                }
            });

        });

        exoPrev.setOnClickListener(v1 -> skipToPrevious());

        exoNext.setOnClickListener(v1 -> skipToNext());

        return v;
    }

    private void skipToPrevious() {
        if (currentStep != 0) {
            currentStep--;
            mViewModel.setStepScreen(currentStep);
            if (currentStep == 0) exoPrev.setVisibility(View.INVISIBLE);
        }
        if (exoNext.getVisibility() == View.INVISIBLE) exoNext.setVisibility(View.VISIBLE);
    }

    private void skipToNext() {
        if (currentStep < numberOfSteps) {
            currentStep++;
            mViewModel.setStepScreen(currentStep);
            if (currentStep == numberOfSteps) exoNext.setVisibility(View.INVISIBLE);
        }
        if (exoPrev.getVisibility() == View.INVISIBLE) exoPrev.setVisibility(View.VISIBLE);
    }

    private void initializePlayer(String url) {
        if (mExoPlayer != null) mExoPlayer.release();
            RenderersFactory renderersFactory = new DefaultRenderersFactory(getContext());
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            mExoPlayer.addListener(this);

            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(), getString(R.string.app_name)), bandwidthMeter);
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(url));
            mExoPlayer.prepare(videoSource);
            mViewModel.getVideoPosition().observe(StepsFragment.this, position -> {
                if (position != null && mExoPlayer != null) mExoPlayer.seekTo(position);
            });
            mExoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        mMediaSession.setActive(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        mMediaSession.release();
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == Player.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if (playbackState == Player.STATE_READY) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if (playbackState == Player.STATE_ENDED) {
            mViewModel.setVideoPosition(0);
            skipToNext();
        }

        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {
        Log.d(TAG, "onSeekProcessed: ");
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            Log.d(TAG, "onPlay: ");
            mExoPlayer.setPlayWhenReady(true);

        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
        }
    }

    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mExoPlayer != null) mViewModel.setVideoPosition(mExoPlayer.getContentPosition());
        else mViewModel.setVideoPosition(0);

    }
}
