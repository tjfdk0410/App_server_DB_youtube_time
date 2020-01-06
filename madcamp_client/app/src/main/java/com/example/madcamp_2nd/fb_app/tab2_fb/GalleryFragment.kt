package com.example.madcamp_2nd.fb_app.tab2_fb


import android.Manifest

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.madcamp_2nd.R
import kotlinx.android.synthetic.main.gallery.*

import com.example.madcamp_2nd.fb_app.pojo.Photo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



//image pick code
private val IMAGE_PICK_CODE = 1000
//Permission code
private val PERMISSION_CODE = 1001

class GalleryFragment: Fragment() {

    var imgList = arrayListOf<Image>()
    private var SPAN_COUNT = 2
    lateinit var galleryAdapter: GalleryRVAdapter


    //add to upload
    var responseText: TextView? = null
    private var apiInterface: APIInterface? = null

    var FACE_BOOK_ID: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.gallery, container, false) //fragement 생성 위한 view를 gallery에서 띄우고 반환





//        /**
//         * GET List Resources
//         */
//        val call: Call<MultipleResource> = apiInterface!!.getallphoto()
//        call.enqueue(object : Callback<MultipleResource?> {
//            override fun onResponse(
//                call: Call<MultipleResource?>,
//                response: Response<MultipleResource?>
//            ) {
//                Log.d("TAG", response.code().toString() + "")
//                var displayResponse = ""
//                val resource: MultipleResource? = response.body()
//                val text: Int? = resource!!.page
//                val total: Int? = resource.total
//                val totalPages: Int? = resource.totalPages
//                val datumList: List<MultipleResource.Datum>? = resource.data
//                displayResponse += "$text Page\n$total Total\n$totalPages Total Pages\n"
//                if (datumList != null) {
//                    for (datum in datumList) {
//                        displayResponse += datum.id.toString() + " " + datum.name + " " + datum.pantoneValue + " " + datum.year + "\n"
//                    }
//                }
//                responseText!!.text = displayResponse
//            }
//
//            override fun onFailure(
//                call: Call<MultipleResource?>,
//                t: Throwable
//            ) {
//                call.cancel()
//            }
//        })
//
        /**
         * Create new user
         */

        /**
         * GET List Users
         */
//        val call2: Call<UserList> = apiInterface!!.doGetUserList("2")
//        call2.enqueue(object : Callback<UserList?> {
//            override fun onResponse(
//                call: Call<UserList?>,
//                response: Response<UserList?>
//            ) {
//                val userList: UserList? = response.body()
//                val text: Int? = userList!!.page
//                val total: Int? = userList.total
//                val totalPages: Int? = userList.totalPages
//                val datumList: ArrayList<Any?> = userList.data
//                Toast.makeText(
//                    context,
//                    "$text page\n$total total\n$totalPages totalPages\n",
//                    Toast.LENGTH_SHORT
//                ).show()
//                for (datum in datumList) {
//                    print( "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq")
////                    Toast.makeText(
////                        context,"id : " + datum.id.toString() + " name: " + datum.first_name.toString() + " " + datum.last_name.toString() + " avatar: " + datum.avatar,
////                        Toast.LENGTH_SHORT
////                    ).show()
//                }
//            }
//
//            override fun onFailure(
//                call: Call<UserList?>,
//                t: Throwable
//            ) {
//                call.cancel()
//            }
//        })
        /**
         * POST name and job Url encoded.
         */
//        val call3: Call<UserList> =
//            apiInterface!!.doCreateUserWithField("morpheus", "leader")
//        call3.enqueue(object : Callback<UserList?> {
//            override fun onResponse(
//                call: Call<UserList?>,
//                response: Response<UserList?>
//            ) {
//                val userList: UserList? = response.body()
//                val text: Int? = userList!!.page
//                val total: Int? = userList.total
//                val totalPages: Int? = userList.totalPages
//                val datumList: ArrayList<Any?> = userList.data
//                Toast.makeText(
//                    context,
//                    "$text page\n$total total\n$totalPages totalPages\n",
//                    Toast.LENGTH_SHORT
//                ).show()
//                for (datum in datumList) {
//                    print("22222222222222222222222222222222")
////                    Toast.makeText(
////                        context,
////                        "id : " + datum.id.toString() + " name: " + datum.first_name.toString() + " " + datum.last_name.toString() + " avatar: " + datum.avatar,
////                        Toast.LENGTH_SHORT
////                    ).show()
//                }
//            }
//
//            override fun onFailure(
//                call: Call<UserList?>,
//                t: Throwable
//            ) {
//                call.cancel()
//            }
//        })


        var view = inflater.inflate(R.layout.gallery, container, false) //fragement 생성 위한 view를 gallery에서 띄우고 반환

        val accessToken = AccessToken.getCurrentAccessToken()
        FACE_BOOK_ID = accessToken.userId


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        galleryAdapter = GalleryRVAdapter(requireContext(), imgList, requireActivity().supportFragmentManager)

        gallRecyclerView.adapter = galleryAdapter
