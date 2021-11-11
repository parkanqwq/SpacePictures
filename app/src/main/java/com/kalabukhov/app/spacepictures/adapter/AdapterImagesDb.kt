package com.kalabukhov.app.spacepictures.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kalabukhov.app.spacepictures.R
import com.kalabukhov.app.spacepictures.databinding.ItemImageSpaceBinding
import com.kalabukhov.app.spacepictures.domain.entity.ImageSpaceDbEntity
import com.kalabukhov.app.spacepictures.ui.profile.Profile
import com.squareup.picasso.Picasso

class AdapterImagesDb(
    private var onItemViewClickListener: Profile.OnItemViewClickListener
) : RecyclerView.Adapter<AdapterImagesDb.MainViewHolder>() {

    private var imageSpaceDbEntity: List<ImageSpaceDbEntity> = listOf()

    fun setImagesDbEntity(data: List<ImageSpaceDbEntity>) {
        imageSpaceDbEntity = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = MainViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_space, parent, false) as View
    )

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(imageSpaceDbEntity[position])
    }

    override fun getItemCount(): Int = imageSpaceDbEntity.size

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemImageSpaceBinding.bind(view)
        fun bind(imageSpaceDbEntity: ImageSpaceDbEntity) = with(binding) {
            Picasso.get()
                .load(imageSpaceDbEntity.url)
                .placeholder(R.drawable.ic_no_photo_vector)
                .error(R.drawable.ic_load_error_vector)
                .into(imageView)
            root.setOnClickListener {
                onItemViewClickListener.onItemViewClick(imageSpaceDbEntity)
            }
        }
    }
}