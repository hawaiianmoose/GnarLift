package hawaiianmoose.gnarlift

import android.content.Context
import android.content.res.ColorStateList
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import data.ResortDataItemResponse
import hawaiianmoose.gnarlift.databinding.LiftViewBinding

class ResortDetailRecyclerViewAdapter(
    private val resortDataItemResponse: ResortDataItemResponse
) : RecyclerView.Adapter<ResortDetailRecyclerViewAdapter.ViewHolder>() {

    private lateinit var parentContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        parentContext = parent.context
        val binding = LiftViewBinding.inflate(LayoutInflater.from(parentContext), parent, false)
        resortDataItemResponse.liftStatus.sortBy { it.name }
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (resortDataItemResponse.lifts.status.isNotEmpty()) {
            resortDataItemResponse.lifts.status.size
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val liftStatus = resortDataItemResponse.liftStatus[position]
        holder.binding.liftNameText.text = liftStatus.name

        if (!liftStatus.isOpen) {
            holder.binding.statusIcon.imageTintList =
                ColorStateList.valueOf(parentContext.getColor(R.color.statusRed))
        }
    }

    class ViewHolder(val binding: LiftViewBinding) : RecyclerView.ViewHolder(binding.root)
}
