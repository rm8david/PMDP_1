package com.example.pmdp_1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Referencias a los elementos de la interfaz
        val inputBruto: EditText = findViewById(R.id.input_bruto)
        val spinnerPagas: Spinner = findViewById(R.id.spinner_pagas)
        val inputEdad: EditText = findViewById(R.id.input_edad)
        val spinnerCategoria: Spinner = findViewById(R.id.spinner_categoria)
        val spinnerDiscapacidad: Spinner = findViewById(R.id.spinner_discapacidad)
        val spinnerEstadoCivil: Spinner = findViewById(R.id.spinner_estado_civil)
        val recogerDatosButton: Button = findViewById(R.id.recoger_datos_button)
        val inputHijos: EditText = findViewById(R.id.input_hijos)

        //COnfiguracion del Spinner de estado_civil
        val estadoOpciones = resources.getStringArray(R.array.estado_civil)
        val estadoAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, estadoOpciones)
        estadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEstadoCivil.adapter = estadoAdapter

        // Configuración del Spinner de Pagas
        val pagasOptions = resources.getStringArray(R.array.opciones_pagas)
        val pagasAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, pagasOptions)
        pagasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPagas.adapter = pagasAdapter

        // Configuración del Spinner de Categoría Profesional
        val categoriaOptions = resources.getStringArray(R.array.opciones_categorias)
        val categoriaAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, categoriaOptions)
        categoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategoria.adapter = categoriaAdapter

        // Configuración del Spinner de Discapacidad
        val discapacidadOptions = resources.getStringArray(R.array.opciones_discapacidad)
        val discapacidadAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, discapacidadOptions)
        discapacidadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDiscapacidad.adapter = discapacidadAdapter

        // Acción del botón Recoger Datos
        recogerDatosButton.setOnClickListener {
            val salarioBruto = inputBruto.text.toString().toDoubleOrNull()
            val numeroPagas = spinnerPagas.selectedItem.toString().toIntOrNull() ?: 12
            val edad = inputEdad.text.toString().toIntOrNull() ?: 40
            val categoriaProfesional = spinnerCategoria.selectedItem.toString()
            val discapacidad = spinnerDiscapacidad.selectedItem.toString()
            val estadoCivil = spinnerEstadoCivil.selectedItem.toString()
            val hijos = inputHijos.text.toString().toIntOrNull() ?: 0

            //declaramos un String que vaya acumulando las posibles deducciones
            var deducciones: String = ""

            //declaramos una variable retencion que sera el porcentaje que pagamos a hacienda
            var retencion: Double = when (salarioBruto ?: 0.0) {
                in 0.0..12450.0 -> 0.19
                in 12451.0..20199.0 -> 0.24
                in 20200.0..35199.0 -> 0.30
                in 35200.0..59999.0 -> 0.37
                in 60000.0..299999.0 -> 0.45
                else -> 0.47
            }
            //modificamos la retencion con la edad
            if (edad > 65) {
                retencion -= 0.06
                deducciones += "-Mayor de 65"
            }
            //modificamos la retencion con el numero de hijos
            when (hijos) {
                0 -> retencion += 0.0
                1, 2 -> {
                    retencion -= 0.03
                    deducciones += "\n -de 1 a 2 hijos"
                }

                3, 4, 5 -> {
                    retencion -= 0.05
                    deducciones += "\n -de 3 a 5 hijos"
                }

                in 6..Int.MAX_VALUE -> {
                    retencion -= 0.07
                    deducciones += "\n -familia numerosa"
                }

                else -> retencion += 0.0
            }
            //modificamos la retencion con el estado civil
            when (estadoCivil) {
                "soltero" -> retencion += 0.0
                "casado" -> retencion += 0.01
                "divorciado" -> retencion += 0.01
                "viudo" -> retencion -= 0.02
                else -> retencion += 0.0
            }
            //modificamos la retencion con la categoria
            when (categoriaProfesional) {
                "Ingenieros y licenciados" -> retencion += 0.01
                "Ingenieros tecnicos" -> retencion += 0.01
                "Jefes administrativos y de taller" -> retencion += 0.0
                "Oficiales administrativos" -> retencion -= 0.01
                "Oficiales de primera y segunda" -> retencion -= 0.01
                "Ayudantes no titulados" -> retencion -= 0.02
                "Peones" -> retencion -= 0.02
            }
            //modificamos la retencion con la discapacidad
            when (discapacidad) {
                "Sin discapacidad" -> retencion += 0.0
                "33% discapacidad" -> {
                    retencion -= 0.08
                    deducciones += "\n -mas de 33% de discapacidad"
                }

                "65% discapacidad" -> {
                    retencion -= 0.12
                    deducciones += "\n -mas de 65% de discapacidad"
                }
            }
            //dividimos el neto entre el numero de pagas y
            //preparamos los valores para enviarselos a la vista de resultado
            val netoAnual = salarioBruto?.times((1 - retencion))
            val brutoMensual = salarioBruto!!.div(numeroPagas)
            val netoMensual = netoAnual?.div(numeroPagas)
            val irpf = retencion * 100
            val intent = Intent(this, SalaryActivity::class.java)
            intent.putExtra("BRUTO", brutoMensual)
            intent.putExtra("NETO", netoMensual)
            intent.putExtra("PAGAS", numeroPagas)
            intent.putExtra("IRPF", irpf)
            intent.putExtra("DEDUCCIONES",deducciones)
            startActivity(intent)

        }
    }
}



