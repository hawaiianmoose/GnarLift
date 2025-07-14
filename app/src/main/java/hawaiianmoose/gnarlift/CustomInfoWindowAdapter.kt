package hawaiianmoose.gnarlift

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import hawaiianmoose.gnarlift.databinding.MapMarkerWindowBinding

class CustomInfoWindowAdapter(val context: Context) : GoogleMap.InfoWindowAdapter {

    private var _binding: MapMarkerWindowBinding? = null
    private val binding get() = _binding!!
    private val window: View = LayoutInflater.from(context).inflate(R.layout.map_marker_window, null)

    private fun renderWindowText(marker: Marker?, view: View) {
        val title = marker?.title
        binding.markerTitle.text = title
        binding.markerIcon.visibility = View.VISIBLE
    }

    override fun getInfoContents(marker: Marker?): View {
        renderWindowText(marker, window)
        return window
    }

    override fun getInfoWindow(marker: Marker?): View {
        renderWindowText(marker, window)
        return window
    }
}