package com.pointwelve.revealed.ui.post.detail


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.pointwelve.revealed.R
import com.pointwelve.revealed.databinding.GetStartedFragmentBinding
import com.pointwelve.revealed.databinding.PostDetailFragmentBinding
import com.pointwelve.revealed.di.Injectable
import com.pointwelve.revealed.ui.getStarted.GetStartedFragmentDirections
import com.pointwelve.revealed.ui.getStarted.GetStartedViewModel
import com.pointwelve.revealed.util.Status
import com.pointwelve.revealed.util.views.autoCleared
import kotlinx.android.synthetic.main.get_started_fragment.*
import javax.inject.Inject

class PostDetailFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val postDetailViewModel: PostDetailViewModel by viewModels {
        viewModelFactory
    }

    var binding by autoCleared<PostDetailFragmentBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val dataBinding = DataBindingUtil.inflate<PostDetailFragmentBinding>(
            inflater,
            R.layout.post_detail_fragment,
            container,
            false
        )
        binding = dataBinding
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

    }

}
