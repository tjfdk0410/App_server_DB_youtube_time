package com.example.madcamp_2nd.fb_app.tab2_fb


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.madcamp_2nd.R
import com.example.madcamp_2nd.fb_app.pojo.Photo
import kotlinx.android.synthetic.main.gallery.*

import com.facebook.AccessToken
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.DataOutputStream
import java.io.File
import java.io.FileInputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList
import kotlin.math.min


//image pick code
private val IMAGE_PICK_CODE = 1000
//Permission code
private val PERMISSION_CODE = 1001

class GalleryFragment: Fragment() {

    var imgList = arrayListOf<Image>()
    var imgList_web = arrayListOf<String>()
    private var SPAN_COUNT = 2
    lateinit var galleryAdapter: GalleryRVadapter

    //add to upload
    private var apiInterface: APIInterface? = null
    private var FACE_BOOK_ID: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.gallery, container, false) //fragement 생성 위한 view를 gallery에서 띄우고 반환



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        print("11111111111111111111111")
        galleryAdapter = GalleryRVadapter(requireContext(), imgList, requireActivity().supportFragmentManager)
        gallRecyclerView.adapter = galleryAdapter
        print("122222222222222222222222211")
        val accessToken = AccessToken.getCurrentAccessToken()
        FACE_BOOK_ID = accessToken.userId
        galleryAdapter.notifyDataSetChanged()

        loadImages()
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            SPAN_COUNT = 3
        }

        val layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT)
        gallRecyclerView.layoutManager = layoutManager
        gallRecyclerView.setHasFixedSize(true)


        //Api interface 인스턴스화
        img_pick_btn.setOnClickListener {
            // 가져오기
            apiInterface = APIClient.client?.create(APIInterface::class.java)

            var obj: JSONObject? = null

//            var objlist= ArrayList<JSONObject>()
//            objlist.add(obj!!)

            apiInterface!!.getAll()?.enqueue(object : Callback<ArrayList<String>> {
                override fun onResponse(call: Call<ArrayList<String>>, response: Response<ArrayList<String>>) {
                    Log.v(" ", response.body().toString())
                    print(response.body())
                    for (i in 0 until response.body()!!.size){
                        imgList.add(Image("", Uri.parse("http://192.249.19.254:8080" + response.body()!![i])))
                    }
                    galleryAdapter.notifyDataSetChanged()
                    gallRecyclerView.adapter = galleryAdapter
                }

                override fun onFailure(
                    call: Call<ArrayList<String>>,
                    t: Throwable
                ) {
                    Log.e("error", t.toString())
                    call.cancel()
                }
            })
        }

        var obj: JSONObject? = null
//        obj!!.put("originalname", "dd")
//        obj!!.put("file", "dd")
        imgbutton.setOnClickListener {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) ==
                    PackageManager.PERMISSION_DENIED
                ) { //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(
                        permissions,
                        com.example.madcamp_2nd.local_app.tab2_local.PERMISSION_CODE
                    );
                } else {
                    //permission already granted
                    pickImageFromGallery()
                }
            } else {
                //system OS is < Marshmallow
                pickImageFromGallery()
            }
        }

