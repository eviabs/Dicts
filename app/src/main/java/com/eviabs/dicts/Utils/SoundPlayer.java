package com.eviabs.dicts.Utils;

// Source: https://github.com/jaydeepw/audio-wife

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eviabs.dicts.R;

import java.io.IOException;
import java.util.ArrayList;

/***
 * A simple audio player wrapper for Android
 ***/
public class SoundPlayer {

    private static final String TAG = SoundPlayer.class.getSimpleName();


    // TODO: externalize the error messages.
    private static final String ERROR_PLAYVIEW_NULL = "Play view cannot be null";


    private MediaPlayer mMediaPlayer;
    private Context mContext;
    private View mPlayButton;
    private View mPauseButton;
    private View mProgressBar;
    private Uri mUri;
    private boolean isBufferred;
    private boolean mHasDefaultUi;

    /****
     * Array to hold custom completion listeners
     ****/
    private ArrayList<OnCompletionListener> mCompletionListeners = new ArrayList<OnCompletionListener>();

    private ArrayList<View.OnClickListener> mPlayListeners = new ArrayList<View.OnClickListener>();

    private ArrayList<View.OnClickListener> mPauseListeners = new ArrayList<View.OnClickListener>();

    public SoundPlayer(Context context, Uri uri, ViewGroup playerContainer) {

        if (uri == null) {
            throw new IllegalArgumentException("Uri cannot be null");
        }

        if (context == null) {
            throw new IllegalArgumentException("context cannot be null");
        }

        this.mUri = uri;
        this.mContext = context;
        this.isBufferred = false;
        useDefaultUi(playerContainer, LayoutInflater.from(mContext));
        initPlayer(context);

    }


