package com.pointwelve.revealed.ui.create_post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SimpleAdapter
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.pointwelve.revealed.R
import com.pointwelve.revealed.databinding.CreatePostFragmentBinding
import com.pointwelve.revealed.di.Injectable
import com.pointwelve.revealed.ui.main.MainFragmentDirections
import com.pointwelve.revealed.util.Status
import com.pointwelve.revealed.util.views.autoCleared
import kotlinx.android.synthetic.main.create_post_fragment.*
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
        setHasOptionsMenu(false)
        activity?.title = "New Post"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        createPostViewModel.configs.observe(viewLifecycleOwner, Observer {
            progressBar.isGone = it.status != Status.LOADING
            if(it.status == Status.SUCCESS) {
                val topics = it.data?.getAllTopics?.edges.orEmpty().map { it.name }
                val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, topics)
                topicDropdown.setAdapter(adapter)
            }
        })
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

        return dataBinding.root
    }

}
