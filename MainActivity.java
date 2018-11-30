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
import android.support.design.widget.NavigationView;
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
import com.isae.mohamad.mahallat.Classes.utilities.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static com.isae.mohamad.mahallat.Classes.utilities.Constants.GEOFENCE_RADIUS;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener,OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener,ResultCallback<Status>, GeofencingManager.GeofencingManagerCallbacks {

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

    private static final String mCurrLocationTitle = "You are here!";

    private DrawerLayout mDrawerLayout;

    private GeofencingManager mGeofencingManager;
    private static final long GEO_DURATION = 60 * 60 * 1000;
    private static final String GEOFENCE_REQ_ID = "My Geofence";
    private final int GEOFENCE_REQ_CODE = 0;
    private Marker geoFenceMarker;
    // Stores the PendingIntent used to request geofence monitoring.
    private PendingIntent geoFencePendingIntent;
    HashMap<Integer ,Store> mStores;
    HashMap<Integer ,Marker> mStoresMarkers;

    FloatingActionButton mLoginButton;

    APIInterface apiInterface;
    /**
     * Create a PendingIntent that triggers GeofenceTransitionIntentService when a geofence
     * transition occurs.
     */
    private PendingIntent getGeofenceTransitionPendingIntent() {
        Intent intent = new Intent(this, GeofenceTransitionService.class);
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
            setContentView(R.layout.activity_main);
            final Context context = getApplicationContext();
            // Construct a FusedLocationProviderClient.
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

            mLoginButton = findViewById(R.id.fab);
            mLoginButton.setOnClickListener(new View.OnClickListener() {
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
                    getDeviceLocation();
                }
            });
            mFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mFragment.getMapAsync(this);

            mDrawerLayout = findViewById(R.id.drawer_layout);

            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.menu);

            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                            // set item as selected to persist highlight
                            menuItem.setChecked(true);
                            // close drawer when item is tapped
                            mDrawerLayout.closeDrawers();
                            switch (menuItem.getItemId())
                            {
                                case R.id.nav_profile:
                                    Toast.makeText(MainActivity.this, menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                                case R.id.nav_favorites:
                                    Toast.makeText(MainActivity.this, menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                                case R.id.nav_settings:
                                    Toast.makeText(MainActivity.this, menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                                case R.id.nav_logout:
                                    // Remove Credentials and IsLoggedIn
                                    MyApplication.EraseCredentials();
                                    mLoginButton.setVisibility(View.VISIBLE);
                            }

                            return true;
                        }
                    });


            // Instantiate a new geofence storage area.
            mGeofencingManager = new GeofencingManager(this);

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
                if(!mDrawerLayout.isDrawerOpen(GravityCompat.START))
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
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        buildGoogleApiClient();

        loadStores();

        mGoogleApiClient.connect();
        try {
            getDeviceLocation();
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
            mLocationPermissionGranted = true;
        }
    }

    private void showDefaultLocation() {
        Toast.makeText(this, "Location permission not granted, " + "showing default location", Toast.LENGTH_SHORT).show();
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

                            //remove previous current location marker and add new one at current position
                            if (mCurrLocation != null) {
                                mCurrLocation.remove();
                            }

                            mCurrLocation = CreateMarker(redmond, mCurrLocationTitle, false);

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

        if(MyApplication.IsLoggedIn())
            mLoginButton.setVisibility(View.INVISIBLE);

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
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            markerForGeofence(latLng);


            CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
            mMap.setInfoWindowAdapter(customInfoWindow);

            //remove previous current location marker and add new one at current position
            if (mCurrLocation != null) {
                mCurrLocation.remove();
            }

            InfoWindowData info = new InfoWindowData();
            info.setId(0);

            mCurrLocation = CreateMarker(latLng, mCurrLocationTitle, false);
            mCurrLocation.setTag(info);
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

        mCurrLocation = CreateMarker(latLng, mCurrLocationTitle, false);

        markerForGeofence(latLng);
        mGeofencingManager.manageMonitoredRegions(location);

        Log.d(TAG,"Location Changed");
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        InfoWindowData info = (InfoWindowData) marker.getTag();
        if(info.getId() > 0)
        {
            Intent StoreIntent = new Intent(getApplicationContext(), StoreActivity.class);
            StoreIntent.putExtra("Store", mStores.get(info.getId()));
            startActivity(StoreIntent);
        }
    }

    public void loadStores() {

        /*Create handle for the RetrofitInstance interface*/
        apiInterface = APIClient.getClient().create(APIInterface.class);

        /*Call the method in the interface to get the store data*/
        Call<List<Store>> call  = apiInterface.doGetStoreList();

        call.enqueue(new Callback<List<Store>>() {
            @Override
            public void onResponse(Call<List<Store>> call, retrofit2.Response<List<Store>> response) {
                if (response.isSuccessful() ) {
                    // Create GeoFences will fill the Stores HashMap And the Store Marker HashMap
                    mGeofencingManager.start(createGeofences(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<Store>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
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

        Intent intent = new Intent( this, GeofenceTransitionService.class);
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

    private void markerForGeofence(LatLng position) {

        if ( mMap!=null ) {
            // Remove last geoFenceMarker
            if (geoFenceMarker != null)
                geoFenceMarker.remove();

            geoFenceMarker = CreateMarker(position, mCurrLocationTitle, false);
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

    public List<StoreGeofence> createGeofences(List<Store> stores) {
        // return StoreGeoFence for the GeoFenceManager Start();
        List<StoreGeofence> storeGeofenceList = new ArrayList<StoreGeofence>();

        // Fill Stores HashMap AND Stores Markers HashMap
        mStoresMarkers = new HashMap<Integer, Marker>();
        mStores = new HashMap<Integer, Store>();
        for(Store store : stores)
        {
            // Create internal "flattened" objects containing the geofence data.
            StoreGeofence storeGeofence =  new StoreGeofence(
                store.getId(),store.getLatitude(), store.getLongitude(), GEOFENCE_RADIUS,GEO_DURATION,
                Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT);

            storeGeofenceList.add(storeGeofence);
            this.mStoresMarkers.put(store.getId(), getMarkerForStore(store));
            this.mStores.put(store.getId(), store);
        }
        return storeGeofenceList;
    }

    private Marker getMarkerForStore(Store store)
    {
        Marker marker = CreateMarker(new LatLng(store.getLatitude(),store.getLongitude()), store.getName(), true);

        InfoWindowData info = new InfoWindowData();
        info.setId(store.getId());
        info.setImage(store.getImage());
        info.setRating(String.valueOf(store.getRating()));
        info.setLikes(String.valueOf(store.getLikes()));
        info.setCategory(store.getCategory().getName());

        marker.setTag(info);

        return marker;
    }

    private Marker CreateMarker(LatLng position, String title, boolean isStore)
    {
        Marker marker ;
        MarkerOptions markerOptions = new MarkerOptions()
                    .position(position)
                    .title(title);
        if(!isStore)
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_dot));

        marker = mMap.addMarker(markerOptions);

        return marker;
    }

    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }


    @Override
    public void OnRegionStateUpdated(final int storeId, GeofencingManager.STATE state) {
        if(state.name() == "EXITED") {
            if (mStoresMarkers.containsKey(storeId)) {
                // Remove Marker from the map
                //mStoresMarkers.get(storeId).remove();
                // Remove the marker from the HashMap
                mStoresMarkers.remove(storeId);
            }
        }
        else // State = Entered
        {
            if(!mStoresMarkers.containsKey(storeId))
            {
                //******************* for API Level 24 ONLY ************************
                //mStoresList.stream().filter(s ->s.getId() == storeId).findFirst();

                if(mStores.containsKey(storeId))
                {
                    //Add the Marker to the Map and keep track of it with the HashMap
                    //getMarkerForStore returns the Marker object
                    this.mStoresMarkers.put(storeId, getMarkerForStore(mStores.get(storeId)));
                }
            }
        }
        Toast.makeText(this,state.name(),Toast.LENGTH_SHORT).show();
    }
}