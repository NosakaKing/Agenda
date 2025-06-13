package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class PerfilActivity : ComponentActivity() {
    lateinit var txtName:EditText
    lateinit var txtLastname:EditText
    lateinit var txtEmail:EditText
    lateinit var txtPassword:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.perfil)
        val codPersona = intent.getStringExtra("codigo")
        consulData(codPersona.toString())
        txtName = findViewById(R.id.txtName)
        txtLastname = findViewById(R.id.txtLastname)
        txtEmail = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)

        var btnCancel = findViewById<Button>(R.id.btnCancel)
        var btnSave = findViewById<Button>(R.id.btnSave)

        btnCancel.setOnClickListener{
            val fp = Intent(this, PersonaActivity::class.java)
            startActivity(fp)
        }
        btnSave.setOnClickListener{
            save()
        }
    }

    private fun save() {
        TODO("Not yet implemented")
    }

    private fun consulData(codigo:String) {
        val url = "http://10.0.2.2:8000/person.php?op=userById"
        val datos = JSONObject()
        datos.put("codigo", codigo)
        val rq = Volley.newRequestQueue(this)
        val js = JsonObjectRequest(Request.Method.POST, url, datos,
            { s->
                try {
                    val obj = (s)
                    if(obj.getBoolean("estado")) {
                        var array=obj.getJSONArray("persona")
                        var dato = array.getJSONObject(0)
                        txtName.setText(dato.getString("nombre"))
                        txtLastname.setText(dato.getString("apellido"))
                        txtEmail.setText(dato.getString("correo"))
                        txtPassword.setText(dato.getString("clave"))
                    }else {
                        Toast.makeText(applicationContext, obj.getString("mensaje"), Toast.LENGTH_LONG).show()
                    }

                }catch (e: JSONException) {
                    Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()

                }
            }, {volleyError-> Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show()})
        rq.add(js)

    }
}