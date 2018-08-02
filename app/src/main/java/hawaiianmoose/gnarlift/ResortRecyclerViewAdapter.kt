package hawaiianmoose.gnarlift

import android.content.Context
import android.support.v7.widget.RecyclerView
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

class ResortRecyclerViewAdapter(private val staticResortDataResponse: StaticResortDataItemResponse, private val favoritesData: ArrayList<String>): RecyclerView.Adapter<ResortRecyclerViewAdapter.ViewHolder>(), Filterable {
    lateinit var parentContext: Context
    lateinit var parentView: ViewGroup
    var isLoading = false
    var staticResortData: MutableList<StaticResortDataItem> = staticResortDataResponse.resorts
    var filteredResortData: MutableList<StaticResortDataItem> = staticResortDataResponse.resorts

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResortRecyclerViewAdapter.ViewHolder {
        parentContext = parent.context
        parentView = parent
        val cardView = LayoutInflater.from(parentContext).inflate(R.layout.resort_card_view, parent, false) as RelativeLayout
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val resortName = filteredResortData[position].name.toString()
        val resortId = filteredResortData[position].resortId.toString()

        viewHolder.cardView.resort_name_text.text = resortName
        Picasso.get().load(filteredResortData[position].imageUrl).fit().into(viewHolder.cardView.resort_card_background)

        if (favoritesData.contains(filteredResortData[position].resortId)) {
            viewHolder.cardView.favorite_resort_button.setImageResource(R.drawable.ic_sharp_star_24px)
        }

        viewHolder.cardView.setOnClickListener {
            if (!isLoading) {
                isLoading = true
                viewHolder.cardView.resort_card_view.alpha = 0.5f
                viewHolder.cardView.loading_animation_view.visibility = View.VISIBLE
                LiftieServiceUtil.getLiftieDataForResort(filteredResortData[position], viewHolder, parentContext, this)
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
                        if (resort.name!!.toLowerCase().contains(charString)) {
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
