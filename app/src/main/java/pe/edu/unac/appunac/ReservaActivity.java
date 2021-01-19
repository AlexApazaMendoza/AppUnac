package pe.edu.unac.appunac;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservaActivity extends AppCompatActivity {

    String valorcodigo,valorcorreo;
    List<String> spinnerArrayAsientoU =  new ArrayList<String>();// esto agregue al final
    List<String> spinnerArrayAsientoD =  new ArrayList<String>();// esto agregue al fninal
    private boolean isFirst=true;

    //START VARIABLES CALENDAR
    EditText t1;
    private int mYearIni, mMonthIni, mDayIni, sYearIni, sMonthIni, sDayIni;
    static final int DATE_ID = 0;
    Calendar C = Calendar.getInstance();
    //END VARIABLES CALENDAR

    //START RESERVAR ASIENTO
    Spinner asiento, bus;
    Button reservar;
    //END RESERVAR ASIENTO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva);

        //START CARGAR DATOS
        valorcodigo = getIntent().getStringExtra("codigo");
        valorcorreo = getIntent().getStringExtra("correo");
        //END CARGAR DATOS

        //START CALENDAR
        sMonthIni = C.get(Calendar.MONTH);
        sDayIni = C.get(Calendar.DAY_OF_MONTH);
        sYearIni = C.get(Calendar.YEAR);
        t1 = (EditText) findViewById(R.id.editTextCalendar2);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_ID);
                hideSoftKeyboard(); //agregué para ocultar el teclado
            }
        });
        //END CALENDAR

        //START GO ESTUDIANTE
        Button btn = (Button) findViewById(R.id.button4);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), EstudianteActivity.class);
                intent.putExtra("correo", valorcorreo);
                startActivityForResult(intent, 0);
            }
        });
        //END GO ESTUDIANTE

        //START RESERVAR ASIENTO
        asiento=findViewById(R.id.spinner3);
        bus=findViewById(R.id.spinner);
        reservar=findViewById(R.id.button5);
        reservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //START VALIDAR RESERVA
                validarReserva("http://192.168.1.34/appunac/validar_reserva.php");
                //END VALIDAR RESERVA
                //ejecutarServicio("http://192.168.1.34/appunac/insertar.php");
            }
        });
        //END RESERVAR ASIENTO

        //START LLENAR SPINNER
        //OJO NO LO LLENA DESDE LA BASE DE DATOS, ESO HAY QUE CAMBIAR
        //bus
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("EGE-823");
        spinnerArray.add("EGK-548");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) findViewById(R.id.spinner);
        sItems.setAdapter(adapter);

        //asiento
        Spinner sItems2 = (Spinner) findViewById(R.id.spinner3);
        List<String> spinnerArray2 =  new ArrayList<String>();
        for (int i = 1; i < 31; i++) {
            spinnerArray2.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sItems2.setAdapter(adapter2);
        /*
        sItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirst){
                    isFirst=false;
                }else{
                    cargarasientos("http://localhost/appunac/cargar_asientos_disponibles.php?fecha="+t1.getText().toString()+"&numplaca="+sItems.getSelectedItem().toString()+"");
                    refrescar();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        //END LLENAR SPINNER

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

    //START RESERVAR ASIENTO
    private void ejecutarServicio(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Reserva hecha exitosamente", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("fecha",t1.getText().toString());
                parametros.put("numplaca",bus.getSelectedItem().toString());
                parametros.put("numasiento",asiento.getSelectedItem().toString());
                parametros.put("codigo",valorcodigo);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    //END RESERVAR ASIENTO


    //START VALIDAR RESERVA
    private void validarReserva(String URL){
        StringRequest stringRequest =new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    Toast.makeText(ReservaActivity.this, "Ya te has registrado", Toast.LENGTH_SHORT).show();
                }else{
                    ejecutarServicio("http://192.168.1.34/appunac/insertar.php");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ReservaActivity.this,error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String, String>();
                parametros.put("fecha",t1.getText().toString());
                parametros.put("codigo",valorcodigo);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    //END VALIDAR RESERVA

    //START SHOW ASIENTOS
    /*
    private void cargarasientos(String URL){
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        spinnerArrayAsientoU.add(jsonObject.getString("numasiento"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "no se encontraron datos", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    private void refrescar(){
        //se agregan los asientos disponibles
        for (int i = 1; i < 31; i++) {
            for (int j = 0; j < spinnerArrayAsientoU.size(); j++) {
                if(Integer.valueOf(spinnerArrayAsientoU.get(j))==i){

                }else{
                    spinnerArrayAsientoD.add(String.valueOf(i));
                }
            }
        }
        Spinner sItems = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArrayAsientoD);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sItems.setAdapter(adapter);
    }*/
    //END SHOW ASIENTOS

}