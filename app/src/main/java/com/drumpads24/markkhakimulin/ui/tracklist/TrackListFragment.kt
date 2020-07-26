package com.drumpads24.markkhakimulin.ui.tracklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.drumpads24.markkhakimulin.R
import com.drumpads24.markkhakimulin.data.model.TrackInfo
import com.drumpads24.markkhakimulin.data.repository.TrackListRepository
import com.drumpads24.markkhakimulin.databinding.FragmentPlayerBinding
import com.drumpads24.markkhakimulin.databinding.TrackListFragmentBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlinx.android.synthetic.main.track_list_fragment.*


class TrackListFragment : Fragment() ,TrackClickListener{

    private lateinit var viewModel: TrackListViewModel
    private lateinit var factory: TrackListViewModelFactory
    private lateinit var binding: TrackListFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.track_list_fragment, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val repository = TrackListRepository(context)
        factory = TrackListViewModelFactory(repository)

        viewModel = ViewModelProviders.of(requireActivity(),factory).get(TrackListViewModel::class.java)

        val mLifecycleOwner :TrackClickListener = this
        binding.trackList = viewModel
        binding.lifecycleOwner = this

        binding.recyclerViewTracks
            .apply {
                adapter = TrackListAdapter(viewModel, mLifecycleOwner)
                layoutManager = LinearLayoutManager(activity)
            }
        binding.recyclerViewTracks.setHasFixedSize(true)

        viewModel.getTracklist()

        viewModel.currentTrack.observe(viewLifecycleOwner, Observer {

            if (viewModel.currentTrack.value ==  null) {
                hidePlayer()
            } else {
                binding.recyclerViewTracks
                    .apply {
                       adapter?.notifyDataSetChanged()
                    }
            }

        })

        hidePlayer()
    }
    fun showPlayer() {

        val manager: FragmentManager = childFragmentManager
        val ft: FragmentTransaction = manager.beginTransaction()
        val playerFragment =
            manager.findFragmentById(R.id.fragmentPlayer)
        ft.show(playerFragment!!)
        ft.commit()


        val params = toolbarLayout.layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED or AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL

        toolbarLayout.layoutParams = params
    }
    fun hidePlayer() {

        val manager: FragmentManager = childFragmentManager
        val ft: FragmentTransaction = manager.beginTransaction()
        val playerFragment =
            manager.findFragmentById(R.id.fragmentPlayer)
        ft.hide(playerFragment!!)
        ft.commit()


        val params = toolbarLayout.layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED or AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL

        toolbarLayout.layoutParams = params
    }

    override fun onRecyclerViewItemClick(position: Int) {

        showPlayer()
        viewModel.setCurrentTrackInfo(position)
        Toast.makeText(requireContext(), "${position} clicked", Toast.LENGTH_LONG).show();
    }

}

@Suppress("UNCHECKED_CAST")
class TrackListViewModelFactory(
    private val repository: TrackListRepository
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TrackListViewModel(repository) as T
    }

}