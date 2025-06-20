package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class NuevoActivity : ComponentActivity() {
    lateinit var txtName:EditText
    lateinit var txtLastname:EditText
    lateinit var txtTelefono: EditText
    lateinit var txtEmail:EditText
    lateinit var codNombre: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nuevo)
        val codPersona = intent.getStringExtra("codigo")
        codNombre = intent.getStringExtra("nombre").orEmpty()
        val codContacto = intent.getStringExtra("contacto")
        txtName = findViewById(R.id.txtNombre)
        txtTelefono = findViewById(R.id.txtTelefono)
        txtLastname = findViewById(R.id.txtApellido)
        txtEmail = findViewById(R.id.txtCorreo)

        var btnCancel = findViewById<Button>(R.id.btnSalir)
        var btnSave = findViewById<Button>(R.id.btnGuardar)

        if (codContacto != null) {
            cargarContacto(codContacto)
        }

        btnCancel.setOnClickListener{
            val fp = Intent(this, PersonaActivity::class.java)
            fp.putExtra("codigo", codPersona)
            fp.putExtra("nombre", codNombre)
            startActivity(fp)
        }
        btnSave.setOnClickListener{
            if (codContacto != null) {
                actualizarContacto(codContacto, codPersona.toString())
            } else {
                guardarContacto(codPersona.toString())
            }
        }
    }

    private fun guardarContacto(codigo:String) {
            val url = "http://10.0.2.2:8000/person.php?op=createContact"
            val datos = JSONObject()
        datos.put("codigo", codigo)
        datos.put("nombre", txtName.text.toString())
        datos.put("apellido", txtLastname.text.toString())
        datos.put("telefono", txtTelefono.text.toString())
        datos.put("email", txtEmail.text.toString())
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
                            fp.putExtra("nombre",codNombre)
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
        }

    private fun cargarContacto(codigo:String) {
        val url = "http://10.0.2.2:8000/person.php?op=contactId"
        val datos = JSONObject()
        datos.put("codigo", codigo)
        val rq = Volley.newRequestQueue(this)
        val js = JsonObjectRequest(Request.Method.POST, url, datos,
            { s->
                try {
                    val obj = (s)
                    if(obj.getBoolean("estado")) {
                        var array=obj.getJSONArray("contactos")
                        var datos = array.getJSONObject(0)
                        txtName.setText(datos.getString("nombre"))
                        txtLastname.setText(datos.getString("apellido"))
                        txtTelefono.setText(datos.getString("telefono"))
                        txtEmail.setText(datos.getString("email"))

                    }else {
                        Toast.makeText(applicationContext, obj.getString("mensaje"), Toast.LENGTH_LONG).show()
                    }

                }catch (e: JSONException) {
                    Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()

                }
            }, {volleyError-> Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show()})
        rq.add(js)

    }

    private fun actualizarContacto(codigo:String, codigoPersona: String) {
            val url = "http://10.0.2.2:8000/person.php?op=contactEdit"
            val datos = JSONObject()
        datos.put("codigo", codigo)
        datos.put("nombre", txtName.text.toString())
        datos.put("apellido", txtLastname.text.toString())
        datos.put("telefono", txtTelefono.text.toString())
        datos.put("email", txtEmail.text.toString())
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
                            fp.putExtra("codigo", codigoPersona)
                            fp.putExtra("nombre",codNombre)
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

    }


}
