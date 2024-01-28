package com.wish.bunny.home

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.wish.bunny.GlobalApplication
import com.wish.bunny.R
import com.wish.bunny.databinding.FragmentWishDetailBinding
import com.wish.bunny.util.RetrofitConnection
import com.wish.bunny.wish.WishService
import com.wish.bunny.wish.WishUpdateFragment
import com.wish.bunny.wish.domain.WishItem
import com.wish.bunny.wish.domain.WishMapResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
 /*
   작성자: 김은솔
   처리 내용: 위시리스트 디테일
 */
class WishDetailFragment : Fragment() {

    private var _binding: FragmentWishDetailBinding? = null
    private val binding get() = _binding!!

    private val selectedDate: Calendar = Calendar.getInstance()
    private val selectedButtons: MutableList<Button> = mutableListOf()

     private val accessToken = GlobalApplication.prefs.getString("accessToken", "")
     //private val accessToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2ZWE3NTFmOS1lNDhlLTQ1OWEtYjYwYi02MzFkMDM4ZmUwZmIiLCJpYXQiOjE3MDYyMjgzMDMsImV4cCI6MTcwODgyMDMwM30.x7mvX8xzWhd-lzB0xooHYIH9pSJfmsgzB7fe7tJhoUI"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWishDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val wishNo = arguments?.getString("wishNo")

        if (wishNo != null) {
            Log.d("WishDetailFragment", wishNo)

            loadWishDetail(wishNo, accessToken)
            view.findViewById<Button>(R.id.deleteBtn).setOnClickListener {
                showConfirmationDialog(wishNo)
            }
            view.findViewById<Button>(R.id.updateBtn).setOnClickListener {
                val newFragment = WishUpdateFragment()

                val bundle = Bundle()
                bundle.putString("wishNo", wishNo)
                newFragment.arguments = bundle

                replaceFragment(newFragment)
            }
        }

    }

     private fun replaceFragment(fragment: Fragment) {
         requireActivity().supportFragmentManager.beginTransaction()
             .replace(R.id.fragment_container, fragment)
             .addToBackStack(null)
             .commit()
     }
    private fun loadWishDetail(wishNo: String, accessToken:String) {
        val retrofitAPI = RetrofitConnection.getInstance().create(WishService::class.java)
        retrofitAPI.getWishDetail(wishNo, "$accessToken").enqueue(object : Callback<WishItem> {
            override fun onResponse(call: Call<WishItem>, response: Response<WishItem>) {
                val wishItem = response.body()

                if (wishItem != null) {
                    updateUI(wishItem)
                    Log.d("wishItem", wishItem.toString())

                } else {
                    Log.d("wishItem", "서버 응답이 null입니다.")
                }
            }

            override fun onFailure(call: Call<WishItem>, t: Throwable) {
                Log.d("wishItem", "불러오기 실패: ${t.message}")
                // TODO: 실패 시 처리 구현
            }
        })
    }
     private fun showConfirmationDialog(wishNo: String) {

         val alertDialogBuilder = AlertDialog.Builder(context)
         alertDialogBuilder.setTitle("버킷리스트 삭제")
         alertDialogBuilder.setMessage("정말로 해당 버킷리스트를 삭제처리 하시겠습니까?")

         //완료처리 Yes시
         alertDialogBuilder.setPositiveButton("Yes") { dialogInterface, _ ->
             //해당 버킷리스트 완료
             deleteWishDetail(wishNo)
             dialogInterface.dismiss()
         }
         //완료처리 No시
         alertDialogBuilder.setNegativeButton("No") { dialogInterface, _ ->
             dialogInterface.dismiss()
         }

         val alertDialog = alertDialogBuilder.create()
         alertDialog.show()
     }
     private fun deleteWishDetail(wishNo: String){
         val retrofitAPI = RetrofitConnection.getInstance().create(WishService::class.java)
         val call = retrofitAPI.deleteWish(wishNo)
         call.enqueue(object : Callback<WishMapResult> {
             override fun onResponse(call: Call<WishMapResult>, response: Response<WishMapResult>) {
                 val wishMapResult = response.body()

                 if (wishMapResult != null) {
                     Log.d("delete Wish", "삭제 성공 ${wishMapResult}" )
                    //삭제 성공시 이동
                     val newFragment = HomeFragment()
                     val bundle = Bundle()
                     bundle.putString("isMine", "1")
                     newFragment.arguments = bundle
                     replaceFragment(newFragment)

                 } else {
                     Log.d("delete Wish", "서버 응답이 null입니다.")
                 }
             }

             override fun onFailure(call: Call<WishMapResult>, t: Throwable) {
                 Log.d("WishList2", "불러오기 실패: ${t.message}")
                 // TODO: 실패 시 처리 구현
             }
         })
     }
    private fun updateUI(wishItem: WishItem) {
        binding.content.text = wishItem.content
        binding.hashtagButton1.text = '#' + wishItem.tagContents
        binding.tvSelectedDate.text = wishItem.deadlineDt
        EditDeleteButtonShowYn(wishItem.writerYn)
        setCategroy(wishItem.category)
    }

    private fun EditDeleteButtonShowYn(writerYn: String) {
        val editBtn = binding.updateBtn
        val deleteBtn = binding.deleteBtn

        if (writerYn.equals("Y")) {
            editBtn.visibility = View.VISIBLE
            deleteBtn.visibility = View.VISIBLE
        } else {
            editBtn.visibility = View.GONE
            deleteBtn.visibility = View.GONE
        }
    }


    private fun setCategroy(category: String) {
        val button1 = binding.button1
        val button2 = binding.button2
        val button3 = binding.button3

        val pinkColor = ContextCompat.getColor(requireContext(), R.color.pink)
        val changeTextColor = ContextCompat.getColor(requireContext(), R.color.white)
        val transparentColor = ContextCompat.getColor(requireContext(), R.color.ivory)
        val originalTextColor = ContextCompat.getColor(requireContext(), R.color.black)

        when (category) {
            "do" -> {
                updateCategoryButton(button1, pinkColor, changeTextColor, transparentColor, originalTextColor)
            }
            "eat" -> {
                updateCategoryButton(button2, pinkColor, changeTextColor, transparentColor, originalTextColor)
            }
            else -> {
                updateCategoryButton(button3, pinkColor, changeTextColor, transparentColor, originalTextColor)
            }
        }
    }

    private fun updateCategoryButton(
        button: Button,
        pinkColor: Int,
        changeTextColor: Int,
        transparentColor: Int,
        originalTextColor: Int
    ) {
        button.setBackgroundColor(pinkColor)
        button.setTextColor(changeTextColor)
        for (otherButton in listOf(binding.button1, binding.button2, binding.button3)) {
            if (otherButton != button) {
                otherButton.setBackgroundColor(transparentColor)
                otherButton.setTextColor(originalTextColor)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
