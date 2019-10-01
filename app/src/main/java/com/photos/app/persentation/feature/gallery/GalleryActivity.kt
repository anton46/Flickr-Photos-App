package com.photos.app.persentation.feature.gallery

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.photos.app.R
import com.photos.app.common.providePhotoModelMapper
import com.photos.app.common.providePhotoRepository
import com.photos.app.domain.loader.SimplePhotoLoader
import com.photos.app.persentation.feature.gallery.viewmodel.GalleryViewModel
import com.photos.app.persentation.feature.gallery.viewmodel.GalleryViewModel.ViewState
import com.photos.app.persentation.view.provideViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.loading_layout.*

class GalleryActivity : AppCompatActivity() {

    private val adapter = GalleryAdapter()
    private lateinit var viewModel: GalleryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initViewModels()
        observeStates()
    }

    private fun initViewModels() {
        viewModel = provideViewModel {
            GalleryViewModel(
                providePhotoRepository(),
                this,
                providePhotoModelMapper()
            )
        }
    }

    private fun observeStates() {
        viewModel.states.observe(this, Observer { render(it) })
    }

    private fun render(viewState: ViewState) {
        when (viewState) {
            is ViewState.Result -> {
                if (viewState.photos.isNotEmpty()) {
                    onSucceed()
                    adapter.addPhotos(viewState.photos)
                } else {
                    onNoResult()
                }
            }
            is ViewState.Error -> onError()
            is ViewState.Loading -> onLoading()
            is ViewState.LoadMore -> onLoadMore()
        }
    }

    private fun search(query: String) {
        SimplePhotoLoader.instance.clear()
        adapter.clear()
        viewModel.search(query)
    }

    private fun onLoading() {
        loading_view.visibility = View.VISIBLE
        feed.visibility = View.GONE
        error_view.visibility = View.GONE
    }

    private fun onError() {
        error_view.visibility = View.VISIBLE
        loading_view.visibility = View.GONE
        feed.visibility = View.GONE
    }

    private fun onSucceed() {
        feed.visibility = View.VISIBLE
        loading_view.visibility = View.GONE
        error_view.visibility = View.GONE
    }

    private fun onLoadMore() {
        Snackbar.make(feed, R.string.loading_more_photos, Snackbar.LENGTH_SHORT).show()
    }

    private fun onNoResult() {
        error_view.text = getString(R.string.search_not_found)
    }

    private fun initView() {
        search_input.requestFocus()
        search_input.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                search()
                handled = true
            }
            handled
        }

        search_button.setOnClickListener {
            search()
        }

        feed.setHasFixedSize(true)
        feed.layoutManager = GridLayoutManager(this, 3)
        feed.adapter = adapter
        feed.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.nextPage()
                }
            }
        })
    }

    private fun search() {
        search(search_input.text.toString())
        hideKeyboard()
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroy() {
        SimplePhotoLoader.instance.clear()
        super.onDestroy()
    }

}
