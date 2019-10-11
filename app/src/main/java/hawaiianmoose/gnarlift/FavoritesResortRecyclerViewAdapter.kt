package hawaiianmoose.gnarlift

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import com.squareup.picasso.Picasso
import data.StaticResortDataItemResponse
import kotlinx.android.synthetic.main.resort_card_view.view.*
import service.FavoriteService
import utils.LiftieServiceUtil

class FavoritesResortRecyclerViewAdapter(val staticResortDataResponse: StaticResortDataItemResponse, context: Context): RecyclerView.Adapter<ResortRecyclerViewAdapter.ViewHolder>(), BaseResortRecyclerAdapter {
    override var isLoading: Boolean = false
    var parentContext = context
    var favoritesData = FavoriteService.getInstance(context).getSavedFavorites()
    var favoritesResortData = staticResortDataResponse.resorts.filter { r -> favoritesData.contains(r.resortId) }.toMutableList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResortRecyclerViewAdapter.ViewHolder {
        val cardView = LayoutInflater.from(parentContext).inflate(R.layout.resort_card_view, parent, false) as RelativeLayout
        return ResortRecyclerViewAdapter.ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ResortRecyclerViewAdapter.ViewHolder, position: Int) {
        val resortName = favoritesResortData[position].name.toString()
        val resortId = favoritesResortData[position].resortId.toString()

        viewHolder.cardView.resort_name_text.text = resortName
        Picasso.get().load(favoritesResortData[position].imageUrl).fit().into(viewHolder.cardView.resort_card_background)

        if (favoritesData.contains(favoritesResortData[position].resortId)) {
            viewHolder.cardView.favorite_resort_button.setImageResource(R.drawable.ic_sharp_star_24px)
        }

        viewHolder.cardView.setOnClickListener {
            if (!isLoading) {
                isLoading = true
                viewHolder.cardView.resort_card_view.alpha = 0.5f
                viewHolder.cardView.loading_animation_view.visibility = View.VISIBLE
                LiftieServiceUtil.getLiftieDataForResort(favoritesResortData[position], viewHolder, parentContext, this)
            }
        }

        viewHolder.cardView.favorite_resort_button.setOnClickListener {
            removeFavoriteResort(resortId, resortName, position)
        }
    }

    override fun getItemCount() = favoritesResortData.size

    fun refreshFavorites() {
        if (favoritesResortData.any()) {
            favoritesData = FavoriteService.getInstance(parentContext).getSavedFavorites()
            favoritesResortData = staticResortDataResponse.resorts.filter { r -> favoritesData.contains(r.resortId) }.toMutableList()
        }
    }

    private fun makeToast(resortName: String, stringId: Int): String {
        return String.format(parentContext.getString(stringId), resortName)
    }

    private fun removeFavoriteResort(resortId: String, resortName: String, position: Int) {
        val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    dialog.dismiss()
                    FavoriteService.getInstance(parentContext).removeFavorite(resortId)
                    favoritesData.remove(resortId)
                    favoritesResortData.removeAt(position)
                    Toast.makeText(parentContext, makeToast(resortName, R.string.toast_removed), Toast.LENGTH_SHORT).show()
                    notifyDataSetChanged()
                }

                DialogInterface.BUTTON_NEGATIVE -> {
                    dialog.dismiss()
                }
            }
        }

        val builder = AlertDialog.Builder(parentContext)
        builder.setMessage(parentContext.getString(R.string.remove_favorite_message)).setPositiveButton(parentContext.getString(R.string.yes), dialogClickListener)
                .setNegativeButton(parentContext.getString(R.string.no), dialogClickListener).show()
    }
}