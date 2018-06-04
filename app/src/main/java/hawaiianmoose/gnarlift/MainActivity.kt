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


        openExplorePage(staticData, favoritesData)
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
                    openExplorePage(staticData, favoritesData)
                    true
                }
                R.id.navigation_favorites -> true
                R.id.navigation_account -> true
                else -> { false }
            }
        }
    }

    private fun openExplorePage(staticData: StaticResortDataItemResponse, favoritesData: MutableSet<String>) {
        val favoritesFormattedData = ArrayList<String>()
        favoritesFormattedData.addAll(favoritesData)
        val exploreFragment = ExploreFragment.newInstance(staticData, favoritesFormattedData)
        openFragment(exploreFragment)
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
