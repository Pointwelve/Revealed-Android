package com.pointwelve.revealed.ui.create_post


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionInflater

import com.pointwelve.revealed.R
import com.pointwelve.revealed.binding.FragmentDataBindingComponent
import com.pointwelve.revealed.databinding.CreatePostFragmentBinding
import com.pointwelve.revealed.databinding.PostFragmentBinding
import com.pointwelve.revealed.di.Injectable
import com.pointwelve.revealed.ui.common.RetryCallback
import com.pointwelve.revealed.ui.main.MainViewModel
import com.pointwelve.revealed.util.views.autoCleared
import javax.inject.Inject

class CreatePostFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val createPostViewModel: CreatePostViewModel by viewModels {
        viewModelFactory
    }

    var binding by autoCleared<CreatePostFragmentBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<CreatePostFragmentBinding>(
            inflater,
            R.layout.create_post_fragment,
            container,
            false
        )
        binding = dataBinding
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.move)
        return dataBinding.root
    }


}
