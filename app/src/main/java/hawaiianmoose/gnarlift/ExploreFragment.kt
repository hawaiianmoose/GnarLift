package hawaiianmoose.gnarlift

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import data.Constants
import data.StaticResortDataItemResponse
import kotlinx.android.synthetic.main.fragment_explore.*

class ExploreFragment : Fragment() {
    private var staticResortData: StaticResortDataItemResponse = StaticResortDataItemResponse()
    private var favoritesData: ArrayList<String> = arrayListOf()

    companion object {
        @JvmStatic
        fun newInstance(staticResortData: StaticResortDataItemResponse, favoritesData: ArrayList<String>) =
                ExploreFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(Constants.staticResortData, staticResortData)
                        putStringArrayList(Constants.favoritesData, favoritesData)
                    }
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            staticResortData = it.get(Constants.staticResortData) as StaticResortDataItemResponse
            favoritesData = it.get(Constants.favoritesData) as ArrayList<String>
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById(R.id.explore_resorts_recycler) as RecyclerView
        val viewManager = LinearLayoutManager(view.context)
        val viewAdapter = ResortRecyclerViewAdapter(staticResortData)
        recycler.setHasFixedSize(true)
        recycler.layoutManager = viewManager
        recycler.adapter = viewAdapter

        val searchView = view.findViewById(R.id.explore_search) as SearchView
        searchView.setOnClickListener { searchView.isIconified = false }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewAdapter.filter.filter(newText)
                return true
            }
        })

        map_button.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this.context!!, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(this.context, ExploreMapActivity::class.java)
                intent.putExtra(Constants.staticResortData, staticResortData)
                startActivity(intent)
            } else {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1337)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == 0) {
            val intent = Intent(this.context, ExploreMapActivity::class.java)
            intent.putExtra(Constants.staticResortData, staticResortData)
            startActivity(intent)
        }
    }

    override fun onResume() {
        explore_resorts_recycler.adapter.notifyDataSetChanged()
        super.onResume()
    }
}
