package com.example.vivantsample;


import com.example.vivantsample.data.Galleries;
import com.example.vivantsample.data.GalleryInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Toast;

public class VivantMap extends FragmentActivity implements
		OnMarkerClickListener, 
		OnMapReadyCallback, 
		OnInfoWindowClickListener {
	
	private GoogleMap mMap ;
	Marker [] marker ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		return false;
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(false);
		
        addMarkersToMap() ;
        setMarkersVisible();
        
        mMap.setOnInfoWindowClickListener(this);

	}
	
	private void setMarkersVisible() {
		final View mapView = getSupportFragmentManager().findFragmentById(R.id.map).getView();
        if (mapView.getViewTreeObserver().isAlive()) {
            mapView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                @SuppressWarnings("deprecation") // We use the new method when supported
                @SuppressLint("NewApi") // We check which build version we are using.
                @Override
                public void onGlobalLayout() {
                	
                	LatLngBounds.Builder builder = new LatLngBounds.Builder();
                	for (int i = 0;i < marker.length;i++)
                		builder.include(marker[i].getPosition());
                	
                	LatLngBounds bounds = builder.build();
                    
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                      mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                      mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                }
            });
        }
	}
	
	private void addMarkersToMap() {
		int noOfGalleries = Global.galleries.galleries.size();
		marker = new Marker[noOfGalleries];
		
		for (int i = 0;i < noOfGalleries;i++) {
			GalleryInfo gallery = Global.galleries.galleries.get(i);
			
			if (gallery == null)
				return ;
			
			MarkerOptions options = new MarkerOptions() ;
			LatLng latLng = new LatLng(gallery.lat, gallery.lon);
			options.position(latLng);
			options.title(gallery.address + "," + gallery.suburb);
			options.snippet("Galleries: " + gallery.piecesofart);
			options.icon(BitmapDescriptorFactory.defaultMarker(i * 360 / 12));
			
			marker[i] = mMap.addMarker(options);	
		}
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		Intent intent = new Intent();
		intent.setClass(this, GalleryActivity.class);
		intent.putExtra("id", getMarkerId(marker));
		
		startActivity(intent);
	}
	
	private int getMarkerId(Marker marker) {
		for (int i = 0;i < Global.galleries.galleries.size();i++) {
			GalleryInfo gallery = Global.galleries.galleries.get(i);

			if (gallery.lat == marker.getPosition().latitude && gallery.lon == marker.getPosition().longitude)
				return gallery.id;
		}
		
		return -1 ;
	}
	
	private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, "Map Not Ready", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
	
    public void onClearMap(View view) {
        if (!checkReady()) {
            return;
        }
        mMap.clear();
    }

    public void onResetMap(View view) {
        if (!checkReady()) {
            return;
        }

        mMap.clear();
        addMarkersToMap();
    }

}
