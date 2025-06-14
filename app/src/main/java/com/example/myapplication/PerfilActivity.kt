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
    lateinit var txtPassword2:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.perfil)
        val codPersona = intent.getStringExtra("codigo")
        consulData(codPersona.toString())
        txtName = findViewById(R.id.txtName)
        txtLastname = findViewById(R.id.txtLastname)
        txtEmail = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)
        txtPassword2 = findViewById(R.id.txtPassword2)

        var btnCancel = findViewById<Button>(R.id.btnCancel)
        var btnSave = findViewById<Button>(R.id.btnSave)

        btnCancel.setOnClickListener{
            val fp = Intent(this, PersonaActivity::class.java)
            startActivity(fp)
        }
        btnSave.setOnClickListener{
            updateUser(codPersona.toString())
        }
    }

    private fun updateUser(codigo:String) {
        if (txtPassword.text.toString().equals(txtPassword2.text.toString())) {
            val url = "http://10.0.2.2:8000/person.php?op=userEdit"
            val datos = JSONObject()
            datos.put("codigo", codigo)
            datos.put("nombre", txtName.text)
            datos.put("apellido", txtLastname.text)
            datos.put("correo", txtEmail.text)
            datos.put("clave", txtPassword.text)

            val rq = Volley.newRequestQueue(this)
            val js = JsonObjectRequest(
                Request.Method.POST,
                url,
                datos,
                { s ->
                    try {
                        val obj = (s)
                        if (obj.getBoolean("estado")) {
                            Toast.makeText(
                                applicationContext,
                                obj.getString("mensaje"),
                                Toast.LENGTH_LONG
                            ).show()
                            val fp = Intent(this, PersonaActivity::class.java)
                            fp.putExtra("codigo", codigo)
                            fp.putExtra("nombre", txtName.text.toString())
                            startActivity(fp)
                        } else {
                            Toast.makeText(
                                applicationContext,
                                obj.getString("mensaje"),
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    } catch (e: JSONException) {
                        Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()

                    }
                },
                { volleyError ->
                    Toast.makeText(
                        applicationContext,
                        volleyError.message,
                        Toast.LENGTH_LONG
                    ).show()
                })
            rq.add(js)

        }else {
            Toast.makeText(applicationContext, "Las claves no coinciden" , Toast.LENGTH_LONG).show()
            txtPassword2.setText("")
        }

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