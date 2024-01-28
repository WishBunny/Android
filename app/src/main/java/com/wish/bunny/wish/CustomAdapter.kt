package com.wish.bunny.wish

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wish.bunny.R
import com.wish.bunny.util.RetrofitConnection
import com.wish.bunny.wish.domain.WishItem
import com.wish.bunny.wish.domain.WishMapResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import android.app.AlertDialog
import android.widget.ImageButton
import androidx.core.content.ContextCompat

/**
작성자: 김은솔
처리 내용: 위시리스트 CustomAdapter
 */
class CustomAdapter(private val context: Context, private val wishItemList: List<WishItem>,
                    private val writerYn: String) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    interface OnDetailButtonClickListener {
        fun onDetailButtonClick(wishNo: String)
    }

    private var onDetailButtonClickListener: OnDetailButtonClickListener? = null

    fun setOnDetailButtonClickListener(listener: OnDetailButtonClickListener) {
        this.onDetailButtonClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_wish, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(wishItemList[position])
        val currentItem = wishItemList[position]
    }

    override fun getItemCount(): Int {
        return wishItemList.size
    }

    /**
    작성자: 김은솔
    처리 내용: 위시리스트 각각의 상세 내용 Dispathcer
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val content = itemView.findViewById<TextView>(R.id.rv_content)
        private val dDay = itemView.findViewById<TextView>(R.id.rv_dDay)
        private val tag1 = itemView.findViewById<TextView>(R.id.rv_tag1)
        private val doneBtn = itemView.findViewById<ImageButton>(R.id.rv_detail_btn)
        private val tv_emoji = itemView.findViewById<TextView>(R.id.tv_emoji)

        init {
            //컨텐츠 내용 클릭시 상세 페이지 이동
            itemView.findViewById<TextView>(R.id.rv_content).setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDetailButtonClickListener?.onDetailButtonClick(wishItemList[position].wishNo)
                }
            }
            //완료 버튼 클릭시
            itemView.findViewById<ImageButton>(R.id.rv_detail_btn).setOnClickListener {
                showConfirmationDialog(wishItemList[position].wishNo)
            }
        }

        //버킷리스트 완료 확인 메세지
        private fun showConfirmationDialog(wishNo: String) {
            val context = itemView.context
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setTitle("버킷리스트 완료")
            alertDialogBuilder.setMessage("정말로 해당 버킷리스트를 완료처리 하시겠습니까?")

            //완료처리 Yes시
            alertDialogBuilder.setPositiveButton("Yes") { dialogInterface, _ ->
                //해당 버킷리스트 완료
                doneWishDetail(wishNo)
                dialogInterface.dismiss()
            }
            //완료처리 No시
            alertDialogBuilder.setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        //버킷리스트 상세 완료 처리
        private fun doneWishDetail(wishNo : String) {
            val retrofitAPI = RetrofitConnection.getInstance().create(WishService::class.java)
            retrofitAPI.finishWish(wishNo).enqueue(object : Callback<WishMapResult> {
                override fun onResponse(call: Call<WishMapResult>, response: Response<WishMapResult>) {
                    val wishMapResult = response.body()

                    if (wishMapResult != null) {
                        // updateUI(wishMapResult.list)
                        //Log.d("doneWishDetail", wishMapResult.result.toString())
                    } else {
                        Log.d("doneWishDetail", "서버 응답이 null입니다.")
                    }
                }

                override fun onFailure(call: Call<WishMapResult>, t: Throwable) {
                    Log.d("doneWishDetail", "불러오기 실패: ${t.message}")

                }
            })
        }

        fun bind(wishItem: WishItem) {
            val title = wishItem.content
            val wishNo = wishItem.wishNo

            Log.d("wishItem", wishItem.toString())
            tv_emoji.text = wishItem.emoji
            content.text = wishItem.content
            dDay.text = calculateDDay(wishItem.deadlineDt)
            tag1.text = "#" + wishItem.tagContents
         
            //로그인 아이디와 작성자가 다른 경우 완료버튼 안보이게
            if(writerYn.equals("Y")){
                doneBtn.visibility = View.VISIBLE
                //성취했을 경우 색깔 변화
                if(wishItem.achieveYn.equals("Y")){
                    doneBtn.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_check_selected))
                }

            }else{
                doneBtn.visibility = View.GONE
            }
        }

        private fun calculateDDay(deadlineDt: String): String {
            if (deadlineDt == null)
                return ""
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val targetDate = LocalDate.parse(deadlineDt, dateFormatter)

            //현재날짜
            val currentDate = LocalDate.now()

            //남은 일수 계산
            val daysRemaining = ChronoUnit.DAYS.between(currentDate, targetDate)

            if (daysRemaining == 0L)
                return "D-Day"
            else if(daysRemaining >= 0)
                return "D-${daysRemaining.toString()}"
            else
                return "D+${Math.abs(daysRemaining).toString()}"
        }
    }
}
