package com.wish.bunny.mypage

import AWSS3Manager
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import androidx.fragment.app.Fragment
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.kakao.sdk.user.UserApiClient
import com.wish.bunny.BuildConfig.AWS_BUCKET_NAME
import com.wish.bunny.BuildConfig.AWS_REGION
import com.wish.bunny.GlobalApplication
import com.wish.bunny.R
import com.wish.bunny.databinding.FragmentMypageBinding
import com.wish.bunny.mypage.domain.ProfileGetResponse
import com.wish.bunny.mypage.domain.ProfileUpdateRequest
import com.wish.bunny.mypage.domain.WishCountResponse
import com.wish.bunny.onboarding.OnboardingActivity
import com.wish.bunny.util.RetrofitConnection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import kotlin.properties.Delegates

class MypageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    private val PICK_IMAGE = 1
    var updatedImageUri: Uri? = null
    lateinit var memberNo: String
    lateinit var profileImgUrl: String

    //이룬 위시 리스트 개수
    var countAchievedDo: Int = 0
    var countAchievedEat: Int = 0
    var countAchievedGet: Int = 0
    var countDo: Int = 0
    var countEat: Int = 0
    var countGet: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        // 통신을 위한 레트로핏 인스턴스 선언
        val retrofitAPI = RetrofitConnection.getInstance().create(MyPageService::class.java)
        loadMyProfileInfo(retrofitAPI)

        // 로그아웃과 관련된 버튼 세팅
        logOutSetting()
        // 닉네임 변경과 관련된 키보드 세팅
        keyboardSetting()
        // 사진 변경과 관련된 세팅
        pictureForEditSetting()
        binding.btnConfirm.setOnClickListener {
            Log.d("MyPageFragment", "프로필 정보 업데이트 시도")
            updateMemberInfo(retrofitAPI)
        }


        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data!!.data != null) {
            Log.d("MyPageFragment", data.toString())
            // data로부터 content uri 가져오기
            val selectedImageUri = data.data
            binding.imgProfile.setImageURI(selectedImageUri)
            updatedImageUri = selectedImageUri!!
        }
    }

    /**
        작성자: 엄상은
        처리 내용: 프로필 정보 업데이트 (통신)
    */
    private fun updateMemberInfo(retrofitAPI: MyPageService) {
        var profileUpdateRequest = ProfileUpdateRequest(
            email = binding.tvEmail.text.toString(),
            imgUrl = profileImgUrl,
            nickname = binding.editNickname.text.toString(),
            memberId = binding.tvMemberId.text.toString()
        )
        // 이미지 변경이 있다면 S3에 새 이미지 업로드
        if (updatedImageUri != null) {
            profileUpdateRequest.imgUrl = uploadFileByUri(updatedImageUri)
            Glide.with(this)
                .load(profileUpdateRequest.imgUrl)
                .into(binding.imgProfile)
        }
        retrofitAPI.updateMyProfile(
            GlobalApplication.prefs.getString("accessToken", ""),
            profileUpdateRequest
        ).enqueue(object : Callback<ProfileGetResponse> {
            override fun onResponse(
                call: Call<ProfileGetResponse>,
                response: Response<ProfileGetResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("updateMemberInfo", "프로필 정보 업데이트 성공")
                    response.body()?.let { setMyProfileInfo(it) }
                } else {
                    Log.d("updateMemberInfo", "프로필 정보 업데이트 실패")
                }
            }

            override fun onFailure(call: Call<ProfileGetResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    /**
        작성자: 엄상은
        처리 내용: 프로필 사진 변경 세팅
    */
    private fun pictureForEditSetting() {
        binding.imgEditphoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE)
        }
        binding.imgProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE)
        }
    }

    /**
        작성자: 엄상은
        처리 내용: 이미지 업로드
    */
    private fun uploadFileByUri(selectedImageUri: Uri?): String {
        // 확장자 추출
        val mimeType = selectedImageUri?.let { context?.contentResolver?.getType(it) }
        val extensionType = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
        // URI로부터 File 객체 생성
        val selectedImageFile = uriToFile(requireContext(), selectedImageUri!!)
        // S3에 업로드
        return uploadToS3(selectedImageFile, memberNo + "." + extensionType)
    }

    /**
        작성자: 엄상은
        처리 내용: URI로부터 File 객체 생성
    */
    private fun uriToFile(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "tempFile")
        file.deleteOnExit()
        FileOutputStream(file).use {
            inputStream?.copyTo(it)
        }
        return file
    }

    /**
        작성자: 엄상은
        처리 내용: S3에 이미지 업로드
    */
    private fun uploadToS3(file: File, fileName: String): String {
        val s3Client = AWSS3Manager.getInstance()
        val transferUtility = TransferUtility.builder().s3Client(s3Client).context(context).build()

        val observer = transferUtility.upload(
            "${AWS_BUCKET_NAME}", // 업로드할 버킷 이름
            fileName, // 파일 이름
            file, // 업로드할 파일
            CannedAccessControlList.PublicRead // 권한
        )

        observer.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (TransferState.COMPLETED == state) {
                    // 업로드 완료
                    Log.d("uploadToS3", "Upload Complete!")
                }
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                val percentDonef = bytesCurrent.toFloat() / bytesTotal.toFloat() * 100
                val percentDone = percentDonef.toInt()
                Log.d("uploadToS3", "ID:$id bytesCurrent: $bytesCurrent bytesTotal: $bytesTotal $percentDone%")
            }

            override fun onError(id: Int, ex: Exception) {
                // Handle errors
            }
        })
        return "https://${AWS_BUCKET_NAME}.s3.${AWS_REGION}.amazonaws.com/$fileName"
    }

    /**
        작성자: 엄상은
        처리 내용: 키보드 액션 설정
    */
    private fun keyboardSetting() {
        // edit 이미지 클릭 시 키보드 입력 받기
        binding.imgEditnickname.setOnClickListener {
            binding.editNickname.requestFocus()
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.editNickname, InputMethodManager.SHOW_IMPLICIT)
            // 텍스트의 맨 뒤로 커서 이동
            binding.editNickname.setSelection(binding.editNickname.text.length)
        }

        // 다른 화면 클릭 시 키보드 내리기
        binding.root.setOnClickListener{
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
            binding.editNickname.clearFocus()
        }
    }

    /**
        작성자: 엄상은
        처리 내용: 카카오 프로필 정보 가져오기
    */
    private fun loadMyProfileInfo(retrofitAPI: MyPageService) {
        Log.d("loadMyProfileInfo", "프로필 정보 불러오기 시도")
        var accessToken = GlobalApplication.prefs.getString("accessToken", "")
        Log.d("loadMyProfileInfo", accessToken)
        accessToken?.let {
            retrofitAPI.loadMyProfile(it).enqueue(object :
                Callback<ProfileGetResponse> {
                override fun onResponse(
                    call: Call<ProfileGetResponse>,
                    response: Response<ProfileGetResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("loadMyProfileInfo", "프로필 정보 불러오기 성공")
                        response.body()?.let { setMyProfileInfo(it) }

                        //이룬 위시리스트 개수 조회
                        countWish(retrofitAPI, memberNo)
                    } else {
                        Log.d("loadMyProfileInfo", "프로필 정보 불러오기 시도")
                        Log.d("loadMyProfileInfo", "프로필 정보 불러오기 실패")
                    }
                }

                override fun onFailure(call: Call<ProfileGetResponse>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }

    /**
        작성자: 엄상은
        처리 내용: 프로필 정보 UI 세팅
    */
    private fun setMyProfileInfo(it: ProfileGetResponse) {
        memberNo = it.data.memberId
        binding.tvMemberId.text = it.data.memberId
        binding.editNickname.setText(it.data.nickname)
        binding.tvEmail.text = it.data.email
        profileImgUrl = it.data.imgUrl
        Glide.with(this)
            .load(it.data.imgUrl)
            .into(binding.imgProfile)
    }

    /**
        작성자: 엄상은
        처리 내용: 로그아웃, 계정 연결 끊기 버튼 세팅
    */
    private fun logOutSetting() {
        binding.LogoutBtn.setOnClickListener {
            logout()
            val intent = Intent(activity, OnboardingActivity::class.java)
            startActivity(intent)
        }
        binding.DisconnectBtn.setOnClickListener {
            disconnect()
            val intent = Intent(activity, OnboardingActivity::class.java)
            startActivity(intent)
        }
    }

    /**
        작성자: 엄상은
        처리 내용: 카카오 연결 끊기 처리
    */
    private fun disconnect() {
        Log.d("MyPageFragement", "회원탈퇴 시도")
        GlobalApplication.prefs.removeKeyWithValue("accessToken")
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e("MyPageFragement", "연결 끊기 실패", error)
            }
            else {
                Log.i("MyPageFragement", "연결 끊기 성공. SDK에서 토큰 삭제 됨")
            }
        }
    }

    /**
        작성자: 엄상은
        처리 내용: 로그아웃 처리
     */
    private fun logout() {
        Log.d("MyPageFragement", "로그아웃 시도")
        GlobalApplication.prefs.removeKeyWithValue("accessToken")
        val accessToken = GlobalApplication.prefs.getString("accessToken", "")
        if (accessToken == "") {
            Log.d("MyPageFragement", "로그아웃 완료")
        } else {
            Log.d("MyPageFragement", "로그아웃 실패")
        }
    }

    /**
    작성자: 이혜연
    처리 내용: 위시리스트 개수 조회
     */
    private fun countWish(retrofitAPI: MyPageService, memberId: String) {
        retrofitAPI.countWish(memberId).enqueue(object :
            Callback<WishCountResponse> {
            override fun onResponse(
                call: Call<WishCountResponse>,
                response: Response<WishCountResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("countWish", "위시 리스트 개수 조회 성공")
                    response.body()?.let {
                        Log.d("하고 싶어요 개수", it.data.toString())

                        binding.tvDoCount.text = it.data.countAchievedDo.toString()
                        binding.tvEatCount.text = it.data.countAchievedEat.toString()
                        binding.tvGetCount.text = it.data.countAchievedGet.toString()

                        Log.d("하고 싶어요 전체 개수",it.data.toString())

                        countAchievedDo = it.data.countAchievedDo
                        countAchievedEat = it.data.countAchievedEat
                        countAchievedGet = it.data.countAchievedGet
                        countDo = it.data.countDo
                        countEat = it.data.countEat
                        countGet = it.data.countGet

                        //탭 레이아웃
                        val percentDo: Float =
                            if (countDo != 0) (countAchievedDo.toFloat() / countDo.toFloat()) * 100 else 0.0f
                        val percentEat: Float =
                            if (countEat != 0) (countAchievedEat.toFloat() / countEat.toFloat()) * 100 else 0.0f
                        val percentGet: Float =
                            if (countGet != 0) (countAchievedGet.toFloat() / countGet.toFloat()) * 100 else 0.0f
                        setTabLayout(percentDo, percentEat, percentGet)
                    }
                } else {
                    Log.d("countWish", "위시 리스트 개수 조회 실패")
                    Log.d("countWish", response.toString())
                }
            }

            override fun onFailure(call: Call<WishCountResponse>, t: Throwable) {
                Log.e("countWish", "Failed to execute API", t)
                t.printStackTrace()
            }
        })
    }

    private var chartDoFragment: ChartDoFragment? = null

    private fun setTabLayout(percentDo: Float, percentEat: Float, percentGet: Float) {

        showChartFragment(percentDo, percentEat, percentGet)

        binding.fragmentTablayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            // tab이 선택되었을 때
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0 -> {
                        replaceFragment(
                            R.id.fragment_mytab,
                            ChartDoFragment(percentDo, percentEat, percentGet)
                        )
                    }

                    1 -> {
                        replaceFragment(
                            R.id.fragment_mytab,
                            ChartEatFragment(percentDo, percentEat, percentGet)
                        )
                    }
                    2 -> {
                        replaceFragment(
                            R.id.fragment_mytab,
                            ChartGetFragment(percentDo, percentEat, percentGet)
                        )
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun replaceFragment(containerId: Int, fragment: Fragment) {
        try {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(null)
                .commit()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("replaceFragment", "Fragment transaction failed: ${e.message}")
        }
    }

    private fun showChartFragment(percentDo: Float, percentEat: Float, percentGet: Float) {
        if (chartDoFragment == null) {
            // 프래그먼트가 생성되지 않았다면 생성하고 추가
            chartDoFragment = ChartDoFragment(percentDo, percentEat, percentGet)
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.fragment_mytab, chartDoFragment!!)
                .commit()
        } else {
            // 이미 생성된 프래그먼트가 있다면 show를 호출하여 보여줌
            requireActivity().supportFragmentManager.beginTransaction()
                .show(chartDoFragment!!)
                .commit()
        }
    }
        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }
