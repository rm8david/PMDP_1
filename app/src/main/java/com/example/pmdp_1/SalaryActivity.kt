package com.example.pmdp_1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SalaryActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_salary)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val resultado = findViewById<TextView>(R.id.resultado)
        /*
        intent.putExtra("BRUTO", brutoMensual)
            intent.putExtra("NETO", netoMensual)
            intent.putExtra("PAGAS", numeroPagas)
            intent.putExtra("IRPF", irpf)
            intent.putExtra("DEDUCCIONES",deducciones)
         */
        val bruto = intent.extras?.getDouble("BRUTO") ?: 0.0
        val neto = intent.extras?.getDouble("NETO") ?: 0.0
        val irpf = intent.extras?.getDouble("IRPF") ?: 0.0

        val brutoRedondeado = String.format("%.2f", bruto).toDouble()
        val netoRedondeado = String.format("%.2f", neto).toDouble()
        val irpfRedondeado = String.format("%.2f", irpf).toDouble()
        val deducciones = intent.extras?.getString("DEDUCCIONES") ?: ""


        resultado.text = "Salario bruto: $brutoRedondeado €\n" +
                "Salario neto: $netoRedondeado €\n" +
                "Retencion del IRPF: $irpfRedondeado %\n" +
                "Deducciones: \n " + deducciones

        val regresar = findViewById<Button>(R.id.regresar)
        regresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}