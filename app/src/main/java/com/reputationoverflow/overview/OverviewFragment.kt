package com.reputationoverflow.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.reputationoverflow.R
import com.reputationoverflow.databinding.FragmentOverviewBinding
import com.reputationoverflow.session.SessionUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OverviewFragment : Fragment() {

    private val viewModel: OverviewViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentOverviewBinding>(inflater,
            R.layout.fragment_overview,container,false)

        binding.viewModel = viewModel

        binding.lifecycleOwner = viewLifecycleOwner

        binding.loginButton.setOnClickListener {
            val intent = SessionUtil.getAuthIntent()
            startActivity(intent)
        }

        binding.logoutButton.setOnClickListener {
            viewModel.logout()
        }

        binding.updateButton.setOnClickListener {
            viewModel.getReputation()
        }

        return binding.root
    }
}