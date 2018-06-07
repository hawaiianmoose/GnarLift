package hawaiianmoose.gnarlift

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import data.Constants
import data.StaticResortDataItemResponse

class FavoritesFragment : Fragment() {
    private var staticResortData: StaticResortDataItemResponse = StaticResortDataItemResponse()
    private var favoritesData: ArrayList<String> = arrayListOf()

    companion object {
        @JvmStatic
        fun newInstance(staticResortData: StaticResortDataItemResponse, favoritesData: ArrayList<String>) =
                FavoritesFragment().apply {
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
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById(R.id.favorite_resorts_recycler) as RecyclerView
        val viewManager = LinearLayoutManager(view.context)
        val viewAdapter = FavoritesResortRecyclerViewAdapter(staticResortData, favoritesData)
        recycler.setHasFixedSize(true)
        recycler.layoutManager = viewManager
        recycler.adapter = viewAdapter
    }
}