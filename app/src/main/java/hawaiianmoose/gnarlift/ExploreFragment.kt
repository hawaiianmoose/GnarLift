package hawaiianmoose.gnarlift

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import data.StaticResortDataItemResponse

private const val StaticResortData = "StaticResortData"
private const val FavoritesData = "FavoritesData"

class ExploreFragment : Fragment() {
    private var staticResortData: StaticResortDataItemResponse = StaticResortDataItemResponse()
    private var favoritesData: ArrayList<String> = arrayListOf()

    companion object {
        @JvmStatic
        fun newInstance(staticResortData: StaticResortDataItemResponse, favoritesData: ArrayList<String>) =
                ExploreFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(StaticResortData, staticResortData)
                        putStringArrayList(FavoritesData, favoritesData)
                    }
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            staticResortData = it.get(StaticResortData) as StaticResortDataItemResponse
            favoritesData = it.get(FavoritesData) as ArrayList<String>
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById(R.id.explore_resorts_recycler) as RecyclerView
        val viewManager = LinearLayoutManager(view.context)
        val viewAdapter = ResortRecyclerViewAdapter(staticResortData, favoritesData)
        recycler.setHasFixedSize(true)
        recycler.layoutManager = viewManager
        recycler.adapter = viewAdapter

        val searchView = view.findViewById(R.id.explore_search) as SearchView
        searchView.setOnClickListener({ searchView.isIconified = false })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewAdapter.filter.filter(newText)
                return true
            }
        })
    }
}
