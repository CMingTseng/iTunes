package com.sion.itunes.view.music

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sion.itunes.R
import com.sion.itunes.databinding.ItemMusicBinding
import com.sion.itunes.model.vo.Music
import timber.log.Timber


class MusicViewHolder(private val itemBinding: ItemMusicBinding) : RecyclerView.ViewHolder(itemBinding.root) {
    private val clRoot: ConstraintLayout = itemBinding.clRoot
    private val ivCover: ImageView = itemBinding.ivCover
    private val tvArtistName: TextView = itemBinding.tvArtistName
    private val tvTrackName: TextView =itemBinding.tvTrackName

    fun onBind(item: Music, musicFuncItem: MusicFuncItem) {
        clRoot.setOnClickListener { musicFuncItem.onMusicItemClick.invoke(item) }
        Glide.with(ivCover.context).load(item.artworkUrl100).placeholder(R.drawable.record)
            .into(ivCover)
        tvArtistName.text = item.artistName
        tvTrackName.text = item.trackName
    }
}