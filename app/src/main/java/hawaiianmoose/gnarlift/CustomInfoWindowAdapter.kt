package hawaiianmoose.gnarlift

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.map_marker_window.view.*

class CustomInfoWindowAdapter(val context: Context) : GoogleMap.InfoWindowAdapter {

    private val window: View = LayoutInflater.from(context).inflate(R.layout.map_marker_window, null)

    private fun renderWindowText(marker: Marker?, view: View) {
        val title = marker?.title
        view.marker_title.text = title

        if (title == context.getString(R.string.current_location)) {
            view.marker_icon.visibility = View.GONE
        } else {
            view.marker_icon.visibility = View.VISIBLE
        }
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