package com.pointwelve.revealed.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.pointwelve.revealed.R
import com.pointwelve.revealed.di.Injectable
import kotlinx.android.synthetic.main.main_fragment.*
import javax.inject.Inject

class MainFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val mainViewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnLogin.setOnClickListener {
            mainViewModel.login(this.requireActivity())
        }

        mainViewModel.authLiveData.observe(viewLifecycleOwner, Observer { isLoggedIn ->
            if(isLoggedIn) {
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToGetStartedFragment())
            }
        })
    }

}
