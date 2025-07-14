package hawaiianmoose.gnarlift

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import data.Constants
import data.StaticResortDataItemResponse
import hawaiianmoose.gnarlift.databinding.FragmentExploreBinding

class ExploreFragment : Fragment() {
    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
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

        binding.mapButton.setOnClickListener {
            val intent = Intent(this.context, ExploreMapActivity::class.java)
            intent.putExtra(Constants.staticResortData, staticResortData)
            startActivity(intent)
        }
    }

    override fun onResume() {
        binding.exploreResortsRecycler.adapter?.notifyDataSetChanged()
        super.onResume()
    }
}