//        galleryAdapter.listner = this

        loadImages()
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            SPAN_COUNT = 3
        }

        val layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT)
        gallRecyclerView.layoutManager = layoutManager
        gallRecyclerView.setHasFixedSize(true)


//        responseText = view.findViewById<TextView>(R.id.responsenseText)

        apiInterface = APIClient.client?.create(APIInterface::class.java)
        //Api interface 인스턴스화


        img_pick_btn.setOnClickListener {
            val photo= Photo("plzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz")
            apiInterface!!.createPhoto(photo)?.enqueue(object : Callback<Photo?> {
                override fun onResponse(
                    call: Call<Photo?>,
                    response: Response<Photo?>
                ) {
                    val taken: Photo? = response.body()
                    Toast.makeText(context, taken!!.photo, Toast.LENGTH_SHORT).show()
                    responsenseText.text = taken.photo
                    print("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                }

                override fun onFailure(
                    call: Call<Photo?>,
                    t: Throwable
                ) {
                    call.cancel()
                    print("bbbbbbbbbbbbbbbbbbbbbbbbbbbbb")
                }
            })
            //check runtime permission
//            if (VERSION.SDK_INT >= VERSION_CODES.M) {
//                if (ContextCompat.checkSelfPermission(
//                        requireContext(),
//                        Manifest.permission.READ_EXTERNAL_STORAGE
//                    ) ==
//                    PackageManager.PERMISSION_DENIED
//                ) { //permission denied
//                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
//                    //show popup to request runtime permission
//                    requestPermissions(permissions,
//                        PERMISSION_CODE
//                    );
//                } else {
//                    //permission already granted
//                    pickImageFromGallery()
//                }
//            } else {
//                //system OS is < Marshmallow
//                pickImageFromGallery()
//            }
        }



    }


    private fun loadImages(){
//        imgList.add(Image("apple", Uri.parse("")))
//        imgList.add(Image("cat", Uri.parse("")))
//        imgList.add(Image("dog", Uri.parse("")))
//        imgList.add(Image("lake", Uri.parse("")))
        galleryAdapter.notifyDataSetChanged()

    }

    fun onClick(position: Int) {

        val bundle = Bundle()
        bundle.putSerializable("images", imgList)
        bundle.putInt("position",position)

        val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()

        val galleryFullFragment =
            GalleryFullscreenFragment()

        galleryFullFragment.arguments = bundle
        galleryFullFragment.show(fragmentTransaction,"gallery")
    }


    /**
     *  pickImageFromGallery()
     *  through intent, call gallery activity
     *  go to gallery activity, and get some image
     */
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK) //action get content 고려
        intent.setType("image/*")
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) //activity 간의 인수와 리턴값을 전달(저장)
        startActivityForResult(Intent.createChooser(intent, "Select picture"),
            IMAGE_PICK_CODE
        )

    }


    /**
     *  onRequestPermissionResult()
     *  handle requested permission result
     *  if user choose OK, call pickImageFromGallery
     */
    //handle requested permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                    //context ** this => requireContext()
                }
            }
        }
    }


    /**
     *  onActivityResult()
     *  get result of gallery activity
     *  handle result of picked image
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {

            if (data == null) {
                //something is wrong
            }
            val clipData = data?.clipData
            if (clipData != null) {
                for (i in 0 until clipData.itemCount) {
                    val str = clipData.getItemAt(i).uri

//                    imgList.add(Image( "galleryphoto_0" + i, str))

                }
            }
            galleryAdapter.notifyDataSetChanged()
        }
    }
}
