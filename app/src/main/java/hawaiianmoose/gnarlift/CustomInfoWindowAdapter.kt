package hawaiianmoose.gnarlift

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.map_marker_window.view.*

class CustomInfoWindowAdapter(context: Context) : GoogleMap.InfoWindowAdapter {

    private val window: View = LayoutInflater.from(context).inflate(R.layout.map_marker_window, null)

    private fun renderWindowText(marker: Marker?, view: View) {
        val title = marker?.title
        view.marker_title.text = title
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