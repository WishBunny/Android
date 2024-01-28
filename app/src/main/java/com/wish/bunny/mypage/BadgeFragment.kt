package com.wish.bunny.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wish.bunny.R
import com.wish.bunny.mypage.domain.GridItem


class BadgeFragment : Fragment() {
    var gridadapter: GridItemAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_badge, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_list)

        gridadapter = GridItemAdapter()
        recyclerView?.adapter = gridadapter

        //임의로 데이터를 넣어줌
        getItem()

        return view
    }

    private fun getItem() {
        var data = GridItem(R.drawable.ic_dialog)
        gridadapter?.addItem(data)
        data = GridItem(R.drawable.ic_dialog)
        gridadapter?.addItem(data)
        data = GridItem(R.drawable.ic_dialog)
        gridadapter?.addItem(data)
        data = GridItem(R.drawable.ic_dialog)
        gridadapter?.addItem(data)
        data = GridItem(R.drawable.ic_dialog)
        gridadapter?.addItem(data)
        data = GridItem(R.drawable.ic_dialog)
        gridadapter?.addItem(data)
        data = GridItem(R.drawable.ic_dialog)
        gridadapter?.addItem(data)
        data = GridItem(R.drawable.ic_dialog)
        gridadapter?.addItem(data)
    }
}