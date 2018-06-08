package hawaiianmoose.gnarlift

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import data.StaticResortDataItemResponse
import service.FavoriteService
import service.ResortService

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavBar: BottomNavigationView
    private lateinit var staticData: StaticResortDataItemResponse
    private lateinit var favoritesData: MutableSet<String>
    private var defaultHomeId: Int = R.id.navigation_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavBar = findViewById(R.id.bottom_navigation)
        initializeBottomNavBar()
        loadStaticResortData()
        loadFavoritesData()

        openDefaultLandingPage()
    }

    override fun onBackPressed() {
        val selectedItemId = bottomNavBar.selectedItemId
        if (defaultHomeId != selectedItemId) {
            bottomNavBar.selectedItemId = defaultHomeId
        } else {
            super.onBackPressed()
        }
    }

    private fun openDefaultLandingPage() = if (favoritesData.any()) {
        openFavoritesPage()
        defaultHomeId = R.id.navigation_favorites
        bottomNavBar.selectedItemId = defaultHomeId
    } else {
        openExplorePage()
    }

    private fun loadFavoritesData() {
        favoritesData = FavoriteService.getInstance(this).getSavedFavorites()
    }

    private fun loadStaticResortData() {
        staticData = ResortService.getInstance(this).fetchStaticResortData()
    }

    private fun initializeBottomNavBar() {
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
        transaction.commit()
    }
}
