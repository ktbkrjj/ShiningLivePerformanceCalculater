package com.example.kenju.shiningliveperformancecalculater

import android.content.Context
import android.content.SearchRecentSuggestionsProvider
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.TextView
import android.widget.BaseAdapter



class MainActivity : AppCompatActivity() {

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

    var mListView: SortableListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mListView = findViewById(R.id.bromideListView) as SortableListView
        mListView?.setDragListener(DragListener())
        mListView?.sortable = true
        mListView?.adapter = BromideAdapter(this, preset)
    }

    data class ViewHolder(val nameTextView: TextView, val attributeImageView: ImageView, val starTextView: TextView, val shineTextView: TextView, val dreamTextView: TextView)

    class BromideAdapter(context: Context, items: List<BromideData>) : ArrayAdapter<BromideData>(context, 0, items) {
        val layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var view = convertView
            var holder: ViewHolder

            if (view == null) {
                view = layoutInflater.inflate(R.layout.listview_item, parent, false)
                holder = ViewHolder(
                        view?.findViewById<TextView>(R.id.itemName) as TextView,
                        view?.findViewById<ImageView>(R.id.itemImageAttribute) as ImageView,
                        view?.findViewById<TextView>(R.id.itemStarValue) as TextView,
                        view?.findViewById<TextView>(R.id.itemShineValue) as TextView,
                        view?.findViewById<TextView>(R.id.itemDreamValue) as TextView
                )
                view?.tag = holder
            } else {
                holder = view?.tag as ViewHolder
            }

            val item = getItem(position) as BromideData
            holder.nameTextView.text = item.name
            holder.attributeImageView.setImageBitmap(BitmapFactory.decodeResource(context.resources, item.imageId))
            holder.starTextView.text = item.star.toString()
            holder.shineTextView.text = item.shine.toString()
            holder.dreamTextView.text = item.dream.toString()

            return view!!
        }
    }

    internal inner class DragListener : SortableListView.SimpleDragListener() {
        var mDraggingPosition = -1

        override fun onStartDrag(position: Int): Int {
            mDraggingPosition = position
            mListView?.invalidateViews()
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

            mDraggingPosition = positionTo
            mListView?.invalidateViews()
            return positionTo
        }

        override fun onStopDrag(positionFrom: Int, positionTo: Int): Boolean {
            mDraggingPosition = -1
            mListView?.invalidateViews()
            return super.onStopDrag(positionFrom, positionTo)
        }
    }
}
