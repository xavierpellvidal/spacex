package com.spacex.usecases.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.spacex.R
import com.spacex.databinding.ItemLaunchBinding
import com.spacex.model.data.Launch
import com.spacex.util.openWebPage
import com.spacex.util.openYoutubeVideo
import java.text.SimpleDateFormat
import java.util.*

class LaunchListAdapter(val launches: MutableList<Launch>, var context: Context?) :
    RecyclerView.Adapter<LaunchListAdapter.RocketViewHolderStyle>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RocketViewHolderStyle {
        return RocketViewHolderStyle(
            ItemLaunchBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RocketViewHolderStyle, position: Int) {
        holder.bindStyle(launches[position])
    }

    inner class RocketViewHolderStyle(private val binding: ItemLaunchBinding) :
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
    }

    override fun getItemCount(): Int = launches.size
}
