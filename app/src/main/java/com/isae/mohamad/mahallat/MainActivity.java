package com.isae.mohamad.mahallat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.isae.mohamad.mahallat.Classes.Store;
import com.isae.mohamad.mahallat.Classes.utilities.Constants;
import com.isae.mohamad.mahallat.Classes.utilities.CustomInfoWindowGoogleMap;
import com.isae.mohamad.mahallat.Classes.utilities.GeofenceTrasitionService;
import com.isae.mohamad.mahallat.Classes.utilities.GeofencingManager;
import com.isae.mohamad.mahallat.Classes.utilities.InfoWindowData;
import com.isae.mohamad.mahallat.Classes.utilities.StoreGeofence;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnMarkerClickListener,GoogleMap.OnInfoWindowClickListener,ResultCallback<Status> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mLocationPermissionGranted;
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    LatLng latLng;
    GoogleMap mMap;
    SupportMapFragment mFragment;
    Marker mCurrLocation;

    private DrawerLayout mDrawerLayout;

    private GeofencingManager mGeofencingManager;
    private static final long GEO_DURATION = 60 * 60 * 1000;
    private static final String GEOFENCE_REQ_ID = "My Geofence";
    private static final float GEOFENCE_RADIUS = 500.0f; // in meters
    private final int GEOFENCE_REQ_CODE = 0;
    private Marker geoFenceMarker;
    // Stores the PendingIntent used to request geofence monitoring.
    private PendingIntent mGeofenceRequestIntent;
    RequestQueue requestQueue;
    List<Geofence> mGeofenceList;

    /**
     * Create a PendingIntent that triggers GeofenceTransitionIntentService when a geofence
     * transition occurs.
     */
    private PendingIntent getGeofenceTransitionPendingIntent() {
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static Intent makeNotificationIntent(Context context, String msg) {
        Intent intent = new Intent( context, MainActivity.class );
        intent.putExtra( "Notification", msg );
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_maps);
            final Context context = getApplicationContext();
            // Construct a FusedLocationProviderClient.
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent LoginIntent = new Intent(context, LoginActivity.class);
                    startActivity(LoginIntent);
                }
            });

            FloatingActionButton location = findViewById(R.id.location);
            location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowMyLocation();
                }
            });
            mFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mFragment.getMapAsync(this);

            mDrawerLayout = findViewById(R.id.drawer_layout);

            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_launcher_background);

            requestQueue = Volley.newRequestQueue(context);

            // Instantiate a new geofence storage area.
            mGeofencingManager = new GeofencingManager(context);
            mGeofenceList = new ArrayList<Geofence>();
            loadStores();

            /*mapFragment.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    if(mCircle == null || mMarker == null){
                        drawMarkerWithCircle(latLng);
                    }else{
                        updateMarkerWithCircle(latLng);
                    }
                }
            });*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
                mDrawerLayout.openDrawer(GravityCompat.START);
                else
                    mDrawerLayout.closeDrawers();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        enableMyLocationIfPermitted();

        mMap.getUiSettings().setZoomControlsEnabled(true);
        //mMap.setMinZoomPreference(11);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        buildGoogleApiClient();

        mGoogleApiClient.connect();
        try {
            ShowMyLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

    }

    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            mLocationPermissionGranted = true;
        } else if (mMap != null) {
            //mMap.setMyLocationEnabled(true);
            mLocationPermissionGranted = true;
        }
    }

    private void showDefaultLocation() {
        Toast.makeText(this, "Location permission not granted, " +
                        "showing default location",
                Toast.LENGTH_SHORT).show();
        LatLng redmond = new LatLng(47.6739881, -122.121512);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(redmond));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocationIfPermitted();
                } else {
                    showDefaultLocation();
                }
                return;
            }

        }
    }

    public void ShowMyLocation() {
        //mMap.setMinZoomPreference(12);
        getDeviceLocation();
        /*CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(new LatLng(location.getLatitude(),
                location.getLongitude()));

        circleOptions.radius(200);
        circleOptions.fillColor(Color.RED);
        circleOptions.strokeWidth(6);

        mMap.addCircle(circleOptions);*/
    }

    private Location getDeviceLocation() {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            if (mLastKnownLocation == null) {
                                mLastKnownLocation = new Location("");
                                mLastKnownLocation.setLatitude(mDefaultLocation.latitude);
                                mLastKnownLocation.setLongitude(mDefaultLocation.longitude);
                            }
                            LatLng redmond = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());

                            CircleOptions circleOptions = new CircleOptions();
                            circleOptions.center(redmond);
                            circleOptions.radius(200);
                            circleOptions.fillColor(Color.BLUE);
                            circleOptions.strokeWidth(1);

                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(redmond);
                            mMap.addMarker(markerOptions.title("You are here"));

                            mMap.moveCamera(CameraUpdateFactory.newLatLng(redmond));

                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            //mLastKnownLocation = mdefaultlocation;
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
        return mLastKnownLocation;
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        InfoWindowData info = new InfoWindowData();
info = (InfoWindowData)marker.getTag();
        // Retrieve the data from the marker.
        /*Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(this,
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();
        }*/
        try {
            if (marker.isInfoWindowShown())
                marker.hideInfoWindow();
            marker.showInfoWindow();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return true;
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this,"Resuming",Toast.LENGTH_SHORT).show();
        if(mGoogleApiClient != null) {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(5000); //5 second
            mLocationRequest.setFastestInterval(3000); //3 second
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //Unregister for location callbacks:
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            //place marker at current position
            mMap.setMinZoomPreference(11);
            //mMap.clear();
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            markerForGeofence(latLng);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            InfoWindowData info = new InfoWindowData();
            info.setImage("https://nyoobserver.files.wordpress.com/2015/12/unnamed2.jpg");
            info.setRating("2");
            info.setLikes("20");
            info.setCategory("Restaurant");

            CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
            mMap.setInfoWindowAdapter(customInfoWindow);
            mCurrLocation = mMap.addMarker(markerOptions);
            mCurrLocation.setTag(info);
            mCurrLocation.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 second
        mLocationRequest.setFastestInterval(3000); //3 second
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {

        //remove previous current location marker and add new one at current position
        if (mCurrLocation != null) {
            mCurrLocation.remove();
        }
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocation = mMap.addMarker(markerOptions);

        markerForGeofence(latLng);


        Log.d(TAG,"Location Changed");

        //If you only need one location, unregister the listener
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent StoreIntent = new Intent(getApplicationContext(), StoreActivity.class);
        startActivity(StoreIntent);
    }

    public void loadStores() {

        JsonArrayRequest arrReq = new JsonArrayRequest(Request.Method.GET, Constants.Get_All_Stores_API,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if (response.length() > 0) {
                            ArrayList<Store> stores = new ArrayList<Store>();
                            stores = Store.fromJson(response);
                            createGeofences(stores);
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("Volley", error.toString());
                    }
                }
        );
        requestQueue.add(arrReq);
    }

    // Create a Geofence
    private Geofence createGeofence(LatLng latLng, float radius ) {
        Log.d(TAG, "createGeofence");
        return new Geofence.Builder()
                .setRequestId(GEOFENCE_REQ_ID)
                .setCircularRegion( latLng.latitude, latLng.longitude, radius)
                .setExpirationDuration( GEO_DURATION )
                .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT )
                .build();
    }

    // Create a Geofence Request
    private GeofencingRequest createGeofenceRequest( Geofence geofence ) {
        Log.d(TAG, "createGeofenceRequest");
        return new GeofencingRequest.Builder()
                .setInitialTrigger( GeofencingRequest.INITIAL_TRIGGER_ENTER )
                .addGeofence( geofence )
                .build();
    }

    private PendingIntent createGeofencePendingIntent() {
        Log.d(TAG, "createGeofencePendingIntent");
        if ( geoFencePendingIntent != null )
            return geoFencePendingIntent;

        Intent intent = new Intent( this, GeofenceTrasitionService.class);
        return PendingIntent.getService(
                this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT );
    }

    // Add the created GeofenceRequest to the device's monitoring list
    private void addGeofence(GeofencingRequest request) {
        Log.d(TAG, "addGeofence");
        if (checkPermission())
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    request,
                    createGeofencePendingIntent()
            ).setResultCallback(this);
    }
    @Override
    public void onResult(@NonNull Status status) {
        Log.i(TAG, "onResult: " + status);
        if ( status.isSuccess() ) {
            drawGeofence();
        } else {
            // inform about fail
        }
    }

    // Draw Geofence circle on GoogleMap
    private Circle geoFenceLimits;
    private void drawGeofence() {
        Log.d(TAG, "drawGeofence()");

        if ( geoFenceLimits != null )
            geoFenceLimits.remove();

        CircleOptions circleOptions = new CircleOptions()
                .center( geoFenceMarker.getPosition())
                .strokeColor(Color.argb(50, 70,70,70))
                .fillColor( Color.argb(100, 150,150,150) )
                .radius( GEOFENCE_RADIUS );
        geoFenceLimits = mMap.addCircle( circleOptions );
    }

    private void markerForGeofence(LatLng latLng) {
        Log.i(TAG, "markerForGeofence("+latLng+")");
        String title = latLng.latitude + ", " + latLng.longitude;
        // Define marker options
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .title(title);
        if ( mMap!=null ) {
            // Remove last geoFenceMarker
            if (geoFenceMarker != null)
                geoFenceMarker.remove();

            geoFenceMarker = mMap.addMarker(markerOptions);
            startGeofence();
        }
    }

    // Start Geofence creation process
    private void startGeofence() {
        Log.i(TAG, "startGeofence()");
        if( geoFenceMarker != null ) {
            Geofence geofence = createGeofence( geoFenceMarker.getPosition(), GEOFENCE_RADIUS );
            GeofencingRequest geofenceRequest = createGeofenceRequest( geofence );
            addGeofence( geofenceRequest );
        } else {
            Log.e(TAG, "Geofence marker is null");
        }
    }

    public void createGeofences(ArrayList<Store> stores) {
        for(Store store : stores)
        {
            // Create internal "flattened" objects containing the geofence data.
            StoreGeofence storeGeofence =  new StoreGeofence(
                store.getId(),store.getLatitude(), store.getLongitude(), GEOFENCE_RADIUS,GEO_DURATION,
                Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT);

            // Store these flat versions in SharedPreferences and add them to the geofence list.
            mGeofencingManager.setGeofence(store.getId(), storeGeofence);
            mGeofenceList.add(storeGeofence.toGeofence());
        }
    }

    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }
}