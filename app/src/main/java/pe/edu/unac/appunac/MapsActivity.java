package pe.edu.unac.appunac;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

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
        mMap = googleMap;

        //RECORRIDO VENTANILLA
        // Add a marker in Ventanilla Grifo
        LatLng ventanillagrifo = new LatLng(-11.871775091338439, -77.12706178728769);
        mMap.addMarker(new MarkerOptions().position(ventanillagrifo).title("Paradero Ventanilla Grifo").snippet("RECORRIDO VENTANILLA Mañana: 07:00 am - Noche: 08:30 pm").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        // Add a marker in Oquendo
        LatLng oquendo = new LatLng(-11.976042, -77.125126);
        mMap.addMarker(new MarkerOptions().position(oquendo).title("Oquendo").snippet("RECORRIDO VENTANILLA Mañana: 07:40 am - Noche: 07:30 pm").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        // Add a marker in Ciudad universitaria
        LatLng ciudaduniversitaria = new LatLng(-12.0614528, -77.1167462);
        mMap.addMarker(new MarkerOptions().position(ciudaduniversitaria).title("Paradero Ciudad Universiataria").snippet("RECORRIDO VENTANILLA Mañana: 08:00 am - Noche: 06:45 pm").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        //RECORRIDO SAN JUAN DE LURIGANCHO
        // Add a marker in San Juan de Lurigancho
        LatLng sanjuandelurigancho = new LatLng(-11.968836288290484, -76.99396863440379);
        mMap.addMarker(new MarkerOptions().position(sanjuandelurigancho).title("San Juan de Lurigancho").snippet("RECORRIDO SAN JUAN DE LURIGANCHO Mañana: 07:00 am - Noche: 08:30 pm").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        // Add a marker in Plaza dos de Mayo
        LatLng plazadosdemayo = new LatLng(-12.043285350810258, -77.04285603894013);
        mMap.addMarker(new MarkerOptions().position(plazadosdemayo).title("Plaza dos de Mayo").snippet("RECORRIDO SAN JUAN DE LURIGANCHO Mañana: 07:45 am - Noche: 07:20 pm").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        // Add a marker in Ciudad universitaria
        LatLng ciudaduniversitaria2 = new LatLng(-12.061349, -77.116661);
        mMap.addMarker(new MarkerOptions().position(ciudaduniversitaria2).title("Paradero Ciudad Universiataria").snippet("RECORRIDO SAN JUAN DE LURIGANCHO Mañana: 08:00 am - Noche: 06:45 pm").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        //RECORRIDO CONO SUR
        // Add a marker in JOSE GALVEZ
        LatLng josegalvez = new LatLng(-12.076162951108747, -77.03031582634199);
        mMap.addMarker(new MarkerOptions().position(josegalvez).title("José Galvez").snippet("RECORRIDO CONO SUR Mañana: 06:00 am - Noche: 09:30 pm").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
        // Add a marker in Ciudad universitaria
        LatLng ciudaduniversitaria3 = new LatLng(-12.061532, -77.116783);
        mMap.addMarker(new MarkerOptions().position(ciudaduniversitaria3).title("Paradero Ciudad Universiataria").snippet("RECORRIDO CONO SUR Mañana: 07:50 am - Noche: 06:50 pm").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));


        //los colores: azul para la ruta de ventanilla, SAN JUAN DE LURIGANCHO verde, cono norte cyan, cono sur magenta, y recoorrido norte red

        //move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ciudaduniversitaria,15));
    }
}