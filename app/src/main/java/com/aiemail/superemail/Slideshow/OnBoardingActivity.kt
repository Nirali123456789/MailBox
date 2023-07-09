package com.aiemail.superemail.Slideshow


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.aiemail.superemail.Activities.LoginActivity
import com.aiemail.superemail.Activities.MainActivity
import com.aiemail.superemail.R
import com.aiemail.superemail.databinding.OnboardingActivityBinding
import com.aiemail.superemail.prefs
import com.aiemail.superemail.startup.OnBoardingPrefManager
import com.aiemail.superemail.utilis.Helpers
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory


class OnBoardingActivity : AppCompatActivity() {
    lateinit var binding: OnboardingActivityBinding
    var mVideoCompleted = false
    private lateinit var prefManager: OnBoardingPrefManager
    private var exoPlayer: SimpleExoPlayer? = null
    private lateinit var playerView: PlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        Helpers.SetUpFullScreen(window)
        super.onCreate(savedInstanceState)
        binding = OnboardingActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        SetUpVideoView()
        prefs.intExamplePref = true
    }


    private fun SetUpVideoView() {

        prefManager = OnBoardingPrefManager(this)
        // Create a SimpleExoPlayer instance
        val trackSelector = DefaultTrackSelector(this)
        var exoPlayer = SimpleExoPlayer.Builder(this).setTrackSelector(trackSelector).build()
        playerView = binding.videoView
// Attach the player to your SimpleExoPlayerView
        binding.videoView.player = exoPlayer




        val videoUri = Uri.parse(
            "android.resource://" + packageName + "/" +
                    R.raw.demo
        )

        exoPlayer = SimpleExoPlayer.Builder(this)
            .setTrackSelector(DefaultTrackSelector(this))
            .setLoadControl(DefaultLoadControl())
            .build()
        playerView.player = exoPlayer
        val userAgent = System.getProperty("http.agent")
        val dataSourceFactory = DefaultDataSourceFactory(this, userAgent)
        val mediaItem = MediaItem.fromUri(videoUri)
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mediaItem)

        exoPlayer?.prepare(mediaSource)
        exoPlayer?.playWhenReady = true
        exoPlayer?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    // Media playback completed
                    // Add your logic here
                    mVideoCompleted = true
                    binding.startBtn.visibility = View.VISIBLE
                    binding.startBtn.setOnClickListener {
                        prefs.intExamplePref = true
                        setFirstTimeLaunchToFalse()
                        if (prefs.islogin) {
                            startActivity(Intent(this@OnBoardingActivity, MainActivity::class.java))
                            finish()
                        } else {
                            startActivity(Intent(this@OnBoardingActivity, LoginActivity::class.java))
                            finish()
                        }
                    }
                }
            }
        })


//        videoView.setOnCompletionListener { mediaPlayer ->
//            mVideoCompleted = true
//            binding.startBtn.visibility = View.VISIBLE
//            binding.startBtn.setOnClickListener {
//                prefs.intExamplePref = true
//                setFirstTimeLaunchToFalse()
//                if (prefs.islogin) {
//                    startActivity(Intent(this, MainActivity::class.java))
//                    finish()
//                } else {
//                    startActivity(Intent(this, LoginActivity::class.java))
//                    finish()
//                }
//            }
//        }


    }

    private fun setFirstTimeLaunchToFalse() {
        prefManager.isFirstTimeLaunch = false
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun releasePlayer() {
        exoPlayer?.release()
        exoPlayer = null
    }
}
