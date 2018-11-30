package com.isae.mohamad.mahallat.Classes.utilities;

import android.location.Location;
import com.google.android.gms.location.LocationListener;

import java.util.ArrayList;
import java.util.List;

import static com.isae.mohamad.mahallat.Classes.utilities.Constants.*;

/**
 * Created by mohamad on 10/30/2018.
 */

public class GeofencingManager implements LocationListener {

    private static final String ENTERED = "entered";
    private static final String EXITED = "exited";
    private static GeofencingManager ourInstance = new GeofencingManager();
    public GeofencingManagerCallbacks geofencingManagerCallbacks;
    List<StoreGeofence> mGeofenceList;

    private GeofencingManager() {
    }

    public GeofencingManager(GeofencingManagerCallbacks geofencingManagerCallbacks) {
        this.geofencingManagerCallbacks = geofencingManagerCallbacks;
        mGeofenceList = new ArrayList<StoreGeofence>();
    }

    public static GeofencingManager getInstance() {
        return ourInstance;
    }

    public void start(List<StoreGeofence> geofenceList)
    {
        this.mGeofenceList = geofenceList;
    }



    public void manageMonitoredRegions(Location currentUserLocation) {
        if (this.mGeofenceList != null && this.mGeofenceList.size() > 0) {
            //if there is no saved state or the user has just exited a monitored region check if he is approaching another region.
            for (StoreGeofence store : this.mGeofenceList) {
                //check if user is inside the region.
                boolean userInsideRegion = userInsideRegion(currentUserLocation, store.getLongitude(), store.getLatitude());
                if (userInsideRegion) {
                    //show the store.
                    handleRegionEvent(store.getId(), ENTERED);
                }
                else
                {
                    //hide the store.
                    handleRegionEvent(store.getId(), EXITED);
                }
            }
        }
    }

    public boolean userInsideRegion(Location userLocation, double longitude, double latitude) {
        Location coordinate = new Location("Point A");
        coordinate.setLongitude(longitude);
        coordinate.setLatitude(latitude);
        float distance = userLocation.distanceTo(coordinate);
        return distance < GEOFENCE_RADIUS;
    }

    public void handleRegionEvent(int storeId, String status) {

        if (status.equalsIgnoreCase(ENTERED)) {
            if (geofencingManagerCallbacks != null)
                geofencingManagerCallbacks.OnRegionStateUpdated(storeId,STATE.ENTERED);
        } else if (status.equalsIgnoreCase(EXITED)) {
            if (geofencingManagerCallbacks != null)
                geofencingManagerCallbacks.OnRegionStateUpdated(storeId, STATE.EXITED);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        manageMonitoredRegions(location);
    }

    public enum STATE {
        ENTERED,
        EXITED
    }

    public interface GeofencingManagerCallbacks {
        void OnRegionStateUpdated(int storeId, STATE state);
    }
}
