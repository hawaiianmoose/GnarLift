package hawaiianmoose.gnarlift

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.squareup.picasso.Picasso
import data.StaticResortDataItem
import data.StaticResortDataItemResponse
import kotlinx.android.synthetic.main.resort_card_view.view.*
import service.FavoriteService

class FavoritesResortRecyclerViewAdapter(staticResortDataResponse: StaticResortDataItemResponse, private val favoritesData: ArrayList<String>): RecyclerView.Adapter<FavoritesResortRecyclerViewAdapter.ViewHolder>() {
    lateinit var parentContext: Context
    var favoritesResortData: MutableList<StaticResortDataItem> = staticResortDataResponse.resorts.filter { r -> favoritesData.contains(r.resortId) }.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesResortRecyclerViewAdapter.ViewHolder {
        parentContext = parent.context
        val cardView = LayoutInflater.from(parentContext).inflate(R.layout.resort_card_view, parent, false) as LinearLayout
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val resortName = favoritesResortData[position].name.toString()
        val resortId = favoritesResortData[position].resortId.toString()

        viewHolder.cardView.resort_name_text.text = resortName
        Picasso.get().load(favoritesResortData[position].imageUrl).fit().into(viewHolder.cardView.resort_card_background)

        if (favoritesData.contains(favoritesResortData[position].resortId)) {
            viewHolder.cardView.favorite_resort_button.setImageResource(R.drawable.ic_sharp_favorite_24px)
        }

        //TODO setup actual click to details page
        viewHolder.cardView.setOnClickListener({
            Toast.makeText(parentContext, viewHolder.cardView.resort_name_text.text, Toast.LENGTH_LONG).show()
        })

        viewHolder.cardView.favorite_resort_button.setOnClickListener {
            removeFavoriteResort(viewHolder, resortId, resortName, position)
        }
    }

    override fun getItemCount() = favoritesResortData.size

    private fun makeToast(resortName: String, stringId: Int): String {
        return String.format(parentContext.getString(stringId), resortName)
    }

    private fun removeFavoriteResort(viewHolder: ViewHolder, resortId: String, resortName: String, position: Int) {
        val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    dialog.dismiss()
                    viewHolder.cardView.favorite_resort_button.setImageResource(R.drawable.ic_sharp_favorite_border_24px)
                    FavoriteService.getInstance(parentContext).removeFavorite(resortId)
                    favoritesData.remove(resortId)
                    favoritesResortData.removeAt(position)
                    Toast.makeText(parentContext, makeToast(resortName, R.string.toast_removed), Toast.LENGTH_LONG).show()
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

    class ViewHolder(val cardView: LinearLayout) : RecyclerView.ViewHolder(cardView)
}