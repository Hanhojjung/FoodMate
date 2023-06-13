package com.example.foodmate2

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
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.foodmate2.databinding.FragmentMyBinding

class MyFragment : Fragment() {
    private var _binding: FragmentMyBinding? = null
    private val binding get() = _binding!!
    lateinit var filePath: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyBinding.inflate(inflater, container, false)
        val view = binding.root

        val requestGalleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        {
            // it -> 사진 데이터
            try {
                // calRadto -> 결과 값은 정수 인데, 예를 들어 4
                // option.inSampleSize = calRatio -> 1/4 비율로 줄인다는 것을 의미
                // 특정 값의 비율을 임의로 정해도 되지만, 특정 사이즈를 요구한 값이 있으면
                // 그 값에 맞추는 함수를 만들었다. : calculateInSampleSize

                val calRatio = calculateInSampleSize(
                    it.data!!.data!!,
                    // 정적 res -> values -> dimens.xml 특정 크기를정해서 재사용
                    // or 하드 코딩으로 150dp 주어도 된다.
                    resources.getDimensionPixelSize(R.dimen.imgSize),
                    resources.getDimensionPixelSize(R.dimen.imgSize)
                )
                // BitmapFactory 사진을 읽는 업무, Options()를 통해서 크기를 지정
                val option = BitmapFactory.Options()
                option.inSampleSize = calRatio

                //inputStream : 사진을 바이트로 읽은 데이터가 있다.
                var inputStream = contentResolver.openInputStream(it.data!!.data!!)
                // 바이트 타입 -> bitmap 타입 형식으로 사진의 타입을 변경.
                // 사진의 결과는 원본 사이즈가 줄어서, bitmap 타입으로 변경 되었다.
                val bitmap = BitmapFactory.decodeStream(inputStream, null, option)
                inputStream!!.close()
                inputStream = null

                bitmap?.let {
                    // 이제 드디어 원하는 사진을 내가 원하는 크기에 맞게끔 할당
                    // bitmap : 리사이즈가 된 사진이 들어 있다.
                    binding.userImageView.setImageBitmap(bitmap)
                } ?: let{
                    Log.d("kkang", "bitmap null")
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }

        // 시작, 갤러리 버튼을 클릭, 이벤트 발생
        binding.galleryButton.setOnClickListener {
            //gallery app........................
            //Intent.ACTION_PICK -> 갤러리 앱 호출, 두번째 매개 변수의 타입을 보고 판단. : MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            // 속성의 값은 모든 이미지를 의미, MIMEType
            intent.type = "image/*"
            // 후처리 작업 시작, 현재 액티비티 -> 갤러리 앱 : 사진 선택 -> 현재 액티비티 돌아옴.
            // 위에 정의된  requestGalleryLauncher 코드로 이동
            requestGalleryLauncher.launch(intent)
        }



        return view

    }
    fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
        //options, BitmapFactory 사진을 처리하는 업무, 그런데 Options가 들어가면
        // 사진 처리 업무보다는 관련 옵션을 정한다.
        val options = BitmapFactory.Options()
        // inJustDecodeBounds 가 true가 되면 본업무 말고 옵션만 처리한다.
        options.inJustDecodeBounds = true
        try {
            // contentResolver.openInputStream 선택된 사진을 바이트로 읽어서 바이트로 반환
            // inputStream : 사진이 바이트 형으로 읽어 놓은 상태. getActivity().getContentResolver()
//            var inputStream = contentResolver.openInputStream(fileUri)
            var inputStream = getActivity()?.getContentResolver()?.openInputStream(fileUri)

            //inJustDecodeBounds 값을 true 로 설정한 상태에서 decodeXXX() 를 호출.
            //로딩 하고자 하는 이미지의 각종 정보가 options 에 설정 된다.
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream!!.close()
            inputStream = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //비율 계산........................
        // 읽은 사진의 가로 세로 정보를 , 재할당 height width
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        //원본 사이즈 그대로, 즉, 비율이 좋지 않음
        var inSampleSize = 1
        //inSampleSize 비율 계산
        //height  불러온 원본 사진의 실제 높이,
        //reqHeight : 내가 꾸미는 뷰의 높이 : 150dp
        // 예를 들어 : 원본 사진의 높이 2000dp
        // 원한느 사이즈 : 150dp
        if (height > reqHeight || width > reqWidth) {

            // 반으로 줄이기 .
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }


}