package pe.edu.unac.appunac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //START VALIDAR USUSARIO
    EditText edtCorreo,edtContra;
    Button btnIngresar;
    //END VALIDAR USUARIO


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //START VALIDAR USUSARIO
        edtCorreo=findViewById(R.id.editTextTextEmailAddress);
        edtContra=findViewById(R.id.editTextTextPassword);
        btnIngresar=findViewById(R.id.button);
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarUsuario("http://192.168.1.34/appunac/validar_usuario.php");//en vez de localhost le puse mi ip
            }
        });
        //END VALIDAR USUARIO

        //START GO ESTUDIANTE
        /*
        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), EstudianteActivity.class);
                startActivityForResult(intent, 0);
            }
        });*/
        //END GO ESTUDIANTE

    }

    //START VALIDAR USUSARIO
    private void validarUsuario(String URL){
        StringRequest stringRequest =new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    Intent intent = new Intent(getApplicationContext(),EstudianteActivity.class);
                    //START ENVIAR EL CORREO
                    intent.putExtra("correo", edtCorreo.getText().toString());
                    //END ENVIAR EL CORREO
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String, String>();
                parametros.put("correo",edtCorreo.getText().toString());
                parametros.put("contra",edtContra.getText().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    //END VALIDAR USUARIO

}