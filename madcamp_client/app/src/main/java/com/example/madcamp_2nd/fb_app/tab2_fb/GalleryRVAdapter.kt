package com.example.madcamp_2nd.fb_app.tab2_fb

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_2nd.R

//import com.thesimplycoder.imagegallery.adapter.Image

class GalleryRVAdapter(val context : Context?, val itemList:ArrayList<Image>, val supportFragmentManager: FragmentManager) : RecyclerView.Adapter<GalleryRVAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.gallery_rv_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gallPhoto = itemView.findViewById<ImageView>(R.id.gallphoto)


        fun bind(Im: Image) {
//            if (Im.uri == Uri.parse("")){
//                if (context != null) {
//                    val resourceId = context.resources.getIdentifier(Im.name,
//                        "drawable",
//                        context.packageName
//                    )
//                    gallPhoto.setImageResource(resourceId)
//                }
//
//            }
//            else  {
//                gallPhoto.setImageURI(Im.uri)
//            }

            gallPhoto.setOnClickListener {

                val bundle = Bundle()
                var position: Int = 0
                for ((i, image) in itemList.withIndex()) {
                    if (image == Im) {
                        position = i
                    }
                }
                bundle.putSerializable("images", itemList)
                bundle.putInt("position", position)

//                // handle click of image
                val fragmentTransaction = supportFragmentManager.beginTransaction()
////                supportFragmentManager.beginTransaction().replace(R.id.gallphoto, GalleryFullscreenFragment()).commit()
                val galleryFullFragment =
                    GalleryFullscreenFragment()//full인지 아닌지
                galleryFullFragment.arguments = bundle
                galleryFullFragment.show(fragmentTransaction, "gallery")


            }
        }
    }
}












