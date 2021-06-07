package com.sion.itunes.view.main

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import androidx.paging.ExperimentalPagingApi
import com.sion.itunes.R
import com.sion.itunes.databinding.ActivityMainBinding
import com.sion.itunes.databinding.FragmentMusicBinding
import com.sion.itunes.view.base.BaseActivity
import com.sion.itunes.view.music.MusicFragment
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
@ExperimentalPagingApi
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView( view)

        navigateTo(MusicFragment())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val searchView = (menu?.findItem(R.id.search)?.actionView as SearchView)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.run { navigateTo(MusicFragment(this)) }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })

            setOnCloseListener {
                navigateTo(MusicFragment())
                false
            }
        }
        return true
    }

}