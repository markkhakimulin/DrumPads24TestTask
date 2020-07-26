package com.drumpads24.markkhakimulin.ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.drumpads24.markkhakimulin.R
import com.drumpads24.markkhakimulin.databinding.FragmentPlayerBinding
import com.drumpads24.markkhakimulin.ui.tracklist.TrackListViewModel
import kotlinx.android.synthetic.main.fragment_player.*

class PlayerFragment : Fragment() {

    lateinit var binding: FragmentPlayerBinding
    lateinit var viewModel: TrackListViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(requireActivity()).get(TrackListViewModel::class.java)
        binding.trackList = viewModel
        binding.lifecycleOwner = this

        closeButton.setOnClickListener {
            viewModel.stopCurrentTrack()
        }
        imageButton.setOnClickListener {
            viewModel.playPauseCurrentTrack(false)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_player, container, false
        )
        return binding.root
    }

}

