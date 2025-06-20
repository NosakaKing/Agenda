package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject


class ContactoActivity : ComponentActivity() {
    val codigos = ArrayList<String>()
    lateinit var lista: ListView
    var nom_persona = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contacto)
        var cod_persona = intent.getStringExtra("codigo")
        var nom_persona = intent.getStringExtra("nombre")
        var btnContact = findViewById<Button>(R.id.btnNuevo)


        btnContact.setOnClickListener {
            val fp = Intent(this, NuevoActivity::class.java)
            fp.putExtra("codigo", cod_persona)
            fp.putExtra("nombre", nom_persona)
            startActivity(fp)
        }
        lista = findViewById(R.id.list)
        lista.setOnItemClickListener {adapterView, view , i, l ->
            val fp = Intent(this, NuevoActivity::class.java)
            fp.putExtra("codigo", cod_persona)
            fp.putExtra("nombre", nom_persona)
            fp.putExtra("contacto", codigos.get(i).toString())
            startActivity(fp)
        }
        consult(cod_persona.toString())
    }



    private fun consult(codigo: String) {
        val al =  ArrayList<String>()
        al.clear()
        val url = "http://10.0.2.2:8000/person.php?op=contact"
        val datos = JSONObject()

        datos.put("codigo", codigo)
        val rq = Volley.newRequestQueue(this)
        val js = JsonObjectRequest(
            Request.Method.POST,
            url,
            datos,
            { s ->
                try {
                    val obj = (s)
                    if (obj.getBoolean("estado")) {
                      val array = obj.getJSONArray("contactos")
                        for (i in 0 until array.length()) {
                            val fila= array.getJSONObject(i)
                            codigos.add(fila.getString("codigo"))
                            al.add(fila.getString("nombre"))
                        }
                        val aa = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al)
                        lista.adapter = aa
                        aa.notifyDataSetChanged()
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