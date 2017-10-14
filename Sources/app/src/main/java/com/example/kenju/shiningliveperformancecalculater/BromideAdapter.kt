package com.example.kenju.shiningliveperformancecalculater

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

data class ViewHolder(val nameTextView: TextView, val attributeImageView: ImageView, val starTextView: TextView, val shineTextView: TextView, val dreamTextView: TextView)

class BromideAdapter(context: Context, items: List<BromideData>) : ArrayAdapter<BromideData>(context, 0, items) {
    val layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        var holder: ViewHolder

        if (view == null) {
            view = layoutInflater.inflate(R.layout.listview_item, parent, false)
            holder = ViewHolder(
                    view.findViewById<TextView>(R.id.itemName) as TextView,
                    view.findViewById<ImageView>(R.id.itemImageAttribute) as ImageView,
                    view.findViewById<TextView>(R.id.itemStarValue) as TextView,
                    view.findViewById<TextView>(R.id.itemShineValue) as TextView,
                    view.findViewById<TextView>(R.id.itemDreamValue) as TextView
            )
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
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