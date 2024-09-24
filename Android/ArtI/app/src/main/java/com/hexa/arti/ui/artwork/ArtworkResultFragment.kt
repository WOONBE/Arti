package com.hexa.arti.ui.artwork

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.databinding.FragmentArtworkResultBinding
import com.hexa.arti.util.navigate
import com.hexa.arti.util.popBackStack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtworkResultFragment : BaseFragment<FragmentArtworkResultBinding>(R.layout.fragment_artwork_result) {

    private val args : ArtworkResultFragmentArgs by navArgs()
    private val artworkResultViewModel : ArtworkResultViewModel by viewModels()

    data class Item(val id: Int, val name: String)
    private val itemList = arrayListOf(
        Item(1, "aa"),
        Item(2, "bb"),
        Item(3, "cc"),
        Item(4, "dd")
    )

    override fun init() {

        with(binding){

            if(args.artType == 1){
                artworkResultViewModel.getImageUri(args.artId)
                artworkResultEt.setText("")
            }
            else if(args.artType == 0){
                artworkResultViewModel.getArtWork(args.artId.toInt())
            }

            artworkResultViewModel.artworkResult.observe(viewLifecycleOwner){
                artworkResultEt.setText(it.title)
            }

            //spinner
            artworkResultThemeSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, itemList.map { it.name }).apply {
                setDropDownViewResource(R.layout.item_spinner_theme)

            }
            artworkResultThemeSpinner.dropDownWidth = ViewGroup.LayoutParams.MATCH_PARENT
            artworkResultThemeSpinner.dropDownWidth = ViewGroup.LayoutParams.WRAP_CONTENT
            artworkResultThemeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val selectedItem = itemList[position]
                    makeToast( "Selected: ${selectedItem.name}, ID: ${selectedItem.id}")
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // 선택되지 않았을 때 처리할 내용
                }
            }

            artworkResultViewModel.imageUri.observe(viewLifecycleOwner) { byteArray ->
                if (byteArray != null) {
                    Glide.with(requireContext())
                        .load(byteArray) // ByteArray를 Glide로 로드
                        .into(artworkResultImg)
                } else {
                }
            }


            artworkBackBtn.setOnClickListener {
                popBackStack()
            }
            artworkResultBtn.setOnClickListener {
                val action =ArtworkResultFragmentDirections.actionArtworkResultFragmentToMyGalleryHomeFragment2(1)
                navigate(action)
            }
        }
    }

}
