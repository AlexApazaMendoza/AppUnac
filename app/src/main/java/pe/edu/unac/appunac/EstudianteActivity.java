package pe.edu.unac.appunac;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class EstudianteActivity extends AppCompatActivity {

    String valorcorreo;

    //START VARIABLES CALENDAR
    EditText t1;
    private int mYearIni, mMonthIni, mDayIni, sYearIni, sMonthIni, sDayIni;
    static final int DATE_ID = 0;
    Calendar C = Calendar.getInstance();
    //END VARIABLES CALENDAR

    //START VARIABLE API MAPS
    Button botonmaps;
    //END VARIABLE API MAPS

    //START CARGAR DATOS
    EditText edtnombre,edtapellido,edtcodigo;
    //END CARGAR DATOS

    //START CARGAR RESERVA
    EditText edtid,edtnumplaca,edtnumasiento;
    //END CARGAR RESERVA


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiante);

        //START CARGAR DATOS
        valorcorreo = getIntent().getStringExtra("correo");
        edtnombre = (EditText) findViewById(R.id.editTextNombre);
        edtapellido = (EditText) findViewById(R.id.editTextApellido);
        edtcodigo = (EditText) findViewById(R.id.editTextCodigo);
        cargardatos("http://192.168.1.34/appunac/cargar_datos.php?correo="+valorcorreo+"");
        //END CARGAR DATOS

        //START CARGAR RESERVA
        edtid = (EditText) findViewById(R.id.editTextId);
        edtnumplaca = (EditText) findViewById(R.id.editTextNumplaca);
        edtnumasiento = (EditText) findViewById(R.id.editTextNumasiento);
        //END CARGAR RESERVA

        //START CALENDAR
        sMonthIni = C.get(Calendar.MONTH);
        sDayIni = C.get(Calendar.DAY_OF_MONTH);
        sYearIni = C.get(Calendar.YEAR);
        t1 = (EditText) findViewById(R.id.editTextCalendar);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_ID);
                hideSoftKeyboard(); //agregué para ocultar el teclado
            }
        });
        //END CALENDAR

        //START GO MAIN
        Button btn = (Button) findViewById(R.id.button3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), MainActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        //END GO MAIN

        //START API MAPS
        botonmaps = (Button) findViewById(R.id.button6);
        botonmaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(intent);
            }
        });
        //END API MAPS

        //START GO RESERVA
        Button btnReserva = (Button) findViewById(R.id.button2);
        btnReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), ReservaActivity.class);
                //START ENVIAR EL CODIGO Y CORREO
                intent.putExtra("codigo", edtcodigo.getText().toString());
                intent.putExtra("correo", valorcorreo);
                //END ENVIAR EL CORREO
                startActivityForResult(intent, 0);
            }
        });
        //END GO RESERVA

    }

    //START CALENDAR
    private void colocar_fecha() {
        t1.setText(mYearIni + "-" + (mMonthIni + 1) + "-" + mDayIni+" ");
    }
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYearIni = year;
                    mMonthIni = monthOfYear;
                    mDayIni = dayOfMonth;
                    colocar_fecha();
                    cargarreserva("http://192.168.1.34/appunac/cargar_reserva.php?fecha="+t1.getText().toString()+"&codigo="+edtcodigo.getText().toString()+"");
                }
            };
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_ID:
                return new DatePickerDialog(this, mDateSetListener, sYearIni, sMonthIni, sDayIni);
        }
        return null;
    }
    //END CALENDAR

    // START Hides the soft keyboard
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
    // END  Hides the soft keyboard

    //START CARGAR DATOS
    private void cargardatos(String URL){
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        edtnombre.setText(jsonObject.getString("nombre"));
                        edtapellido.setText(jsonObject.getString("apellido"));
                        edtcodigo.setText(jsonObject.getString("codigo"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    //END CARGAR DATOS

    //START CARGAR RESERVA--ojo la reserva se puede hacer varias veces para una fecha, eso hay que corregir
    private void cargarreserva(String URL){
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        edtid.setText(jsonObject.getString("id"));
                        edtnumplaca.setText(jsonObject.getString("numplaca"));
                        edtnumasiento.setText(jsonObject.getString("numasiento"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                edtid.setText("NoEncontrado");
                edtnumplaca.setText("NoEncontrado");
                edtnumasiento.setText("NoEncontrado");
                //Toast.makeText(getApplicationContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    //END CARGAR RESERVA


}