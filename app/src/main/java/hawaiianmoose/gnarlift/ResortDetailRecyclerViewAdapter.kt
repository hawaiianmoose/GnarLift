package hawaiianmoose.gnarlift

import android.content.Context
import android.content.res.ColorStateList
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import data.ResortDataItemResponse
import kotlinx.android.synthetic.main.lift_view.view.*

class ResortDetailRecyclerViewAdapter(val resortDataItemResponse: ResortDataItemResponse): RecyclerView.Adapter<ResortDetailRecyclerViewAdapter.ViewHolder>() {
    lateinit var parentContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        parentContext = parent.context
        val liftView = LayoutInflater.from(parentContext).inflate(R.layout.lift_view, parent, false) as LinearLayout
        resortDataItemResponse.liftStatus.sortBy { lift -> lift.name }

        return ViewHolder(liftView)
    }

    override fun getItemCount(): Int {
        return if (resortDataItemResponse.lifts?.status != null) {
            resortDataItemResponse.lifts.status.size
        }
        else {
            0
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.liftView.lift_name_text.text = resortDataItemResponse.liftStatus.get(position).name

        if (!resortDataItemResponse.liftStatus.get(position).isOpen) {
            holder.liftView.status_icon.imageTintList = ColorStateList.valueOf(parentContext.getColor(R.color.statusRed))
        }
    }

    class ViewHolder(val liftView: LinearLayout) : RecyclerView.ViewHolder(liftView)
}
