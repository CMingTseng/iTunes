package com.sion.itunes.view.base.footer

import android.view.View
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView

class NetworkStateItemViewHolder(view: View,
                                 private val retryCallback: () -> Unit = {}
) : RecyclerView.ViewHolder(view) {
//    private val progressBar = view.progress_bar
//    private val errorMsg = view.error_msg
//    private val retry = view.error_msg.also { it.setOnClickListener { retryCallback() } }

    fun onBind(loadState: LoadState) {}
}