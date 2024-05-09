package com.example.a22it204_dangcongnhat_22jit
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topLeft = view.findViewById<FrameLayout>(R.id.top_left)
        val topRight = view.findViewById<FrameLayout>(R.id.top_right)
        val bottomLeft = view.findViewById<FrameLayout>(R.id.bottom_left)
        val bottomRight = view.findViewById<FrameLayout>(R.id.bottom_right)

        // Set onClickListeners using switch-case
        topLeft.setOnClickListener {
            val fragment = ClassStudentFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        topRight.setOnClickListener {
            val fragment = DiemDanhFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        bottomLeft.setOnClickListener {

        }

        bottomRight.setOnClickListener {

        }

    }
}
