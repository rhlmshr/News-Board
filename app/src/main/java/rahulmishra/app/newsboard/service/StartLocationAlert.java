package rahulmishra.app.newsboard.service;

import android.app.Activity;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import rahulmishra.app.newsboard.views.interfaces.GpsSuccessListener;

public class StartLocationAlert implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final int REQUEST_CHECK_SETTINGS = 109;
    private static final String TAG = "START_LOCATION_ALERT";
    Activity context;
    GoogleApiClient googleApiClient;
    GpsSuccessListener listener;

    public StartLocationAlert(Activity context, GpsSuccessListener gpsSuccessListener) {
        this.context = context;
        this.listener = gpsSuccessListener;
        googleApiClient = getInstance();
        if (googleApiClient != null) {
            settingsrequest();
            googleApiClient.connect();
        }
    }

    public GoogleApiClient getInstance() {
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(context).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        return mGoogleApiClient;
    }

    public void settingsrequest() {

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

//        PendingResult<LocationSettingsResult> result =
//                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        Task<LocationSettingsResponse> task =
                LocationServices.getSettingsClient(context).checkLocationSettings(builder.build());

        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                    listener.onGpsSuccess();
                    Log.e(TAG, "LocationSettingsStatusCodes.SUCCESS");
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            Log.e(TAG, "LocationSettingsStatusCodes.RESOLUTION_REQUIRED");
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                        context,
                                        REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            Log.e(TAG, "LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE");

                            break;
                    }
                }
            }
        });
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {


    /*    MainActivity mm = new MainActivity();
        mm.requestLocationUpdates();*/
        //Toast.makeText(context , "Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
