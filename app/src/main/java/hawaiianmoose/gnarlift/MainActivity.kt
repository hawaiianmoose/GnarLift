package hawaiianmoose.gnarlift

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import data.StaticResortDataItemResponse
import service.ResortService

class MainActivity : AppCompatActivity() {

    private lateinit var staticData: StaticResortDataItemResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeBottomNavBar()
        loadStaticResortData()
        openExplorePage(staticData)
    }

    private fun loadStaticResortData() {
        staticData = ResortService.getInstance(this).fetchStaticResortData()
    }

    fun initializeBottomNavBar() {
        val bottomNavBar = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavBar.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navigation_home -> {
                    openExplorePage(staticData)
                    true
                }
                R.id.navigation_favorites -> true
                R.id.navigation_account -> true
                else -> { false }
            }
        }
    }

    fun openExplorePage(staticData: StaticResortDataItemResponse) {
        val exploreFragment = ExploreFragment.newInstance(staticData, "notta")
        openFragment(exploreFragment)
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
