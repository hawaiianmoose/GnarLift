package hawaiianmoose.gnarlift

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.squareup.picasso.Picasso
import data.StaticResortDataItemResponse
import kotlinx.android.synthetic.main.resort_card_view.view.*
import service.FavoriteService

class ResortRecyclerViewAdapter(private val staticResortData: StaticResortDataItemResponse, private val favoritesData: ArrayList<String>): RecyclerView.Adapter<ResortRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(val cardView: LinearLayout) : RecyclerView.ViewHolder(cardView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResortRecyclerViewAdapter.ViewHolder {
        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.resort_card_view, parent, false) as LinearLayout

        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cardView.resort_name_text.text = staticResortData.resorts[position].name
        Picasso.get().load(staticResortData.resorts[position].imageUrl).fit().into(holder.cardView.resort_card_background)

        if (favoritesData.contains(staticResortData.resorts[position].resortId)) {
            holder.cardView.favorite_resort_button.setImageResource(R.drawable.ic_sharp_favorite_24px)
        }

        //testing click
        holder.cardView.setOnClickListener({
            Toast.makeText(holder.cardView.context, holder.cardView.resort_name_text.text, Toast.LENGTH_LONG).show() //TODO add as toast for adding / removing favorites
        })

        holder.cardView.favorite_resort_button.setOnClickListener {
            val resortId = staticResortData.resorts[position].resortId

            if (favoritesData.contains(resortId)) {
                holder.cardView.favorite_resort_button.setImageResource(R.drawable.ic_sharp_favorite_border_24px)
                FavoriteService.getInstance(holder.cardView.context).removeFavorite(resortId)
                favoritesData.remove(resortId)
            } else {
                holder.cardView.favorite_resort_button.setImageResource(R.drawable.ic_sharp_favorite_24px)
                FavoriteService.getInstance(holder.cardView.context).saveFavorite(resortId)
                favoritesData.add(resortId ?: "")
            }
        }
    }

    override fun getItemCount() = staticResortData.resorts.size
}
