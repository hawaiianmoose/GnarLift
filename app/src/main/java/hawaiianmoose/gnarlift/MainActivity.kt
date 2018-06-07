package hawaiianmoose.gnarlift

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import data.StaticResortDataItemResponse
import service.FavoriteService
import service.ResortService

class MainActivity : AppCompatActivity() {

    private lateinit var staticData: StaticResortDataItemResponse
    private lateinit var favoritesData: MutableSet<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeBottomNavBar()
        loadStaticResortData()
        loadFavoritesData()

        openExplorePage()
    }

    private fun loadFavoritesData() {
        favoritesData = FavoriteService.getInstance(this).getSavedFavorites()
    }

    private fun loadStaticResortData() {
        staticData = ResortService.getInstance(this).fetchStaticResortData()
    }

    private fun initializeBottomNavBar() {
        val bottomNavBar = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavBar.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navigation_home -> {
                    openExplorePage()
                    true
                }
                R.id.navigation_favorites -> {
                    openFavoritesPage()
                    true
                }
                R.id.navigation_account -> true
                else -> { false }
            }
        }
    }

    private fun openExplorePage() {
        loadFavoritesData()
        val favoritesFormattedData = ArrayList<String>()
        favoritesFormattedData.addAll(favoritesData)
        openFragment(ExploreFragment.newInstance(staticData, favoritesFormattedData))
    }

    private fun openFavoritesPage() {
        loadFavoritesData()
        val favoritesFormattedData = ArrayList<String>()
        favoritesFormattedData.addAll(favoritesData)
        openFragment(FavoritesFragment.newInstance(staticData, favoritesFormattedData))
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
