package com.example.kenju.shiningliveperformancecalculater

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListAdapter
import android.widget.SimpleAdapter
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val list = mutableListOf<Map<String, String>>()
        for (i in 0..20) {
            list.add(mapOf("title" to "title $i", "detail" to "detail $i"))
        }

        val adapter = SimpleAdapter(
                this, // Context
                list, // 表示したいデータリスト
                android.R.layout.simple_list_item_2, // レイアウトファイルのID
                arrayOf("title", "detail"), // from: 表示したデータのMapのキー名
                intArrayOf(android.R.id.text1, android.R.id.text2) // to: レイアウトファイルに定義されているアイテムのID
        )
        bromideListView.adapter = adapter as ListAdapter

        bromideListView.setOnItemClickListener { parent, view, position, id ->
            val title = view.findViewById<TextView>(android.R.id.text1)
            val detail = view.findViewById<TextView>(android.R.id.text2)
            Toast.makeText(this, "${title.text} (${detail.text})", Toast.LENGTH_SHORT).show()
        }

    }
}
