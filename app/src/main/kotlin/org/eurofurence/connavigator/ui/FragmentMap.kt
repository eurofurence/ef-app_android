package org.eurofurence.connavigator.ui

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.util.extensions.applyOnRoot


/**
 * Created by David on 4/21/2016.
 */
class FragmentMap() : Fragment(), OnMapReadyCallback {

    override fun onMapReady(newMap: GoogleMap) {
        if (requestMapPermission())
        // Show current location
            newMap.isMyLocationEnabled = true

        // Set map typ
        newMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        // Center camera on berlin
        newMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(52.5243700, 13.4105300), 10F))

        // Place marker at estrel
        newMap.addMarker(MarkerOptions()
                .position(LatLng(52.474167, 13.459444))
                .title("Estrel Berlin")
        )

        newMap.uiSettings?.setAllGesturesEnabled(true)
    }

    private fun requestMapPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show a message to the user
                val dialog = Dialog(activity)
                dialog.setTitle("We'd like to show you where you are on the map!")
                dialog.show()
            } else {
                //Request permissions
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
            }
        }
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_map, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Analytics.changeScreenName("View Map")

        applyOnRoot { changeTitle("Around Eurofurence") }

        val mapFragment = SupportMapFragment.newInstance();
        childFragmentManager.beginTransaction()
                .replace(R.id.internalMapFragment, mapFragment)
                .commit()

        mapFragment.getMapAsync { onMapReady(it) }
    }
}