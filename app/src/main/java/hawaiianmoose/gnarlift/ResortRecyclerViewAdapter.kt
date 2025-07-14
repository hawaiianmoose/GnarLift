package hawaiianmoose.gnarlift

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import com.squareup.picasso.Picasso
import data.StaticResortDataItem
import data.StaticResortDataItemResponse
import service.FavoriteService
import android.view.animation.AnimationUtils
import hawaiianmoose.gnarlift.databinding.ResortCardViewBinding
import utils.LiftieServiceUtil

class ResortRecyclerViewAdapter(
    private val staticResortDataResponse: StaticResortDataItemResponse
) : RecyclerView.Adapter<ResortRecyclerViewAdapter.ViewHolder>(), Filterable, BaseResortRecyclerAdapter {

    override var isLoading: Boolean = false
    lateinit var parentContext: Context
    var staticResortData: MutableList<StaticResortDataItem> = staticResortDataResponse.resorts
    var filteredResortData: MutableList<StaticResortDataItem> = staticResortDataResponse.resorts
    var favoritesData = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        parentContext = parent.context
        val binding = ResortCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        favoritesData = FavoriteService.getInstance(parentContext).getSavedFavorites() as MutableSet<String>
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resortData = filteredResortData[position]
        val resortName = resortData.name.toString()
        val resortId = resortData.resortId.toString()

        holder.binding.resortNameText.text = resortName
        Picasso.get().load(resortData.imageUrl).fit().into(holder.binding.resortCardBackground)
        holder.binding.loadingAnimationView.visibility = if (resortData.isLoading) View.VISIBLE else View.GONE

        if (favoritesData.contains(resortData.resortId)) {
            holder.binding.favoriteResortButton.setImageResource(R.drawable.ic_sharp_star_24px)
        } else {
            holder.binding.favoriteResortButton.setImageResource(R.drawable.ic_sharp_star_border_24px)
        }

        holder.binding.root.setOnClickListener {
            if (!isLoading) {
                isLoading = true
                resortData.isLoading = true
                holder.binding.resortCardView.alpha = 0.5f
                this.notifyDataSetChanged()
                LiftieServiceUtil.getLiftieDataForResort(resortData, holder, parentContext, this)
            }
        }

        holder.binding.favoriteResortButton.setOnClickListener {
            val image = holder.binding.favoriteResortButton
            val iconBounce = AnimationUtils.loadAnimation(parentContext, R.anim.bounce)
            image.startAnimation(iconBounce)
            if (favoritesData.contains(resortId)) {
                removeFavoriteResort(holder, resortId, resortName)
            } else {
                addFavoriteResort(holder, resortId, resortName)
            }
        }
    }

    override fun getItemCount(): Int = filteredResortData.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()

                filteredResortData = if (charString.isEmpty()) {
                    staticResortDataResponse.resorts
                } else {
                    staticResortData.filter {
                        it.name?.toLowerCase()?.startsWith(charString.lowercase()) == true
                    }.toMutableList()
                }

                return FilterResults().apply {
                    values = filteredResortData
                }
            }

            override fun publishResults(charSequence: CharSequence, results: FilterResults) {
                filteredResortData = results.values as MutableList<StaticResortDataItem>
                notifyDataSetChanged()
            }
        }
    }

    private fun makeToast(resortName: String, stringId: Int): String {
        return String.format(parentContext.getString(stringId), resortName)
    }

    private fun addFavoriteResort(holder: ViewHolder, resortId: String, resortName: String) {
        holder.binding.favoriteResortButton.setImageResource(R.drawable.ic_sharp_star_24px)
        FavoriteService.getInstance(parentContext).saveFavorite(resortId)
        favoritesData.add(resortId)
        Toast.makeText(parentContext, makeToast(resortName, R.string.toast_added), Toast.LENGTH_SHORT).show()
    }

    private fun removeFavoriteResort(holder: ViewHolder, resortId: String, resortName: String) {
        holder.binding.favoriteResortButton.setImageResource(R.drawable.ic_sharp_star_border_24px)
        FavoriteService.getInstance(parentContext).removeFavorite(resortId)
        favoritesData.remove(resortId)
        Toast.makeText(parentContext, makeToast(resortName, R.string.toast_removed), Toast.LENGTH_SHORT).show()
    }

    class ViewHolder(val binding: ResortCardViewBinding) : RecyclerView.ViewHolder(binding.root)
}
