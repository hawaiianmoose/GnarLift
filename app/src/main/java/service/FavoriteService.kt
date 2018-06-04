package service

import android.content.Context
import data.Constants

class FavoriteService private constructor (passedContext: Context) {

    private var context: Context = passedContext

    companion object : SingletonHolder<FavoriteService, Context>(::FavoriteService)

    fun getSavedFavorites(): MutableSet<String> {
        val prefs = context.getSharedPreferences(Constants.sharedPrefFile, Context.MODE_PRIVATE)
        return prefs.getStringSet(Constants.favoriteResorts, mutableSetOf())
    }

    fun saveFavorite(favorite: String?) {
        if (favorite.isNullOrEmpty()) { return }
        val prefs = context.getSharedPreferences(Constants.sharedPrefFile, Context.MODE_PRIVATE)
        val savedFavorites = prefs.getStringSet(Constants.favoriteResorts, mutableSetOf())
        val newSavedFavorites = mutableSetOf<String?>()
        for (favoriteResort in savedFavorites) {
            newSavedFavorites.add(favoriteResort)
        }
        newSavedFavorites.add(favorite)

        prefs.edit().putStringSet(Constants.favoriteResorts, newSavedFavorites).apply()
    }

    fun removeFavorite(favorite: String?) {
        if (favorite.isNullOrEmpty()) { return }
        val prefs = context.getSharedPreferences(Constants.sharedPrefFile, Context.MODE_PRIVATE)
        val savedFavorites = prefs.getStringSet(Constants.favoriteResorts, mutableSetOf())
        val newSavedFavorites = mutableSetOf<String?>()
        for (favoriteResort in savedFavorites) {
            newSavedFavorites.add(favoriteResort)
        }
        newSavedFavorites.remove(favorite)
        prefs.edit().putStringSet(Constants.favoriteResorts, newSavedFavorites).apply()
    }
}