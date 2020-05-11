package com.example.androidkotlinassignment.models

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bumptech.glide.Glide
import com.example.androidkotlinassignment.R

@Entity(tableName="facts")
class Fact(@field:PrimaryKey val id:String, val title:String, val description:String?, val imageHref: String?, val catId:String) {
    companion object{
        @JvmStatic @BindingAdapter("factImage")
        fun loadFactImage(view: ImageView, imageUrl:String?)
        {
            Glide.with(view.context).load(imageUrl).fitCenter().error(R.drawable.ic_image)
                .fallback(R.drawable.ic_image)
                .into(view)
        }
    }
}