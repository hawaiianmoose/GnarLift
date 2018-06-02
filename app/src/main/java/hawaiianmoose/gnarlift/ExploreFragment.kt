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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val StaticResortData = "StaticResortData"
private const val ARG_PARAM2 = "param2"

class ExploreFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var staticResortData: StaticResortDataItemResponse? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            staticResortData = it.get(StaticResortData) as StaticResortDataItemResponse
            param2 = it.getString(ARG_PARAM2)
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
        searchView.setOnClickListener({
            searchView.setIconified(false)
        })

    }

    companion object {

        @JvmStatic
        fun newInstance(staticResortData: StaticResortDataItemResponse, param2: String) =
                ExploreFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(StaticResortData, staticResortData)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
