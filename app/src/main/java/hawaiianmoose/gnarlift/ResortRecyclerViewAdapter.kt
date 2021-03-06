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
import kotlinx.android.synthetic.main.resort_card_view.view.*
import service.FavoriteService
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import utils.LiftieServiceUtil

class ResortRecyclerViewAdapter(private val staticResortDataResponse: StaticResortDataItemResponse) : RecyclerView.Adapter<ResortRecyclerViewAdapter.ViewHolder>(), Filterable, BaseResortRecyclerAdapter {
    override var isLoading: Boolean = false
    lateinit var parentContext: Context
    var staticResortData: MutableList<StaticResortDataItem> = staticResortDataResponse.resorts
    var filteredResortData: MutableList<StaticResortDataItem> = staticResortDataResponse.resorts
    var favoritesData = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResortRecyclerViewAdapter.ViewHolder {
        parentContext = parent.context
        val cardView = LayoutInflater.from(parentContext).inflate(R.layout.resort_card_view, parent, false) as RelativeLayout
        favoritesData = FavoriteService.getInstance(this.parentContext).getSavedFavorites()
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val resortData = filteredResortData[position]
        val resortName = resortData.name.toString()
        val resortId = resortData.resortId.toString()

        viewHolder.cardView.resort_name_text.text = resortName
        Picasso.get().load(resortData.imageUrl).fit().into(viewHolder.cardView.resort_card_background)
        viewHolder.cardView.loading_animation_view.visibility = if (resortData.isLoading) View.VISIBLE else View.GONE

        if (favoritesData.contains(resortData.resortId)) {
            viewHolder.cardView.favorite_resort_button.setImageResource(R.drawable.ic_sharp_star_24px)
        } else {
            viewHolder.cardView.favorite_resort_button.setImageResource(R.drawable.ic_sharp_star_border_24px)
        }

        viewHolder.cardView.setOnClickListener {
            if (!isLoading) {
                isLoading = true
                resortData.isLoading = true
                viewHolder.cardView.resort_card_view.alpha = 0.5f
                this.notifyDataSetChanged()
                LiftieServiceUtil.getLiftieDataForResort(resortData, viewHolder, parentContext, this)
            }
        }

        viewHolder.cardView.favorite_resort_button.setOnClickListener {
            if (favoritesData.contains(resortId)) {
                removeFavoriteResort(viewHolder, resortId, resortName)
            } else {
                val image = viewHolder.cardView.favorite_resort_button as ImageView
                val iconBounce = AnimationUtils.loadAnimation(parentContext, R.anim.bounce)
                image.startAnimation(iconBounce)
                addFavoriteResort(viewHolder, resortId, resortName)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()

                filteredResortData = if (charString.isEmpty()) {
                    staticResortDataResponse.resorts
                } else {
                    val filteredList = mutableListOf<StaticResortDataItem>()

                    for (resort in staticResortData) {
                        if (resort.name!!.toLowerCase().startsWith(charString)) {
                            filteredList.add(resort)
                        }
                    }
                    filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = filteredResortData
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredResortData = filterResults.values as MutableList<StaticResortDataItem>
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount() = filteredResortData.size

    private fun makeToast(resortName: String, stringId: Int): String {
        return String.format(parentContext.getString(stringId), resortName)
    }

    private fun addFavoriteResort(viewHolder: ViewHolder, resortId: String, resortName: String) {
        viewHolder.cardView.favorite_resort_button.setImageResource(R.drawable.ic_sharp_star_24px)
        FavoriteService.getInstance(parentContext).saveFavorite(resortId)
        favoritesData.add(resortId)
        Toast.makeText(parentContext, makeToast(resortName, R.string.toast_added), Toast.LENGTH_SHORT).show()
    }

    private fun removeFavoriteResort(viewHolder: ViewHolder, resortId: String, resortName: String) {
        viewHolder.cardView.favorite_resort_button.setImageResource(R.drawable.ic_sharp_star_border_24px)
        FavoriteService.getInstance(parentContext).removeFavorite(resortId)
        favoritesData.remove(resortId)
        Toast.makeText(parentContext, makeToast(resortName, R.string.toast_removed), Toast.LENGTH_SHORT).show()
    }

    class ViewHolder(val cardView: RelativeLayout) : RecyclerView.ViewHolder(cardView)
}
