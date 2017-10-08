package com.example.kenju.shiningliveperformancecalculater

import android.content.Context
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val names = listOf(
                "寿 嶺二",
                "黒崎蘭丸",
                "美風 藍",
                "カミュ",
                "一十木音也")
        val images = listOf(
                R.drawable.dream,
                R.drawable.dream,
                R.drawable.shine,
                R.drawable.shine,
                R.drawable.star)
        val star = listOf("100", "200", "300", "400", "500")
        val shine = listOf("100", "200", "300", "400", "500")
        val dream = listOf("100", "200", "300", "400", "500")

        val flowers = mutableListOf<BromideData>()
        for (i in 0..names.count()-1) {
            flowers.add(BromideData(names[i], images[i], star[i], shine[i], dream[i]))
        }

        val adapter = BromideAdapter(this, flowers)
        bromideListView.adapter = adapter

    }

    data class ViewHolder(val nameTextView: TextView, val attributeImageView: ImageView, val starTextView: TextView, val shineTextView: TextView, val dreamTextView: TextView)

    class BromideAdapter(context: Context, flowers: List<BromideData>) : ArrayAdapter<BromideData>(context, 0, flowers) {
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
            holder.starTextView.text = item.star
            holder.shineTextView.text = item.shine
            holder.dreamTextView.text = item.dream

            return view!!
        }
    }
}
