package doron_vaskoinc.exifeditor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    private GoogleMap gMap;
    ImageDatabase imageDB;
    public ArrayList<Double> latitudes = new ArrayList<>();
    public ArrayList<Double> longitudes = new ArrayList<>();
    public ArrayList<String> filenames = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationClickListener(this);

        LatLng umb = new LatLng(42.18463,-71.02114);




        imageDB = new ImageDatabase(this);
        Cursor initCursor = imageDB.getAllData();
        Log.v("Doron", ""+initCursor.getCount());
        initCursor.moveToFirst();
        for (int i = 0; i < initCursor.getCount(); i++) {
            Double iLat = initCursor.getDouble(initCursor.getColumnIndex("LAT"));
            String latRef = initCursor.getString(initCursor.getColumnIndex("LATREF"));
            if ((latRef != null) && latRef.toUpperCase().equals("S")) {
                iLat *= -1;
            }
            Double iLon = initCursor.getDouble(initCursor.getColumnIndex("LON"));
            String lonRef = initCursor.getString(initCursor.getColumnIndex("LONREF"));
            if ((lonRef != null) && lonRef.toUpperCase().equals("W")) {
                iLon *= -1;
            }
            Log.v("Doron", ""+iLat+" , "+iLon);
            String filename = initCursor.getString(initCursor.getColumnIndex("FILENAME"));
            if (iLat != null && iLon != null && filename != null) {
                latitudes.add(iLat);
                longitudes.add(iLon);
                filenames.add(filename);
                initCursor.moveToNext();
            }
        }
        for (int i = 0; i < latitudes.size(); i++) {
            double iLat = latitudes.get(i);
            double iLon = longitudes.get(i);
            if (iLat != 0.0 && iLon != 0.0) {
                LatLng coordinate = new LatLng(iLat, iLon);
                googleMap.addMarker(new MarkerOptions()
                        .position(coordinate)
                        .title(filenames.get(i))
                );

            }
        }
        // Position the map.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(umb, 10));
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location: \n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

}
