package hawaiianmoose.gnarlift

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import data.Constants
import data.StaticResortDataItem
import data.StaticResortDataItemResponse
import utils.LiftieServiceUtil

class ExploreMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var staticData = StaticResortDataItemResponse()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        staticData = intent.extras.get(Constants.staticResortData) as StaticResortDataItemResponse

        setContentView(R.layout.activity_explore_map)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setInfoWindowAdapter(CustomInfoWindowAdapter(this))

        addResortMarkers(mMap)
        val defaultLocation = LatLng(39.8283, -98.5795)
        centerMap(defaultLocation)
    }

    private fun centerMap(latLng: LatLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(3.5f))
    }

    private fun addResortMarkers(mMap: GoogleMap) {
        for (resort in staticData.resorts) {
            val resortLocation = LatLng(resort.latitude, resort.longitude)
            val locationMarker = mMap.addMarker(MarkerOptions().position(resortLocation).title(resort.name))
            locationMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            locationMarker.tag = resort

            mMap.setOnInfoWindowClickListener { marker ->
                if (marker.title != getString(R.string.current_location)) {
                    val loadingDialog = LoadingDialog()
                    loadingDialog.show(fragmentManager, "loading_dialog_fragment")

                    LiftieServiceUtil.getLiftieDataForResort(marker.tag as StaticResortDataItem, this, loadingDialog)
                }
            }
        }
    }
}