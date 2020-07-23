package com.drumpads24.markkhakimulin.ui.tracklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.drumpads24.markkhakimulin.R
import com.drumpads24.markkhakimulin.data.model.TrackInfo
import com.drumpads24.markkhakimulin.data.repository.TrackListRepository
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlinx.android.synthetic.main.track_list_fragment.*


class TrackListFragment : Fragment() ,TrackClickListener{

    private lateinit var viewModel: TrackListViewModel
    private lateinit var factory: TrackListViewModelFactory
    lateinit var layout: CollapsingToolbarLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.track_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val repository = TrackListRepository(context)
        factory = TrackListViewModelFactory(repository)

        viewModel = ViewModelProviders.of(requireActivity(),factory).get(TrackListViewModel::class.java)

        viewModel.getTracklist()

        viewModel.tracks.observe(viewLifecycleOwner, Observer { tracks ->
            recycler_view_tracks.also {
                if (it.adapter == null) {
                    it.layoutManager = LinearLayoutManager(activity)
                    it.setHasFixedSize(true)
                    val adapter = TrackListAdapter(tracks, this)
                    adapter.setHasStableIds(true)
                    it.adapter = adapter
                } else {
                    val adapter:TrackListAdapter = it.adapter as TrackListAdapter
                    adapter.updateTrackList(tracks)
                }
            }
        })
        viewModel.currentTrackInfo.observe(viewLifecycleOwner, Observer {

            if (viewModel.currentTrackInfo.value == null) {
                hidePlayer()
            } else {
                viewModel.updateTrackInfo(it!!)
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

    override fun onRecyclerViewItemClick(view: View, trackInfo: TrackInfo) {

        showPlayer()
        viewModel.setCurrentTrackInfo(trackInfo)
        viewModel.updateTrackInfo(trackInfo)
        Toast.makeText(requireContext(), "${trackInfo.id} clicked", Toast.LENGTH_LONG).show();
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