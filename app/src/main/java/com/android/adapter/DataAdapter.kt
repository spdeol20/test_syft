package com.android.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.models.DataModelNew
import com.android.test.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item.view.*

class DataAdapter(val con: Activity, val data: ArrayList<DataModelNew>?) : RecyclerView.Adapter<DataAdapter.myHolder>() {
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

        holder.itemView.setOnClickListener(object :View.OnClickListener{
            override fun onClick(view: View?) {
                Toast.makeText(con,data?.get(position)?.name,Toast.LENGTH_SHORT);
            }

        })


    }

    class myHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}

