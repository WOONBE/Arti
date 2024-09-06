package com.hexa.arti.ui.artmuseum

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.MenuInflater
import android.view.View
import android.widget.GridLayout
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.animation.doOnEnd
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.data.MyGalleryThemeItem
import com.hexa.arti.databinding.FragmentMyGalleryBinding
import com.hexa.arti.ui.artmuseum.adpater.MyGalleryThemeAdapter
import com.hexa.arti.util.navigate
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream


class MyGalleryFragment : BaseFragment<FragmentMyGalleryBinding>(R.layout.fragment_my_gallery){
    override fun init() {
    }
    private lateinit var adapter: MyGalleryThemeAdapter
    private  val sampleData = listOf(
        MyGalleryThemeItem("병현이의 절망", listOf(R.drawable.survey_example, R.drawable.survey_example, R.drawable.survey_example)),
        MyGalleryThemeItem("미술관 테마2", listOf(R.drawable.survey_example, R.drawable.survey_example,R.drawable.survey_example))
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.subscribeBtn.setOnClickListener {
            navigate(R.id.action_myGalleryFragment_to_subscribeFragment)
        }
        adapter = MyGalleryThemeAdapter()
        binding.myGalleryThemeRv.adapter = adapter
        adapter.submitList(sampleData)

        binding.myGalleryNameModifyBtn.setOnClickListener {
            // 제목 버전
            binding.myGalleryNameModifyBtn.visibility = View.GONE
            binding.myGalleryNameCheckBtn.visibility = View.VISIBLE
            binding.myGalleryNameTv.apply {
                isClickable = true
                isFocusableInTouchMode = true
                isEnabled = true
                requestFocus() // 포커스를 EditText로 이동
                setSelection(text.length) // 커서를 텍스트 끝으로 이동
            }
        }

        binding.myGalleryNameCheckBtn.setOnClickListener {
            binding.myGalleryNameModifyBtn.visibility = View.VISIBLE
            binding.myGalleryNameCheckBtn.visibility = View.GONE
            binding.myGalleryNameTv.apply {
                isClickable = false
                isFocusable = false
                isEnabled = false

            }
        }

        binding.myGalleryThumbnailIv.setOnClickListener {
            openImagePicker()
        }
    }
    private val getImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                handleImage(it)
                binding.myGalleryThumbnailIv.setImageURI(it)
            }
        }
    private fun openImagePicker() {
        getImageLauncher.launch("image/*")
    }

    private fun uriToFile(context: Context, uri: Uri): File {
        val contentResolver = context.contentResolver
        val file =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "temp_image.jpg")

        contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                val buffer = ByteArray(1024)
                var length: Int
                while (inputStream.read(buffer).also { length = it } > 0) {
                    outputStream.write(buffer, 0, length)
                }
            }
        }
        return file
    }

    private fun compressImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        val compressedFile = File(file.parent, "compressed_${file.name}")
        FileOutputStream(compressedFile).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream) // 80% 압축 품질
        }
        return compressedFile
    }

    private fun handleImage(imageUri: Uri) {
        var file = uriToFile(requireContext(), imageUri)

        val maxSize = 10 * 1024 * 1024 // 10MB
        if (file.length() > maxSize) {
            file = compressImage(file)

            if (file.length() > maxSize) {
                makeToast("File size still exceeds limit after compression")
                return
            }
        }
        val requestFile = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
    }

}