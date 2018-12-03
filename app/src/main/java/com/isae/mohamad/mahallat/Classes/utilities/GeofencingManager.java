package com.isae.mohamad.mahallat.Classes.utilities;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.Marker;

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
    private static int RADIUS = 500;//meters
    private static int UpdateDuration = 5000;//seconds
    private static int UpdateDistance = 1;//meters
    public boolean LocationServicesOn = false;
    public GeofencingManagerCallbacks geofencingManagerCallbacks;
    private LocationManager locationManager;
    private Context context;
    List<Geofence> mGeofenceList;
    private ArrayList<Marker> mCameras;

    // The SharedPreferences object in which geofences are stored.
    private final SharedPreferences mPrefs;
    // The name of the SharedPreferences.
    private static final String SHARED_PREFERENCES = "SharedPreferences";
    private GeofencingManager() {
    }

    public GeofencingManager(Context context) {
        this.context = context;
        mGeofenceList = new ArrayList<Geofence>();
        mPrefs = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static GeofencingManager getInstance() {
        return ourInstance;
    }

    public void start(Context context) {
        this.context = context;
    }



    public void manageMonitoredRegions(Location currentUserLocation) {
        if (this.mCameras != null) {
            //cameras are loaded so lets start monitoring.
            //check if there is a currently saved state .
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            int savedRegionIdentifier = sharedPreferences.getInt("saved_state_identifier", -1);
            if (savedRegionIdentifier != -1) {
                //get camera for saved state id.
                Camera camera = getCameraWithId(savedRegionIdentifier);
                if (camera != null) {
                    //check if user is still in the bounds of the currently saved state.
                    boolean userInsideRegion = userInsideRegion(currentUserLocation, camera.Longitude, camera.Latitude);
                    if (userInsideRegion) {
                        handleRegionEvent(camera, ENTERED);
                        //dont show the map tip.
                        editor.putBoolean(Constants.SHOW_MAP_TIP, false);
                        editor.apply();
                        return;
                    }
                    editor.putBoolean(Constants.SHOW_MAP_TIP, true);
                    editor.putInt("saved_state_identifier", -1);
                    editor.apply();
                    handleRegionEvent(camera, EXITED);
                }
            }
            //if there is no saved state or the user has just exited a monitored region check if he is approaching another region.
            for (Camera camera : this.mCameras) {
                //check if user is inside the region.
                boolean userInsideRegion = userInsideRegion(currentUserLocation, camera.Longitude, camera.Latitude);
                if (userInsideRegion) {
                    //dont show the map tip.
                    editor.putBoolean(Constants.SHOW_MAP_TIP, false);
                    editor.putInt("saved_state_identifier", camera.CameraID);
                    editor.apply();
                    handleRegionEvent(camera, ENTERED);
                }
            }
        }
    }

    public Camera getCameraWithId(int id) {
        for (Camera camera : this.mCameras) {
            if (camera.CameraID == id)
                return camera;
        }
        return null;
    }

    /**
     * Returns a stored geofence by its id, or returns null if it's not found.
     * @param id The ID of a stored geofence.
     * @return A Geofence defined by its center and radius, or null if the ID is invalid.
     */
    public StoreGeofence getGeofence(String id) {
        // Get the latitude for the geofence identified by id, or INVALID_FLOAT_VALUE if it doesn't
        // exist (similarly for the other values that follow).
        double lat = mPrefs.getFloat(getGeofenceFieldKey(id, KEY_LATITUDE),
                INVALID_FLOAT_VALUE);
        double lng = mPrefs.getFloat(getGeofenceFieldKey(id, KEY_LONGITUDE),
                INVALID_FLOAT_VALUE);
        float radius = mPrefs.getFloat(getGeofenceFieldKey(id, KEY_RADIUS),
                INVALID_FLOAT_VALUE);
        long expirationDuration =
                mPrefs.getLong(getGeofenceFieldKey(id, KEY_EXPIRATION_DURATION),
                        INVALID_LONG_VALUE);
        int transitionType = mPrefs.getInt(getGeofenceFieldKey(id, KEY_TRANSITION_TYPE),
                INVALID_INT_VALUE);
        // If none of the values is incorrect, return the object.
        if (lat != INVALID_FLOAT_VALUE
                && lng != INVALID_FLOAT_VALUE
                && radius != INVALID_FLOAT_VALUE
                && expirationDuration != INVALID_LONG_VALUE
                && transitionType != INVALID_INT_VALUE) {
            return new StoreGeofence(id, lat, lng, radius, expirationDuration, transitionType);
        }
        // Otherwise, return null.
        return null;
    }

    /**
     * Save a geofence.
     * @param geofence with the values you want to save in SharedPreferences.
     */
    public void setGeofence(String id, StoreGeofence geofence) {
        // Get a SharedPreferences editor instance. Among other things, SharedPreferences
        // ensures that updates are atomic and non-concurrent.
        //geofence.getClass().
        SharedPreferences.Editor prefs = mPrefs.edit();
        // Write the Geofence values to SharedPreferences.
        prefs.putFloat(getGeofenceFieldKey(id, KEY_LATITUDE), (float) geofence.getLatitude());
        prefs.putFloat(getGeofenceFieldKey(id, KEY_LONGITUDE), (float) geofence.getLongitude());
        prefs.putFloat(getGeofenceFieldKey(id, KEY_RADIUS), geofence.getRadius());
        prefs.putLong(getGeofenceFieldKey(id, KEY_EXPIRATION_DURATION),
                geofence.getExpirationDuration());
        prefs.putInt(getGeofenceFieldKey(id, KEY_TRANSITION_TYPE),
                geofence.getTransitionType());
        // Commit the changes.
        prefs.commit();
    }

    /**
     * Given a Geofence object's ID and the name of a field (for example, KEY_LATITUDE), return
     * the key name of the object's values in SharedPreferences.
     * @param id The ID of a Geofence object.
     * @param fieldName The field represented by the key.
     * @return The full key name of a value in SharedPreferences.
     */
    private String getGeofenceFieldKey(String id, String fieldName) {
        return KEY_PREFIX + "_" + id + "_" + fieldName;
    }

    public boolean userInsideRegion(Location userLocation, double longitude, double latiitude) {
        Location coordinate = new Location("Pont A");
        coordinate.setLongitude(longitude);
        coordinate.setLatitude(latiitude);
        float distance = userLocation.distanceTo(coordinate);
        return distance < RADIUS;
    }

    public void handleRegionEvent(Camera camera, String status) {
        boolean connected = Utils.getInstance().isInternetConnected(context);
        if (!camera.isActive || !connected) {
            status = EXITED;
        }
        if (status.equalsIgnoreCase(ENTERED)) {
            //user entered camera region check app status.
            if (geofencingManagerCallbacks != null)
                geofencingManagerCallbacks.OnRegionStateUpdated(STATE.ENTERED);

            Utils.getInstance().saveInt(camera.CameraID, Constants.SELECTED_CAM_ID, context);
            //TODO : download the image on different thread and cache it to preferences,
        } else if (status.equalsIgnoreCase(EXITED)) {
            if (geofencingManagerCallbacks != null)
                geofencingManagerCallbacks.OnRegionStateUpdated(STATE.EXITED);
            Utils.getInstance().saveInt(-1, Constants.SELECTED_CAM_ID, context);
        }
    }

    public void stop() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (locationManager == null) {
            locationManager.removeUpdates(this);
            LocationServicesOn = false;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        manageMonitoredRegions(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        if(geofencingManagerCallbacks!=null){
            geofencingManagerCallbacks.OnProviderDisabled(provider);
        }
    }

    @Override
    public void camerasCallback(List<Camera> Camera) {
        if (Camera.size() > 0) {
            this.mCameras = new ArrayList<>(Camera);
            Utils.getInstance().saveCameras(this.mCameras, Constants.allCamerasUrl, context);
        }
    }

    public enum STATE {
        ENTERED,
        EXITED
    }

    public interface GeofencingManagerCallbacks {
        void OnRegionStateUpdated(STATE state);

        void OnProviderDisabled(String provider);
    }
}
