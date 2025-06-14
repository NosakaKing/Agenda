package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.ui.theme.MyApplicationTheme
import org.json.JSONException
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        var txtUsuario = findViewById<EditText>(R.id.txtUsuario)
        var txtClave = findViewById<EditText>(R.id.txtClave)
        var btnLogin = findViewById<Button>(R.id.btnLogin)
        var lblCreate = findViewById<TextView>(R.id.lblCreate)
        var lblRecovery = findViewById<TextView>(R.id.lblRecovery)

        lblCreate.setOnClickListener{
            Toast.makeText(applicationContext, "Crear Cuenta", Toast.LENGTH_LONG).show()
        }

        lblRecovery.setOnClickListener{
            Toast.makeText(applicationContext, "Recuperar Clave", Toast.LENGTH_LONG).show()
        }

        btnLogin.setOnClickListener {
            val url = "http://10.0.2.2:8000/person.php?op=login"
            val datos = JSONObject()
            datos.put("usuario", txtUsuario.text.toString())
            datos.put("clave", txtClave.text.toString())

            val rq = Volley.newRequestQueue(this)
            val js = JsonObjectRequest(Request.Method.POST, url, datos,
                { s->
                    try {
                        val obj = (s)
                        if(obj.getBoolean("estado")) {
                            var array=obj.getJSONArray("persona")
                            var persona = array.getJSONObject(0)
                            val fp = Intent(this, PersonaActivity::class.java)
                            fp.putExtra("codigo", persona.getString("codigo"))
                            fp.putExtra("nombre", persona.getString("nombre"))
                            Toast.makeText(applicationContext, "Bienvenido: " + persona.getString("nombre"), Toast.LENGTH_LONG).show()
                            startActivity(fp)
                        }else {
                            Toast.makeText(applicationContext, obj.getString("mensaje"), Toast.LENGTH_LONG).show()
                        }

                    }catch (e:JSONException) {
                        Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()

                    }
                }, {volleyError-> Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show()})
            rq.add(js)

        }
    }
}