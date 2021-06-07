package com.sion.itunes.view.audio

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.sion.itunes.R
import com.sion.itunes.databinding.FragmentAudioBinding
import com.sion.itunes.databinding.FragmentMusicBinding
import com.sion.itunes.model.vo.Music
import com.sion.itunes.view.base.BaseDialogFragment

import timber.log.Timber
import kotlin.math.abs
import kotlin.math.ceil

class AudioDialogFragment(val music: Music) : BaseDialogFragment() {
    override fun isFullLayout() = true
    override fun getLayoutId() = R.layout.fragment_audio

    private lateinit var mediaPlayer: MediaPlayer

    private val mainHandler by lazy { Handler(Looper.getMainLooper()) }
    private lateinit var runnable: Runnable
    private var _binding: FragmentAudioBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAudioBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(requireContext()).load(music.artworkUrl100).placeholder(R.drawable.record)
            .into(binding.ivCover)

        binding.tvArtistName.text = music.artistName
        binding.tvTrackName.text = music.trackName

        binding.ivClose.setOnClickListener { dismiss() }

        binding.ibControl.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                binding.ibControl.setImageResource(R.drawable.play)
                mediaPlayer.pause()
            } else {
                binding.ibControl.setImageResource(R.drawable.pause)
                mediaPlayer.start()
            }
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                takeIf { fromUser }?.run {
                    val ms = mediaPlayer.duration * (progress / 100.toDouble())
                    mediaPlayer.seekTo(ms.toInt())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        runnable = Runnable {
            val progress =
                ((mediaPlayer.currentPosition.toDouble() / mediaPlayer.duration) * 100).toInt()
            binding.seekBar.progress = progress
            binding.tvCurrentTime.text = formatDuration(mediaPlayer.currentPosition)
            mainHandler.postDelayed(runnable, 1000)
        }

        setupAndStartMediaPlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        stopAndRelease(mediaPlayer)
        mainHandler.removeCallbacks(runnable)
    }

    /**
     *  setup [MediaPlayer] and start playing when prepared
     */
    private fun setupAndStartMediaPlayer() {
        mediaPlayer = MediaPlayer().also { mp ->
            mp.setDataSource(music.previewUrl)
            mp.prepareAsync()
            mp.setOnPreparedListener {
                binding.progressBar.visibility = View.GONE
                binding.tvDuration.text = formatDuration(mediaPlayer.duration)
                it.start()
                binding.ibControl.setImageResource(R.drawable.pause)
                mainHandler.post(runnable)
            }
            mp.setOnCompletionListener {
                binding.ibControl.setImageResource(R.drawable.play)
            }
        }
    }

    /**
     * format millisecond to MM:SS
     * @param duration millisecond
     * @return String MM:SS
     */
    private fun formatDuration(duration: Int): String {
        val absSeconds = abs(ceil(duration.toDouble() / 1000).toInt())
        val positive = String.format(
            "%02d:%02d",
            absSeconds / 60,
            absSeconds % 60
        )
        return if (duration < 0) "-$positive" else positive
    }

    /**
     * Stop and release [MediaPlayer]
     */
    private fun stopAndRelease(mediaPlayer: MediaPlayer?) {
        mediaPlayer?.run {
            try {
                if (this.isPlaying) {
                    this.pause()
                    this.stop()
                }
                this.release()
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}