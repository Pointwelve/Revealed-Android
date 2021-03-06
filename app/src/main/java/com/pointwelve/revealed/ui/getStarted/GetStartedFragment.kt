package com.pointwelve.revealed.ui.getStarted

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pointwelve.revealed.R
import com.pointwelve.revealed.databinding.GetStartedFragmentBinding
import com.pointwelve.revealed.di.Injectable
import com.pointwelve.revealed.util.Status
import com.pointwelve.revealed.util.views.autoCleared
import kotlinx.android.synthetic.main.get_started_fragment.*
import javax.inject.Inject

class GetStartedFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val getStartedViewModel: GetStartedViewModel by viewModels {
        viewModelFactory
    }

    var binding by autoCleared<GetStartedFragmentBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val dataBinding = DataBindingUtil.inflate<GetStartedFragmentBinding>(
            inflater,
            R.layout.get_started_fragment,
            container,
            false
        )
        binding = dataBinding
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        btnSignUp.setOnClickListener {
            getStartedViewModel.signupUser(usernameEditText.text.toString())
        }

        getStartedViewModel.user.observe(viewLifecycleOwner, Observer { resources ->
            progressBar.isGone = resources.status != Status.LOADING
            if(resources.status == Status.SUCCESS) {
                findNavController().navigate(GetStartedFragmentDirections.actionMainFragmentToPostFragment())
            }
        })
    }
}
