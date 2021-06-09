package com.sion.itunes.view.music

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.sion.itunes.R
import com.sion.itunes.databinding.FragmentAudioBinding
import com.sion.itunes.databinding.FragmentMusicBinding
import com.sion.itunes.theme.PagingComposeTheme
import com.sion.itunes.view.audio.AudioDialogFragment
import com.sion.itunes.view.base.BaseFragment
import com.sion.itunes.view.base.footer.BaseLoadStateAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
@ExperimentalPagingApi
class MusicFragment(private val keyword: String = "pop") : BaseFragment() {
    private val viewModel: MusicViewModel by viewModel()
    override fun getLayoutId() = R.layout.fragment_music
    private var _binding: FragmentMusicBinding? = null
    private val binding get() = _binding!!

    private val musicFuncItem = MusicFuncItem(
        onMusicItemClick = { music ->
            AudioDialogFragment(music).show(
                requireActivity().supportFragmentManager,
                AudioDialogFragment::class.java.simpleName
            )
        }
    )

    private val loadStateListener = { loadStatus: CombinedLoadStates ->
        when (loadStatus.refresh) {
            is LoadState.Error -> {
                showDialog()
                binding.progressBar.visibility = View.GONE
            }
            is LoadState.Loading -> binding.progressBar.visibility = View.VISIBLE
            is LoadState.NotLoading -> {
                binding.progressBar.visibility = View.GONE
            }
        }
        when (loadStatus.append) {
            is LoadState.Error -> {
            }
            is LoadState.Loading -> {
            }
            is LoadState.NotLoading -> {
            }
        }
    }

    private val musicAdapter by lazy {
        val adapter = MusicAdapter(musicFuncItem)
        adapter.addLoadStateListener(loadStateListener)
        adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMusicBinding.inflate(inflater, container, false)
        val view = binding.root
        return ComposeView(requireContext()).apply {
            setContent {
                PagingComposeTheme {
                   MusicScreen(keyword,viewModel)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvMusic.takeIf { it.adapter == null }?.let {
            it.adapter = musicAdapter.withLoadStateFooter(BaseLoadStateAdapter())
            search()
        }
    }

    private fun search(key: String = keyword) {
        lifecycleScope.launch {
            viewModel.search(key, true).collectLatest {
                musicAdapter.submitData(it)
            }
        }
    }
}