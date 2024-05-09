package com.example.a22it204_dangcongnhat_22jit
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class CalculateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_calculate, container, false)

        val btnSnt = view.findViewById<Button>(R.id.btnsnt)
        val tvkqt1 = view.findViewById<TextView>(R.id.tvkqt1)

        btnSnt.setOnClickListener {
            val inputText = view.findViewById<EditText>(R.id.edt1).text.toString()
            if (inputText.isEmpty()) {
                showToast("Bạn chưa nhập số")
                return@setOnClickListener
            }
            val number = inputText.toIntOrNull()
            if (number == null) {
                showToast("Số không hợp lệ")
                return@setOnClickListener
            }
            if (isPrime(number)) {
                tvkqt1.text = "Số nguyên tố"
            } else {
                tvkqt1.text = "Không phải số nguyên tố"
            }
        }

        val btnGpt = view.findViewById<Button>(R.id.btngpt)
        val tvkqt2 = view.findViewById<TextView>(R.id.tvkqt2)

        btnGpt.setOnClickListener {
            val inputA = view.findViewById<EditText>(R.id.edt3).text.toString()
            val inputB = view.findViewById<EditText>(R.id.edt4).text.toString()

            if (inputA.isEmpty() || inputB.isEmpty()) {
                showToast("Bạn chưa nhập đủ số")
                return@setOnClickListener
            }

            val a = inputA.toDoubleOrNull()
            val b = inputB.toDoubleOrNull()

            if (a == null || b == null) {
                showToast("Số không hợp lệ")
                return@setOnClickListener
            }

            val result = calculateEquation(a, b)
            tvkqt2.text = "Kết quả: $result"
        }

        return view
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun isPrime(number: Int): Boolean {
        if (number <= 1) return false
        for (i in 2 until number) {
            if (number % i == 0) return false
        }
        return true
    }

    private fun calculateEquation(a: Double, b: Double): Double {
        // Giải phương trình bậc nhất ax + b = 0
        return if (a == 0.0) {
            if (b == 0.0) {
                Double.POSITIVE_INFINITY // Vô số nghiệm
            } else {
                Double.NaN // Vô nghiệm
            }
        } else {
            -b / a // Nghiệm duy nhất
        }
    }
}
