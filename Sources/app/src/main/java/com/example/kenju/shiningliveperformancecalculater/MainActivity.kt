package com.example.kenju.shiningliveperformancecalculater

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

private var preset = mutableListOf(
        BromideData("寿 嶺二",      R.drawable.star,  100, 400, 400),
        BromideData("黒崎蘭丸",     R.drawable.shine, 200, 300, 400),
        BromideData("美風 藍",      R.drawable.dream, 300, 200, 400),
        BromideData("カミュ",       R.drawable.star,  400, 100, 500),
        BromideData("一十木音也",   R.drawable.star,  200, 100, 200),
        BromideData("聖川真斗",     R.drawable.shine, 500, 100, 200),
        BromideData("四ノ宮那月",   R.drawable.shine, 300, 100, 200),
        BromideData("一ノ瀬トキヤ", R.drawable.dream, 400, 400, 100),
        BromideData("神宮寺レン",   R.drawable.dream, 200, 500, 100),
        BromideData("来栖翔",       R.drawable.shine, 100, 200, 500),
        BromideData("愛島セシル",   R.drawable.star,  500, 300, 400)
)

class MainActivity : AppCompatActivity() {

    var listView_: SortableListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView_ = findViewById(R.id.bromideListView) as SortableListView
        listView_?.setDragListener(DragListener())
        listView_?.sortable = true
        listView_?.adapter = BromideAdapter(this, preset)
    }

    internal inner class DragListener : SortableListView.SimpleDragListener() {
        var draggingPos_ = -1

        override fun onStartDrag(position: Int): Int {
            draggingPos_ = position
            listView_?.invalidateViews()
            return position
        }

        override fun onDuringDrag(positionFrom: Int, positionTo: Int): Int {
            if (positionFrom < 0 || positionTo < 0 || positionFrom == positionTo) {
                return positionFrom
            }

            var i: Int
            if (positionFrom < positionTo) {
                val data = preset[positionFrom]
                i = positionFrom
                while (i < positionTo) {
                    preset[i] = preset[++i]
                }
                preset[positionTo] = data
            } else if (positionFrom > positionTo) {
                val data = preset[positionFrom]
                i = positionFrom
                while (i > positionTo) {
                    preset[i] = preset[--i]
                }
                preset[positionTo] = data
            }

            draggingPos_ = positionTo
            listView_?.invalidateViews()
            return positionTo
        }

        override fun onStopDrag(positionFrom: Int, positionTo: Int): Boolean {
            draggingPos_ = -1
            listView_?.invalidateViews()
            return super.onStopDrag(positionFrom, positionTo)
        }
    }
}
