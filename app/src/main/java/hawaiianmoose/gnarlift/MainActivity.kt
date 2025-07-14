package hawaiianmoose.gnarlift

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
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

    private fun openDefaultLandingPage() = if (favoritesData.size > 0) {
        openFavoritesPage()
        defaultHomeId = R.id.navigation_favorites
        bottomNavBar.selectedItemId = defaultHomeId
    } else {
        openExplorePage()
    }

    private fun loadFavoritesData() {
        favoritesData = FavoriteService.getInstance(this).getSavedFavorites() as MutableSet<String>
    }

    private fun loadStaticResortData() {
        staticData = ResortService.getInstance(this).fetchStaticResortData()
    }

    private fun initializeBottomNavBar() {
        bottomNavBar.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navigation_home -> {
                    if (bottomNavBar.selectedItemId != R.id.navigation_home) {
                        if(bottomNavBar.selectedItemId == R.id.navigation_favorites) {
                            openExplorePage(R.anim.enter_from_left, R.anim.exit_to_right)
                        }
                        if(bottomNavBar.selectedItemId == R.id.navigation_info) {
                            openExplorePage(R.anim.enter_from_right, R.anim.exit_to_left)
                        }
                    }
                    true
                }
                R.id.navigation_favorites -> {
                    if (bottomNavBar.selectedItemId != R.id.navigation_favorites) {
                        if(bottomNavBar.selectedItemId == R.id.navigation_home) {
                            openFavoritesPage(R.anim.enter_from_right, R.anim.exit_to_left)
                        }
                        if(bottomNavBar.selectedItemId == R.id.navigation_info) {
                            openFavoritesPage(R.anim.enter_from_left, R.anim.exit_to_right)
                        }
                    }
                    true
                }
                R.id.navigation_info -> {
                    if (bottomNavBar.selectedItemId != R.id.navigation_info) {
                        if(bottomNavBar.selectedItemId == R.id.navigation_favorites) {
                            openInfoPage(R.anim.enter_from_right, R.anim.exit_to_left)
                        }
                        if(bottomNavBar.selectedItemId == R.id.navigation_home) {
                            openInfoPage(R.anim.enter_from_left, R.anim.exit_to_right)
                        }
                    }
                    true
                }
                else -> { false }
            }
        }
    }

    private fun openExplorePage(enterAnimationId: Int? = null, exitAnimationId: Int? = null) {
        loadFavoritesData()
        val favoritesFormattedData = ArrayList<String>()
        favoritesFormattedData.addAll(favoritesData)
        openFragment(ExploreFragment.newInstance(staticData, favoritesFormattedData), enterAnimationId, exitAnimationId)
    }

    private fun openFavoritesPage(enterAnimationId: Int? = null, exitAnimationId: Int? = null) {
        loadFavoritesData()
        val favoritesFormattedData = ArrayList<String>()
        favoritesFormattedData.addAll(favoritesData)
        openFragment(FavoritesFragment.newInstance(staticData, favoritesFormattedData), enterAnimationId, exitAnimationId)
    }

    private fun openInfoPage(enterAnimationId: Int? = null, exitAnimationId: Int? = null) {
        openFragment(InfoFragment(), enterAnimationId, exitAnimationId)
    }

    private fun openFragment(fragment: Fragment, enterAnimationId: Int?, exitAnimationId: Int?) {
        val transaction = supportFragmentManager.beginTransaction()
        if (enterAnimationId != null && exitAnimationId != null) {
            transaction.setCustomAnimations(enterAnimationId, exitAnimationId)
        }
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }
}
