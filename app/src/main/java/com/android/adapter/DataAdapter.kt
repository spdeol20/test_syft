package com.android.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.models.DataModel
import com.android.test.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item.view.*

class DataAdapter(val con: Context, val data: List<DataModel>?) : RecyclerView.Adapter<DataAdapter.myHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myHolder {
        return myHolder(LayoutInflater.from(con).inflate(R.layout.item,parent,false))
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    override fun onBindViewHolder(holder: myHolder, position: Int) {
     holder.itemView.tvName.text = data?.get(position)?.name
     holder.itemView.tvDes.text = data?.get(position)?.description
        Glide.with(con).load(data?.get(position)?.owner!!.avatar_url)
            .centerCrop()
            .into(holder.itemView.imgAvat);

//        holder.itemView.setOnClickListener(object :View.OnClickListener{
//            override fun onClick(view: View?) {
////                Toast.makeText(con,data?.get(position)?.name,Toast.LENGTH_SHORT);
//            }
//
//        })
        holder.itemView.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action){
                MotionEvent.ACTION_DOWN -> {
                Toast.makeText(con,data?.get(position)?.name,Toast.LENGTH_SHORT).show();
                }
                MotionEvent.ACTION_UP -> {
                }


                else -> {}
            }
        return@setOnTouchListener true}

    }

    class myHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}