//        postButton.setOnClickListener {
//            apiInterface = APIClient.client?.create(APIInterface::class.java)
//            val photo = obj?.let { it1 ->
//                Photo("plzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz",
//                    it1
//                )
//            }
//            apiInterface!!.createPhoto(photo)?.enqueue(object : Callback<Photo?> {
//                override fun onResponse(
//                    call: Call<Photo?>,
//                    response: Response<Photo?>
//                ) {
//                    val taken: Photo? = response.body()
//                    Toast.makeText(context, taken!!.name, Toast.LENGTH_SHORT).show()
//                    print("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
//                }
//
//                override fun onFailure(
//                    call: Call<Photo?>,
//                    t: Throwable
//                ) {
//                    call.cancel()
//                    print("bbbbbbbbbbbbbbbbbbbbbbbbbbbbb")
//                }
//            })
//        }
    }

    interface UploadAPIs {
        @Multipart
        @POST("/upload")
        fun uploadImages(
            @Part files: List<MultipartBody.Part>?,
            @Part("user_id") user_id: RequestBody?
        ): Call<ResponseBody?>
    }
    private fun prepareFilePart(partName: String, filepath: String): MultipartBody.Part {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        val file = File(filepath)
        // create RequestBody instance from file
        val requestFile =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

//    private fun uploadToServer(filePath: ArrayList<String>) {
//        val retrofit: Retrofit = NetworkClient.getRetrofitClient(getActivity())
//        val uploadAPIs = retrofit.create(dd.UploadAPIs::class.java)
//        // create list of file parts (photo, video, ...)
//        val parts: MutableList<MultipartBody.Part> =
//            ArrayList()
//        for (i in filePath.indices) {
//            parts.add(prepareFilePart("image", filePath[i]))
//        }
//        //
//        val accessToken = AccessToken.getCurrentAccessToken()
//        //Create request body with Facebook id for auth.
//        val usr_id =
//            RequestBody.create(MediaType.parse("text/plain"), accessToken.userId)
//        //        Call call = uploadAPIs.uploadImage(part, description, usr_id);
//        val call: Call<*> = uploadAPIs.uploadImages(parts, usr_id)
//        call.enqueue(object : Callback<Any?> {
//            override fun onResponse(
//                call: Call<*>?,
//                response: Response<*>
//            ) {
//                Log.d("File upload response", response.toString())
//            }
//
//            override fun onFailure(
//                call: Call<*>?,
//                t: Throwable
//            ) {
//                t.printStackTrace()
//            }
//        })
//    }


    fun multipartRequest(urlTo: String, params: Map<String, String>, filepath: String, filefield: String, fileMimeType: String): String {

        val twoHyphens = "--"
        val boundary = "*****" + System.currentTimeMillis().toString() + "*****"
        val lineEnd = "\r\n"
        val maxBufferSize = 5 * 1024 * 1024
        val q = filepath.split("/")
        val idx = q.size- 1
        try {
            val file = File(filepath)
            val fileInputStream = FileInputStream(file)
            val url = URL(urlTo)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.doOutput = true
            connection.useCaches = false
            connection.requestMethod = "POST"
            connection.setRequestProperty("Connection", "Keep-Alive")
            connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0")
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
            val outputStream = DataOutputStream(connection.outputStream)
            outputStream.writeBytes(twoHyphens + boundary + lineEnd)
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + filefield + "\"; filename=\"" + q[idx] + "\"" + lineEnd)
            outputStream.writeBytes("Content-Type: $fileMimeType$lineEnd")
            outputStream.writeBytes("Content-Transfer-Encoding: binary$lineEnd")
            outputStream.writeBytes(lineEnd)
            var bytesAvailable = fileInputStream.available()
            var bufferSize = min(bytesAvailable, maxBufferSize)
            val buffer = byteArrayOf()
            var bytesRead = fileInputStream.readBytes()
            outputStream.write(bytesRead)
            //.read(buffer, 0, bufferSize)
/*
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize)
                bytesAvailable = fileInputStream.available()
                bufferSize = min(bytesAvailable, maxBufferSize)
                bytesRead = fileInputStream.read(buffer, 0, bufferSize)
            }
 */
            outputStream.writeBytes(lineEnd)
            val keys = params.keys.iterator()
            while (keys.hasNext()) {
                val key = keys.next()
                val value = params[key]
                outputStream.writeBytes(twoHyphens + boundary + lineEnd)
                outputStream.writeBytes("Content-Disposition: form-data; name=\"$key\"$lineEnd")
                outputStream.writeBytes("Content-Type: text/plain$lineEnd")
                outputStream.writeBytes(lineEnd)
                outputStream.writeBytes(value)
                outputStream.writeBytes(lineEnd)
            }
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd)
            if (200 != connection.responseCode) {
                throw Exception("Failed to upload code:" + connection.responseCode + " " + connection.responseMessage)
            }
            val inputStream = connection.inputStream
            val result = inputStream.bufferedReader().use {it.readText()}
            fileInputStream.close()
            inputStream.close()
            outputStream.flush()
            outputStream.close()
            return result
        } catch (e: Exception) {
            Log.e("MultipartRequest>>", e.toString())
            return "failed"
        }
    }


    private fun loadImages(){
        imgList.add(Image("apple", Uri.parse("")))
        imgList.add(Image("cat", Uri.parse("")))
        imgList.add(Image("dog", Uri.parse("")))
        imgList.add(Image("lake", Uri.parse("")))
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

// 시작, 전체 갤러리 가져오기


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







