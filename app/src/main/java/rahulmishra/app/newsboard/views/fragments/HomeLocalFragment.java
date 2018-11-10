package rahulmishra.app.newsboard.views.fragments;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;
import java.util.Locale;

import rahulmishra.app.newsboard.R;
import rahulmishra.app.newsboard.service.StartLocationAlert;
import rahulmishra.app.newsboard.service.model.ArticleStructure;
import rahulmishra.app.newsboard.service.model.NewsResponse;
import rahulmishra.app.newsboard.util.GeneralUtils;
import rahulmishra.app.newsboard.viewmodel.NewsViewModel;
import rahulmishra.app.newsboard.views.adapters.DiscoverAdapter;
import rahulmishra.app.newsboard.views.interfaces.DbQueryListener;
import rahulmishra.app.newsboard.views.interfaces.GpsSuccessListener;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class HomeLocalFragment extends Fragment implements GpsSuccessListener,
        SwipeRefreshLayout.OnRefreshListener, DiscoverAdapter.OnSaveClickListener {

    private Context context;
    private LocationManager locationManager;
    private NewsViewModel newsViewModel;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout discoverRefresh;
    private String locationQuery;
    private DiscoverAdapter discoverAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        context = inflater.getContext();
        recyclerView = view.findViewById(R.id.discover_list);
        discoverRefresh = view.findViewById(R.id.discover_refresh);
        discoverAdapter = new DiscoverAdapter();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(discoverAdapter);

        discoverAdapter.setOnSaveClickListener(this);

        discoverRefresh.setColorSchemeResources(R.color.colorPrimaryDark);

        discoverRefresh.setRefreshing(true);

        checkLocationPermission();
    }

    @Override
    public void onGpsSuccess() {
        getCurrentAddress();
    }

    public void checkLocationPermission() {
        Dexter.withActivity((Activity) context).withPermissions
                (Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
//                    enableGPS();
                            StartLocationAlert startLocationAlert = new StartLocationAlert((Activity) context, HomeLocalFragment.this);

                        } else {
                            GeneralUtils.showToastMessage(context, "Location Permission Denied");
                            discoverRefresh.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    public void getCurrentAddress() {

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null) {

            try {
                if (Build.VERSION.SDK_INT >= 23 &&
                        context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 100, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                        if (location != null) {
//                            updateProfile.setLatitude(String.valueOf(location.getLatitude()));
//                            updateProfile.setLongitude(String.valueOf(location.getLongitude()));
                            getCityState(location);

                        }
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                GeneralUtils.showToastMessage(context, "Error fetching : " + e.toString());
                Log.d("locationerror", e.getMessage());
                discoverRefresh.setRefreshing(false);
            }

        } else {
            getCurrentAddress();
        }
    }

    private void getCityState(Location location) {

        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
//                String locality = addresses.get(0).getLocality();
                locationQuery = addresses.get(0).getAdminArea();
                discoverRefresh.setRefreshing(true);
                onRefresh();
            }

        } catch (Exception e) {
            e.printStackTrace();
            GeneralUtils.showToastMessage(context, "Error fetching : " + e.toString());
            Log.d("locationerror", e.getMessage());
            discoverRefresh.setRefreshing(false);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == StartLocationAlert.REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_CANCELED) {
                // The user was asked to change settings, but chose not to
                //finish();
                return;
            }
            if (resultCode == RESULT_OK) {
                // The user was asked to change settings, but chose not to
                getCurrentAddress();
            }
        }
    }

    @Override
    public void onRefresh() {
        newsViewModel.getSearchResult(locationQuery).observe(this, new Observer<NewsResponse>() {
            @Override
            public void onChanged(@Nullable NewsResponse newsResponse) {
                if (newsResponse != null) {
                    discoverAdapter.setmResources(newsResponse.getArticles());
                    Log.d("TAG123", "onChanged: " + newsResponse.getArticles().toString());
                }
                discoverRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void onSaveClicked(ArticleStructure articleStructure) {
        newsViewModel.storeArticle(articleStructure, new DbQueryListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "Article Archived!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
