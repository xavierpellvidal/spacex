package com.spacex.usecases.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.spacex.R
import com.spacex.databinding.ItemRocketBinding
import com.spacex.model.data.Rocket

class RocketListAdapter(
    private val rockets: MutableList<Rocket>,
    val onRocketClick: (Rocket) -> Unit,
    var context: Context?
) : RecyclerView.Adapter<RocketListAdapter.RocketViewHolderStyle>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RocketViewHolderStyle {
        return RocketViewHolderStyle(
            ItemRocketBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RocketViewHolderStyle, position: Int) {
        holder.bindStyle(rockets[position])
    }

    inner class RocketViewHolderStyle(private val binding: ItemRocketBinding) :
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
    }

    override fun getItemCount(): Int = rockets.size
}
