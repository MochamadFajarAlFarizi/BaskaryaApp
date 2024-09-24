    package com.example.baskaryaapp.ui.scan

    import android.net.Uri
    import android.os.Bundle
    import android.util.Log
    import android.view.View
    import androidx.activity.result.PickVisualMediaRequest
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.fragment.app.Fragment
    import com.example.baskaryaapp.R
    import com.example.baskaryaapp.data.api.ApiUpload
    import com.example.baskaryaapp.data.response.SimilarImagesItem
    import com.example.baskaryaapp.data.response.UploadResponse
    import com.example.baskaryaapp.databinding.FragmentScanBinding
    import com.example.baskaryaapp.ui.recomendation.RecomendationFragment
    import com.example.baskaryaapp.ui.recomendation.RecomentationAdapter
    import com.example.baskaryaapp.utils.getImageUri
    import com.example.baskaryaapp.utils.uriToFile
    import okhttp3.MediaType.Companion.toMediaTypeOrNull
    import okhttp3.MultipartBody
    import okhttp3.RequestBody.Companion.asRequestBody
    import retrofit2.Call
    import retrofit2.Response
    
    class ScanFragment : Fragment(R.layout.fragment_scan) {

        private lateinit var binding: FragmentScanBinding
        private var currentImageUri: Uri? = null
        private var uploadResponseData: List<SimilarImagesItem?>? = null
        private lateinit var adapter: RecomentationAdapter


        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            binding = FragmentScanBinding.bind(view)
            adapter = RecomentationAdapter()
            binding.buttonAdd.visibility = View.INVISIBLE
            binding.imageView2.setOnClickListener{back()}
            binding.fabGallery.setOnClickListener { startGallery() }
            binding.fabCamera.setOnClickListener { startCamera() }
            binding.buttonAdd.setOnClickListener { uploadImage() }
        }


        private fun startGallery() {
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        private val launcherGallery = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri: Uri? ->
            if (uri != null) {
                currentImageUri = uri
                showImage()
            } else {
                Log.d("Photo Picker", "No media selected")
            }
        }

        private fun startCamera() {
            currentImageUri = getImageUri(requireContext())
            launcherIntentCamera.launch(currentImageUri)
        }

        private val launcherIntentCamera = registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { isSuccess ->
            if (isSuccess) {
                showImage()
            }
        }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val file = uriToFile(uri, requireContext())
            val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("file", file.name, requestFile)

            binding.progressBar.visibility = View.VISIBLE
            binding.buttonAdd.isEnabled = false

            ApiUpload.apiService.postbatik(imagePart).enqueue(object : retrofit2.Callback<UploadResponse> {
                override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                    if (response.isSuccessful) {
                        val uploadResponse = response.body()
                        uploadResponse?.let {
                            uploadResponseData = it.similarImages
                            showUploadResponseInRecomendationFragment()
                        }
                    } else {
                        Log.e("UploadResponse", "Unsuccessful response: ${response.code()}")
                    }
                    binding.progressBar.visibility = View.GONE
                    binding.buttonAdd.isEnabled = true
                }

                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                    Log.e("UploadResponse", "Failure: ${t.message}")
                    binding.progressBar.visibility = View.GONE
                    binding.buttonAdd.isEnabled = true
                }
            })

        }
    }

        private fun showUploadResponseInRecomendationFragment() {
            val recommendationFragment = RecomendationFragment()
            recommendationFragment.uploadResponseData = uploadResponseData
            parentFragmentManager.beginTransaction()
                .replace(R.id.navhost, recommendationFragment)
                .addToBackStack(null)
                .commit()
        }

        private fun showImage() {
            currentImageUri?.let {
                Log.d("Image URI", "showImage: $it")
                binding.ivItemImage.setImageURI(it)
                binding.buttonAdd.visibility = View.VISIBLE
            }
        }
        private fun back(){
            requireActivity().onBackPressed()
        }
    }