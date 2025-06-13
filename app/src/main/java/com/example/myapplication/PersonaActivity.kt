package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class PersonaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.persona)
        val codPersona = intent.getStringExtra("codigo")
        val namePersona = intent.getStringExtra("nombre")

        var lblName = findViewById<TextView>(R.id.lblUsername)
        lblName.text = namePersona.toString()
        var btnPerfil = findViewById<Button>(R.id.btnPerfil)
        var btnContact = findViewById<Button>(R.id.btnContact)
        var btnExit = findViewById<Button>(R.id.btnExit)

        btnPerfil.setOnClickListener {
            val fp = Intent(this, PerfilActivity::class.java)
            fp.putExtra("codigo", codPersona)
            startActivity(fp)
        }


    }
    }