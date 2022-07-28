package com.spacex.usecases.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.spacex.R
import com.spacex.databinding.ItemLaunchBinding
import com.spacex.databinding.ItemRocketBinding
import com.spacex.model.data.Launch
import com.spacex.util.openWebPage
import com.spacex.util.openYoutubeVideo
import java.text.SimpleDateFormat
import java.util.*

class LaunchListAdapter(var context: Context?) : ListAdapter<Launch, LaunchListAdapter.LaunchViewHolder>(LaunchDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchViewHolder {
        return LaunchViewHolder.from(parent, context)
    }

    override fun onBindViewHolder(holder: LaunchViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindStyle(item)
    }

    class LaunchViewHolder(private val binding: ItemLaunchBinding, var context: Context?) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindStyle(launches: Launch): Unit = with(binding) {
            // Date formatter
            val formatter = SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.getDefault())
            formatter.timeZone = TimeZone.getDefault()

            // Fill ui
            txtName.text = launches.name + " "
            txtDate.text = formatter.format(launches.date_utc)

            if (launches.success) {
                txtSuccess.text =
                    context!!.getText(R.string.rocket_launches_success).toString() + " "
                backDrw.setBackgroundResource(R.drawable.back_success)
                imgCheck.setImageResource(R.drawable.ic_check_circle)
                imgCheck.setColorFilter(
                    context!!.resources.getColor(
                        R.color.success,
                        context!!.theme
                    )
                )

            } else {
                txtSuccess.text =
                    context!!.getText(R.string.rocket_launches_failure).toString() + " "
                backDrw.setBackgroundResource(R.drawable.back_failure)
                imgCheck.setImageResource(R.drawable.ic_highlight_off)
                imgCheck.setColorFilter(
                    context!!.resources.getColor(
                        R.color.accent_color,
                        context!!.theme
                    )
                )
            }

            if (!launches.links.youtube_id.isNullOrEmpty()) {
                youtube.visibility = View.VISIBLE
                youtube.setOnClickListener {
                    openYoutubeVideo(launches.links.youtube_id!!, context!!)
                }
            } else youtube.visibility = View.GONE

            if (!launches.links.wikipedia.isNullOrEmpty()) {
                wikipedia.visibility = View.VISIBLE
                wikipedia.setOnClickListener {
                    openWebPage(launches.links.wikipedia!!, context!!)
                }
            } else wikipedia.visibility = View.GONE

            if (!launches.links.presskit.isNullOrEmpty()) {
                presskit.visibility = View.VISIBLE
                presskit.setOnClickListener {
                    openWebPage(launches.links.presskit!!, context!!)
                }
            } else presskit.visibility = View.GONE
        }

        companion object {
            fun from(parent: ViewGroup, context: Context?): LaunchViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemLaunchBinding.inflate(layoutInflater, parent, false)
                return LaunchViewHolder(binding, context)
            }
        }
    }
}

class LaunchDiffCallback : DiffUtil.ItemCallback<Launch>() {

    override fun areItemsTheSame(oldItem: Launch, newItem: Launch): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Launch, newItem: Launch): Boolean {
        return oldItem == newItem
    }

}
