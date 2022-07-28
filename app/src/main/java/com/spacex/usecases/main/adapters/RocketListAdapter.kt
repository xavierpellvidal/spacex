package com.spacex.usecases.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.spacex.R
import com.spacex.databinding.ItemRocketBinding
import com.spacex.model.data.Rocket

class RocketListAdapter(private val onRocketClick: (Rocket) -> Unit, var context: Context?) : ListAdapter<Rocket, RocketListAdapter.RocketViewHolder>(RocketDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RocketViewHolder {
        return RocketViewHolder.from(parent, context, onRocketClick)
    }

    override fun onBindViewHolder(holder: RocketViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindStyle(item)
    }

    class RocketViewHolder private constructor(private val binding: ItemRocketBinding, var context: Context?, val onRocketClick: (Rocket) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindStyle(rocket: Rocket): Unit = with(binding) {
            //  Fill ui
            binding.txtName.text = rocket.name + " "
            Glide.with(context!!).load(rocket.flickr_images[0]).into(binding.imgProfile)

            binding.txtLocation.text = rocket.country
            binding.txtEngines.text =
                rocket.engines.number.toString() + " " + context!!.getText(R.string.rockets_list_engines)

            if (rocket.active) binding.imgToggle.setColorFilter(
                context!!.resources.getColor(
                    R.color.success,
                    context!!.theme
                )
            )
            else binding.imgToggle.setColorFilter(
                context!!.resources.getColor(
                    R.color.grey600,
                    context!!.theme
                )
            )

            binding.txtLocation.text = rocket.country + " "

            // Item clicked
            itemView.setOnClickListener {
                onRocketClick(rocket)
            }
        }

        companion object {
            fun from(parent: ViewGroup, context: Context?, onRocketClick: (Rocket) -> Unit): RocketViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemRocketBinding.inflate(layoutInflater, parent, false)
                return RocketViewHolder(binding,  context, onRocketClick)
            }
        }
    }
}

class RocketDiffCallback : DiffUtil.ItemCallback<Rocket>() {

    override fun areItemsTheSame(oldItem: Rocket, newItem: Rocket): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Rocket, newItem: Rocket): Boolean {
        return oldItem == newItem
    }

}