    /***
     * Starts playing audio file associated. Before playing the audio, visibility of appropriate UI
     * controls is made visible. Calling this method has no effect if the audio is already being
     * played.
     ****/
    public void play() {

        // if play button itself is null, the whole purpose of SoundPlayer is
        // defeated.
        if (mPlayButton == null) {
            throw new IllegalStateException(ERROR_PLAYVIEW_NULL);
        }

        if (mUri == null) {
            throw new IllegalStateException("Uri cannot be null. Call init() before calling this method");
        }

        if (mMediaPlayer == null) {
            throw new IllegalStateException("Call init() before calling this method");
        }

        if (mMediaPlayer.isPlaying()) {
            return;
        }

        setBuffering();

        if (!isBufferred) {
            try {
                mMediaPlayer.setDataSource(mContext, mUri);
                mMediaPlayer.prepareAsync(); // might take long! (for buffering, etc)

            } catch (IOException e) {
                e.printStackTrace();
            }

            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer player) {
                    isBufferred = true;
                    setViewsVisibility();
                    mMediaPlayer.start();
                    setPausable();
                    mMediaPlayer.setOnCompletionListener(mOnCompletion);
                }
            });
        } else {

        // enable visibility of all UI controls.
        setViewsVisibility();

        mMediaPlayer.start();

        setPausable();
        }
    }

    /**
     * Ensure the views are visible before playing the audio.
     */
    private void setViewsVisibility() {

        if (mPlayButton != null) {
            mPlayButton.setVisibility(View.VISIBLE);
        }

        if (mPauseButton != null) {
            mPauseButton.setVisibility(View.VISIBLE);
        }
    }

    /***
     * Pause the audio being played. Calling this method has no effect if the audio is already
     * paused
     */
    public void pause() {

        if (mMediaPlayer == null) {
            return;
        }

        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            setPlayable();
        }
    }

    /***
     * Changes audiowife state to enable play functionality.
     */
    private void setPlayable() {
        if (mPlayButton != null) {
            mPlayButton.setVisibility(View.VISIBLE);
        }

        if (mPauseButton != null) {
            mPauseButton.setVisibility(View.GONE);
        }

        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    /****
     * Changes audio wife to enable pause functionality.
     */
    private void setPausable() {
        if (mPlayButton != null) {
            mPlayButton.setVisibility(View.GONE);
        }

        if (mPauseButton != null) {
            mPauseButton.setVisibility(View.VISIBLE);
        }

        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    /***
     * Sets the audio play functionality on click event of this view. You can set {@link android.widget.Button} or
     * an {@link android.widget.ImageView} as audio play control
     ****/
    public SoundPlayer setPlayView(View play) {

        if (play == null) {
            throw new NullPointerException("PlayView cannot be null");
        }

        if (mHasDefaultUi) {
            Log.w(TAG, "Already using default UI. Setting play view will have no effect");
            return this;
        }

        mPlayButton = play;

        initOnPlayClick();
        return this;
    }

    private void initOnPlayClick() {
        if (mPlayButton == null) {
            throw new NullPointerException(ERROR_PLAYVIEW_NULL);
        }

        // add default click listener to the top
        // so that it is the one that gets fired first
        mPlayListeners.add(0, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                play();
            }
        });

        // Fire all the attached listeners
        // when the play button is clicked
        mPlayButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                for (View.OnClickListener listener : mPlayListeners) {
                    listener.onClick(v);
                }
            }
        });
    }

    /***
     * Sets the audio pause functionality on click event of the view passed in as a parameter. You
     * can set {@link android.widget.Button} or an {@link android.widget.ImageView} as audio pause control. Audio pause
     * functionality will be unavailable if this method is not called.
     ****/
    public SoundPlayer setPauseView(View pause) {

        if (pause == null) {
            throw new NullPointerException("PauseView cannot be null");
        }

        if (mHasDefaultUi) {
            Log.w(TAG, "Already using default UI. Setting pause view will have no effect");
            return this;
        }

        mPauseButton = pause;

        initOnPauseClick();
        return this;
    }

    private void initOnPauseClick() {
        if (mPauseButton == null) {
            throw new NullPointerException("Pause view cannot be null");
        }

        // add default click listener to the top
        // so that it is the one that gets fired first
        mPauseListeners.add(0, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pause();
            }
        });

        // Fire all the attached listeners
        // when the pause button is clicked
        mPauseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                for (View.OnClickListener listener : mPauseListeners) {
                    listener.onClick(v);
                }
            }
        });
    }

    public SoundPlayer setProgressView(View progress) {

        if (progress == null) {
            throw new NullPointerException("ProgressView cannot be null");
        }

//        if (mHasDefaultUi) {
//            Log.w(TAG, "Already using default UI. Setting pause view will have no effect");
//            return this;
//        }

        mProgressBar = progress;

        return this;
    }

    /****
     * Add custom playback completion listener. Adding multiple listeners will queue up all the
     * listeners and fire them on media playback completes.
     */
    public SoundPlayer addOnCompletionListener(OnCompletionListener listener) {

        // add default click listener to the top
        // so that it is the one that gets fired first
        mCompletionListeners.add(0, listener);

        return this;
    }

    /****
     * Add custom play view click listener. Calling this method multiple times will queue up all the
     * listeners and fire them all together when the event occurs.
     ***/
    public SoundPlayer addOnPlayClickListener(View.OnClickListener listener) {

        mPlayListeners.add(listener);

        return this;
    }

    /***
     * Add custom pause view click listener. Calling this method multiple times will queue up all
     * the listeners and fire them all together when the event occurs.
     ***/
    public SoundPlayer addOnPauseClickListener(View.OnClickListener listener) {

        mPauseListeners.add(listener);

        return this;
    }

    /****
     * Initialize and prepare the audio player
     ****/
    private void initPlayer(final Context ctx) {

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

    }

    private void setBuffering() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        if (mPlayButton != null) {
            mPlayButton.setVisibility(View.GONE);
        }

        if (mPauseButton != null) {
            mPauseButton.setVisibility(View.GONE);
        }
    }



    private OnCompletionListener mOnCompletion = new OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {
            // set UI when audio finished playing

            setPlayable();
            // ensure that our completion listener fires first.
            // This will provide the developer to over-ride our
            // completion listener functionality

            fireCustomCompletionListeners(mp);
        }
    };

    private void fireCustomCompletionListeners(MediaPlayer mp) {
        for (OnCompletionListener listener : mCompletionListeners) {
            listener.onCompletion(mp);
        }
    }


    public void useDefaultUi(ViewGroup playerContainer, LayoutInflater inflater) {
        if (playerContainer == null) {
            throw new NullPointerException("Player container cannot be null");
        }

        if (inflater == null) {
            throw new IllegalArgumentException("Inflater cannot be null");
        }

        View playerUi = playerContainer;

        if (playerContainer.findViewById(R.id.player_layout) == null) {
            playerUi = inflater.inflate(R.layout.sound_player_layout, playerContainer);
        }

        // init play view
        View playView = playerUi.findViewById(R.id.player_play);
        setPlayView(playView);

        // init pause view
        View pauseView = playerUi.findViewById(R.id.player_pause);
        setPauseView(pauseView);

        // init pause view
        View progressView = playerUi.findViewById(R.id.player_progress_bar);
        setProgressView(progressView);

        // this has to be set after all the views
        // have finished initializing.
        mHasDefaultUi = true;
    }

    /***
     * Releases the allocated resources.
     *
     * */
    public void release() {

        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}