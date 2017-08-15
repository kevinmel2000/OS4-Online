package com.os4.ecb.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.os4.ecb.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.os4.ecb.service.aidl.IPDXServiceExt;
import com.os4.ecb.service.aidl.IPDXServiceListenerExt;

public class PDXMapActivity extends BaseActivity implements OnMapReadyCallback {

    private static final String TAG = PDXMapActivity.class.getName();
    private IPDXServiceExt pdxService = null;
    private GoogleMap mMap;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 50f, locationListener);
    }

    private LocationListener locationListener = new LocationListener() {

        public void onLocationChanged(Location loc) {
            Log.i(TAG,"Update Location = onLocationChanged");
            if (loc != null) {
                Double latitudePoint = loc.getLatitude();
                Double longitudePoint = loc.getLongitude();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(latitudePoint,longitudePoint)));
                mMap.addMarker(new MarkerOptions().position(new LatLng(latitudePoint,longitudePoint)).title("Me"));
            }
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause .....");
        mConnection.onServiceDisconnected(null);
        unbindService(mConnection);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(new Intent(IPDXServiceExt.class.getName()),mConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            pdxService = IPDXServiceExt.Stub.asInterface(service);
            try{
                if(pdxService!=null)pdxService.addServiceListener(serviceListener);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        public void onServiceDisconnected(ComponentName className) {
            try{
                if(pdxService!=null) pdxService.removeServiceListener(serviceListener);
            }catch(Exception e){e.printStackTrace();}
            pdxService = null;
        }
    };

    private IPDXServiceListenerExt serviceListener = new IPDXServiceListenerExt.Stub() {

        @Override
        public void onConnected(String sessionId) throws RemoteException {}
        @Override
        public void onDisconnected(String reason) throws RemoteException {}
        @Override
        public void onRegistered(String json) throws RemoteException {}
        @Override
        public void onAuthorized(String json) throws RemoteException {}
        @Override
        public void onGetRosters(String json) throws RemoteException {}
        @Override
        public void onPresence(String json) throws RemoteException {}
        @Override
        public void onPresenceGroup(String json) throws RemoteException {}
        @Override
        public void onUpdateAvatar(String json) throws RemoteException {}
        @Override
        public void onUpdateService(String json) throws RemoteException {}
        @Override
        public void onUpdateGroup(String json) throws RemoteException {}
        @Override
        public void onUpdateParticipant(String json) throws RemoteException {}
        @Override
        public void onUpdateServiceInfo(String json) throws RemoteException {}
        @Override
        public void onUpdateGroupInfo(String json) throws RemoteException {}
        @Override
        public void onSubscribe(String json) throws RemoteException {}
        @Override
        public void onUnSubscribe(String json) throws RemoteException {}
        @Override
        public void onAddRoster(String json) throws RemoteException {}
        @Override
        public void onUpdateRoster(String json) throws RemoteException {}
        @Override
        public void onDeleteRoster(String json) throws RemoteException {}
        @Override
        public void onMessage(String json) throws RemoteException {}
        @Override
        public void onMessageGroup(String json) throws RemoteException {}
        @Override
        public void onMessageParticipant(String json) throws RemoteException {}
        @Override
        public void onMessageState(String json) throws RemoteException {}
        @Override
        public void onInvitation(String json) throws RemoteException {}
        @Override
        public void onGetAffiliation(String json) throws RemoteException {}
        @Override
        public void onPing(String json) throws RemoteException {}
        @Override
        public void onTransferFile(String json) throws RemoteException {}
        @Override
        public void onIQPacket(String json) throws RemoteException {}
        @Override
        public void onMessagePacket(String json) throws RemoteException {}
        @Override
        public void onErrorResponse(String error) throws RemoteException {}
        @Override
        public void onSignOut() throws RemoteException {
            finish();
        }
    };

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapRead");
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnCameraMoveListener(new OnCameraMoveListener(){
            @Override
            public void onCameraMove() {
                try {
                    Thread.sleep(1);
                }catch(Exception e){
                    Log.e(TAG,e.getMessage());
                }
            }
        });
        LatLng ciomas = new LatLng(-6.6065326, 106.7554617);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ciomas));
        mMap.addMarker(new MarkerOptions().position(ciomas).title("Marker in Bogor"));
    }
}
