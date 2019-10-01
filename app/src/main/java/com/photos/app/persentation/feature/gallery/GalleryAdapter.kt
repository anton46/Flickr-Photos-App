package com.photos.app.persentation.feature.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.photos.app.R
import com.photos.app.common.provideLoadPhotosRepository
import com.photos.app.data.network.core.UrlProvider
import com.photos.app.domain.loader.SimplePhotoLoader
import com.photos.app.domain.model.PhotoModel
import com.photos.app.persentation.view.photoSize
import kotlin.math.max

class GalleryAdapter : RecyclerView.Adapter<GalleryAdapter.MovieItemViewHolder>() {
    companion object {
        const val GRID = 3
    }

    private val items: MutableList<PhotoModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): MovieItemViewHolder {
        val context = parent.context
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false)
        val params = RecyclerView.LayoutParams(context.photoSize(GRID), context.photoSize(
            GRID
        ))
        itemView.layoutParams = params
        return MovieItemViewHolder(
            itemView
        )
    }

    fun addPhotos(movies: List<PhotoModel>) {
        items.addAll(movies)
        notifyItemRangeInserted(max(0, items.size - movies.size), movies.size)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        holder.clear()
        holder.bindView(items[position])
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    class MovieItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val photo = itemView.findViewById<ImageView>(R.id.photo)
        private val placeholder = itemView.findViewById<View>(R.id.place_holder)

        fun bindView(item: PhotoModel) {
            SimplePhotoLoader
                .instance
                .loadPhoto(
                    item.buildUrl(),
                    photo,
                    placeholder
                )
        }

        fun clear() {
            photo.visibility = View.GONE
            placeholder.visibility = View.VISIBLE
        }

        private fun PhotoModel.buildUrl() = UrlProvider().createLoadImageUrl(this)
    }
}