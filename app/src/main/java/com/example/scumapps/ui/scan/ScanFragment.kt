package com.example.scumapps.ui.scan

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.scumapps.R
import com.example.scumapps.databinding.FragmentScanBinding
import com.example.scumapps.ui.profile.ProfileFragment

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.scanIcon.setOnClickListener {
            val intentToScan = Intent(activity, ScanningActivity::class.java)
            startActivity(intentToScan)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}