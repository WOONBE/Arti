package com.hexa.arti.ui.artmuseum

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.hexa.arti.R
import com.hexa.arti.config.BaseFragment
import com.hexa.arti.data.model.artmuseum.MyGalleryThemeItem
import com.hexa.arti.databinding.FragmentMyGalleryBinding
import com.hexa.arti.ui.artmuseum.adpater.MyGalleryThemeAdapter
import com.hexa.arti.util.navigate
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream


class MyGalleryFragment : BaseFragment<FragmentMyGalleryBinding>(R.layout.fragment_my_gallery){
    override fun init() {
    }
    private lateinit var adapter: MyGalleryThemeAdapter
    private  val sampleData = listOf(
        MyGalleryThemeItem("병현이의 절망", listOf(R.drawable.survey_example, R.drawable.survey_example, R.drawable.survey_example, R.drawable.survey_example)),
        MyGalleryThemeItem("미술관 테마2", listOf(R.drawable.survey_example, R.drawable.survey_example,R.drawable.survey_example))
    )

    private var initName = ""
    private var initInfo = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MyGalleryThemeAdapter()
        binding.myGalleryThemeRv.adapter = adapter
        adapter.submitList(sampleData)

        initEvent()
    }
    private fun initEvent(){
        with(binding){
            // 미술관 이름 변경
            myGalleryNameModifyBtn.setOnClickListener {
                myGalleryNameModifyBtn.visibility = View.GONE
                myGalleryNameCheckBtn.visibility = View.VISIBLE
                myGalleryNameCancelBtn.visibility = View.VISIBLE
                myGalleryNameTv.apply {
                    initName = this.text.toString()
                    isClickable = true
                    isFocusableInTouchMode = true
                    isEnabled = true
                    requestFocus() // 포커스를 EditText로 이동
                    setSelection(text.length) // 커서를 텍스트 끝으로 이동
                }
            }
            // 미술관 제목 변경 체크 버튼
            myGalleryNameCheckBtn.setOnClickListener {
                myGalleryNameModifyBtn.visibility = View.VISIBLE
                myGalleryNameCheckBtn.visibility = View.GONE
                myGalleryNameCancelBtn.visibility = View.GONE
                myGalleryNameTv.apply {
                    isClickable = false
                    isFocusable = false
                    isEnabled = false
                }
            }
            // 미술관 이름 변경 취소 버튼
            myGalleryNameCancelBtn.setOnClickListener {
                myGalleryNameModifyBtn.visibility = View.VISIBLE
                myGalleryNameCheckBtn.visibility = View.GONE
                myGalleryNameCancelBtn.visibility = View.GONE
                myGalleryNameTv.apply {
                    isClickable = false
                    isFocusable = false
                    isEnabled = false
                    setText(initName)
                }
            }

            // 미술관 소개 버튼
            myGalleryInfoModifyBtn.setOnClickListener {
                initInfo = myGalleryInfoEt.text.toString()
                myGalleryInfoCheckBtn.visibility = View.VISIBLE
                myGalleryInfoCancelBtn.visibility = View.VISIBLE
                myGalleryInfoModifyBtn.visibility = View.GONE

                with(myGalleryInfoEt) {
                    isClickable = true
                    isFocusableInTouchMode = true
                    isEnabled = true
                    requestFocus() // 포커스를 EditText로 이동
                    setSelection(text.length) // 커서를 텍스트 끝으로 이동
                }
            }

            // 소개글 수정 체크 버튼
            myGalleryInfoCheckBtn.setOnClickListener {
                myGalleryInfoCheckBtn.visibility = View.GONE
                myGalleryInfoCancelBtn.visibility = View.GONE
                myGalleryInfoModifyBtn.visibility = View.VISIBLE

                with(myGalleryInfoEt) {
                    isClickable = false
                    isFocusableInTouchMode = false
                    isEnabled = false
                }
            }

            // 소개글 수정 취소 버튼
            myGalleryInfoCancelBtn.setOnClickListener {
                myGalleryInfoCheckBtn.visibility = View.GONE
                myGalleryInfoCancelBtn.visibility = View.GONE
                myGalleryInfoModifyBtn.visibility = View.VISIBLE

                with(myGalleryInfoEt) {
                    isClickable = false
                    isFocusableInTouchMode = false
                    isEnabled = false
                    setText(initInfo)
                }
            }

            // 미술관 실행 버튼
            myGalleryPlayBtn.setOnClickListener {
                navigate(R.id.action_myGalleryHomeFragment_to_artGalleryDetailFragment)
            }
            // 썸네일 이미지 클릭
            myGalleryThumbnailIv.setOnClickListener {
                openImagePicker()
            }
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