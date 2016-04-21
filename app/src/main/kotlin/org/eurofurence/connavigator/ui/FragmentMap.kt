package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.eurofurence.connavigator.R


/**
 * Created by David on 4/21/2016.
 */
class FragmentMap() : Fragment(), OnMapReadyCallback {

    override fun onMapReady(newMap: GoogleMap?) {
        // Set map typ
        newMap?.mapType = GoogleMap.MAP_TYPE_NORMAL

        // Center camera on berlin
        newMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(52.5243700, 13.4105300), 10F))

        // Place marker at estrel
        newMap?.addMarker(MarkerOptions()
                .position(LatLng(52.474167, 13.459444))
                .title("Estrel Berlin")
        )

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_view_map, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Analytics.changeScreenName("View Map")

        val map = activity.fragmentManager.findFragmentById(R.id.internalMapFragment) as MapFragment

        map.getMapAsync(this)
    }
}