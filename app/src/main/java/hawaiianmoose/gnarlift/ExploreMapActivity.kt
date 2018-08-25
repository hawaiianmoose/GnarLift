package hawaiianmoose.gnarlift

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.support.v4.content.ContextCompat
import android.support.v4.app.ActivityCompat
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

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true)
            googleMap.getUiSettings().setMyLocationButtonEnabled(true)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1337)
        }

        addResortMarkers(mMap)

        val gps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val currentLocation = LatLng(gps.latitude, gps.longitude)
        val locationMarker = mMap.addMarker(MarkerOptions().position(currentLocation).title(getString(R.string.current_location)))
        locationMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
        locationMarker.showInfoWindow()

        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(6f))
    }

    fun addResortMarkers(mMap: GoogleMap) {
        for (resort in staticData.resorts) {
            val resortLocation = LatLng(resort.latitude, resort.longitude)
            val locationMarker = mMap.addMarker(MarkerOptions().position(resortLocation).title(resort.name))
            locationMarker.showInfoWindow()
            locationMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            locationMarker.tag = resort

            mMap.setOnInfoWindowClickListener { marker ->
                val loadingDialog = LoadingDialog()
                loadingDialog.show(fragmentManager, "loading_dialog_fragment")

                LiftieServiceUtil.getLiftieDataForResort(marker.tag as StaticResortDataItem, this, loadingDialog)
            }
        }
    }
}