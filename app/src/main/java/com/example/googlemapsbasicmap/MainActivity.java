package com.example.googlemapsbasicmap;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap myMap;

    private LatLng casaPadres;

    private LatLngBounds allBounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Botones
        Button btn3d = findViewById(R.id.btn3d);
        Button btnReset = findViewById(R.id.btnReset);

        btn3d.setOnClickListener(v -> go3D());
        btnReset.setOnClickListener(v -> resetBounds());
    }



    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        UiSettings ui = myMap.getUiSettings();
        ui.setZoomControlsEnabled(true);
        ui.setMapToolbarEnabled(true);
        ui.setRotateGesturesEnabled(true);
        ui.setTiltGesturesEnabled(true);
        myMap.setBuildingsEnabled(true);

        casaPadres = new LatLng(-17.774904, -63.206342);
        LatLng abuelosPaterno = new LatLng(-17.774237, -63.206431);
        LatLng abuelosMaterno = new LatLng(-17.730312, -63.117127);
        LatLng tioReynaldo   = new LatLng(-17.783282, -63.202879);

        String[] nombres = new String[] {
                "Casa (Padres)",
                "Abuelos (paterno)",
                "Abuelos (materno)",
                "Tío Reynaldo (paterno)"
        };

        LatLng[] puntos = new LatLng[] {
                casaPadres, abuelosPaterno, abuelosMaterno, tioReynaldo
        };

        float[] colores = new float[] {
                BitmapDescriptorFactory.HUE_ORANGE,
                BitmapDescriptorFactory.HUE_GREEN,
                BitmapDescriptorFactory.HUE_AZURE,
                BitmapDescriptorFactory.HUE_ROSE
        };

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < puntos.length; i++) {
            myMap.addMarker(new MarkerOptions()
                    .position(puntos[i])
                    .title(nombres[i])
                    .icon(BitmapDescriptorFactory.defaultMarker(colores[i])));
            builder.include(puntos[i]);
        }
        allBounds = builder.build();

        myMap.setOnMapLoadedCallback(() ->
                myMap.animateCamera(CameraUpdateFactory.newLatLngBounds(allBounds, 120)));
    }

    private void go3D() {
        if (myMap == null || casaPadres == null) return;

        CameraPosition cam = new CameraPosition.Builder()
                .target(casaPadres)
                .zoom(18f)
                .tilt(60f)
                .bearing(35f)
                .build();

        myMap.animateCamera(CameraUpdateFactory.newCameraPosition(cam), 800, null);
    }

    private void resetBounds() {
        if (myMap == null || allBounds == null) return;
        myMap.animateCamera(CameraUpdateFactory.newLatLngBounds(allBounds, 120), 600, null);
    }
}
