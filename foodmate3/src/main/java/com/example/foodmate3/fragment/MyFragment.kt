package com.example.foodmate3.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.foodmate3.R
import com.example.foodmate3.UpdateActivity
import com.example.foodmate3.controller.SharedPreferencesUtil
import com.example.foodmate3.databinding.FragmentMyBinding

class MyFragment : Fragment() {
    private var _binding: FragmentMyBinding? = null
    private val binding get() = _binding!!
    private lateinit var filePath: String
    private lateinit var nicknameTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyBinding.inflate(inflater, container, false)
        val view = binding.root
        nicknameTextView = binding.nickname

        val sessionNickname = SharedPreferencesUtil.getSessionNickname(requireContext())
        Log.d("MyFragment", "$sessionNickname")
        setSessionNickname(sessionNickname)


        val requestGalleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                try {
                    val calRatio = calculateInSampleSize(
                        data!!.data!!,
                        resources.getDimensionPixelSize(R.dimen.imgSize),
                        resources.getDimensionPixelSize(R.dimen.imgSize)
                    )
                    val option = BitmapFactory.Options()
                    option.inSampleSize = calRatio

                    val inputStream = requireContext().contentResolver.openInputStream(data.data!!)
                    val bitmap = BitmapFactory.decodeStream(inputStream, null, option)
                    inputStream?.close()

                    bitmap?.let {
                        binding.profileimg.setImageBitmap(bitmap)
                    } ?: run {
                        Log.d("kkang", "bitmap null")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        binding.profile.setOnClickListener {
            val intent = Intent(requireContext(), UpdateActivity::class.java)
            startActivity(intent)
        }


        return view
    }

    private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        try {
            val inputStream = requireContext().contentResolver.openInputStream(fileUri)
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setSessionNickname(nickname: String?) {
        nicknameTextView.text = nickname ?: "기본 닉네임"
    }
}