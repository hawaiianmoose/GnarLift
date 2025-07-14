package hawaiianmoose.gnarlift

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import data.StaticResortDataItemResponse
import hawaiianmoose.gnarlift.databinding.ResortCardViewBinding
import service.FavoriteService
import utils.LiftieServiceUtil

class FavoritesResortRecyclerViewAdapter(
    private val staticResortDataResponse: StaticResortDataItemResponse,
    private val parentContext: Context
) : RecyclerView.Adapter<FavoritesResortRecyclerViewAdapter.ViewHolder>(), BaseResortRecyclerAdapter {

    override var isLoading: Boolean = false
    var favoritesData = FavoriteService.getInstance(parentContext).getSavedFavorites()
    private var favoritesResortData =
        staticResortDataResponse.resorts.filter { r -> favoritesData?.contains(r.resortId) == true }
            .toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ResortCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resort = favoritesResortData[position]
        val resortName = resort.name.orEmpty()
        val resortId = resort.resortId.orEmpty()

        holder.binding.resortNameText.text = resortName
        Picasso.get().load(resort.imageUrl).fit().into(holder.binding.resortCardBackground)

        if (favoritesData?.contains(resort.resortId) == true) {
            holder.binding.favoriteResortButton.setImageResource(R.drawable.ic_sharp_star_24px)
        }

        holder.binding.root.setOnClickListener {
            if (!isLoading) {
                isLoading = true
                holder.binding.resortCardView.alpha = 0.5f
                holder.binding.loadingAnimationView.visibility = View.VISIBLE
                LiftieServiceUtil.getLiftieDataForResort(resort, holder, parentContext, this)
            }
        }

        holder.binding.favoriteResortButton.setOnClickListener {
            removeFavoriteResort(resortId, resortName, position)
        }
    }

    override fun getItemCount(): Int = favoritesResortData.size

    fun refreshFavorites() {
        favoritesData = FavoriteService.getInstance(parentContext).getSavedFavorites()
        favoritesResortData = staticResortDataResponse.resorts.filter {
            favoritesData?.contains(it.resortId) ?: false
        }.toMutableList()
        notifyDataSetChanged()
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
                    //favoritesData.remove(resortId)
                    favoritesResortData.removeAt(position)
                    Toast.makeText(parentContext, makeToast(resortName, R.string.toast_removed), Toast.LENGTH_SHORT).show()
                    notifyDataSetChanged()
                }
                DialogInterface.BUTTON_NEGATIVE -> dialog.dismiss()
            }
        }

        AlertDialog.Builder(parentContext)
            .setMessage(R.string.remove_favorite_message)
            .setPositiveButton(R.string.yes, dialogClickListener)
            .setNegativeButton(R.string.no, dialogClickListener)
            .show()
    }

    class ViewHolder(val binding: ResortCardViewBinding) : RecyclerView.ViewHolder(binding.root)
}
