package hawaiianmoose.gnarlift

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import data.Constants
import data.StaticResortDataItemResponse
import hawaiianmoose.gnarlift.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
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
        val viewAdapter = FavoritesResortRecyclerViewAdapter(staticResortData, requireContext())
        recycler.setHasFixedSize(true)
        recycler.layoutManager = viewManager
        recycler.adapter = viewAdapter
    }

    override fun onResume() {
        val favAdapter = binding.favoriteResortsRecycler.adapter as FavoritesResortRecyclerViewAdapter
        favAdapter.refreshFavorites()
        favAdapter.notifyDataSetChanged()
        super.onResume()
    }
}