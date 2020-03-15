package com.pointwelve.revealed.ui.post.detail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.pointwelve.revealed.R
import com.pointwelve.revealed.databinding.PostDetailFragmentBinding
import com.pointwelve.revealed.di.Injectable
import com.pointwelve.revealed.util.views.autoCleared
import javax.inject.Inject

class PostDetailFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val postDetailViewModel: PostDetailViewModel by viewModels {
        viewModelFactory
    }

    val args: PostDetailFragmentArgs by navArgs()

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

        postDetailViewModel.getPost(args.postId)
            .observe(viewLifecycleOwner, Observer { post ->
                post?.let {
                    Toast.makeText(requireContext(), it.message ?: "", Toast.LENGTH_LONG).show()
                }
            })
    }

}
