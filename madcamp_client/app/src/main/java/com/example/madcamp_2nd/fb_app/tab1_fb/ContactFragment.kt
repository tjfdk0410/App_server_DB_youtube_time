package com.example.madcamp_2nd.fb_app.tab1_fb

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.contacts.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_2nd.R
import com.facebook.AccessToken

import io.github.rybalkinsd.kohttp.dsl.httpDelete
import io.github.rybalkinsd.kohttp.dsl.httpPost
import io.github.rybalkinsd.kohttp.ext.url
import java.net.URL


class ContactFragment: Fragment() {

    var itemList = arrayListOf<Item>()

    var FACE_BOOK_ID: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loadMyContacts()

        val accessToken = AccessToken.getCurrentAccessToken()
        FACE_BOOK_ID = accessToken.userId

//        //버튼 클릭시 리스너로 간다.
//        contact_sync_button.setOnClickListener{
//            JSONTask().execute("http://192.249.19.254:6080/") //AsyncTask 시작시킴
//
//        }
//        contact_upload_button.setOnClickListener{
//            JSONTask().execute("http://192.249.19.254:6080/") //AsyncTask 시작시킴
//        }

        return inflater.inflate(R.layout.contacts, container, false)
    }


//    inner class JSONTask : AsyncTask<String?, String?, String?>() {
//        override fun doInBackground(vararg urls: String?): String? {
//            try {
//                var post = httpPost { url("http://192.249.19.254:6080/api/contacts")
//                    body {
//                        form("user_id=123456789&" + "phNum=010-2222-6666&" + "name=newPeople")
//                    }
//                }
//                Log.i("post>>>>>>>>>>>>>>>>>>", post.message())
//                ​
//                var get =
//                    URL("http://192.249.19.254:6080/api/contacts/user_id/dddafd@d34d.com").readText() // 로그인한 유저가 받은 아이디로 찾기 //not found 일 때 처리
//                Log.i(
//                    "get>>>>>>>>>>>>>>>>",
//                    get
//                ) //형태>> [{"_id":"5e11cddde1fc032f3ba8e4c3","phNum":"010-121324-1124","name":"dafoudfasfi"}]
//                //parsing code 필요
//                ​
//                ​
//                var delete = httpDelete {
//                    url("http://192.249.19.254:6080/api/contacts/5e11cddde1fc032f3ba8e4c3")
//                }
//                Log.i("delete>>>>>>>>>>>>>>>>", delete.message())
//                ​
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//            return null
//        }
//    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // set recycler view
        val mAdapter = MainRvAdapter(requireContext(), itemList)
        recycler_view.adapter = mAdapter

        // set contact add button listener
        contact_add_button.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val dialogView = layoutInflater.inflate(R.layout.contact_popup, null)
            val user_name = dialogView.findViewById<EditText>(R.id.popup_user_name)
            val phone_number = dialogView.findViewById<EditText>(R.id.popup_phone_number)

            builder.setView(dialogView)
                .setPositiveButton("확인") {
                    dialogInterface, i ->
                        val userName = user_name.text.toString()
                        val phoneNumber = phone_number.text.toString()
                        itemList.add(Item(userName, phoneNumber))
                }
                .setNegativeButton("취소") {
                    dialogInterface, i ->

                }
                .show()

        }

        val layoutManager = LinearLayoutManager(requireContext())
        recycler_view.layoutManager = layoutManager
        recycler_view.setHasFixedSize(true)
    }

    /**
     * loadMyContacts()
     *      load contacts from the phone
     */
    private fun loadMyContacts() {
        // set pointer for reading contacts
        val contactsPointer = requireContext().contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                                                          null,
                                                           null,
                                                       null,
                                                          ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" DESC")
        // if some errors occur (manage null pointer exception)
        if (contactsPointer == null) {
            System.exit(0); // some errors
        } else {    // no errors below
            // if there's any contacts
            if (contactsPointer.count > 0) {
                // till the end
                while (contactsPointer.moveToNext()) {
                    val id = contactsPointer.getString(contactsPointer.getColumnIndex(
                        ContactsContract.Contacts._ID))
                    val name = contactsPointer.getString(contactsPointer.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME))
                    val phoneNumber = contactsPointer.getString(contactsPointer.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)).toInt()
                    // if he has phone number,
                    if (phoneNumber > 0) {
                        val phoneNumberPointer = requireContext().contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(id), null)

                        if (phoneNumberPointer == null) {
                            System.exit(0); // some errors
                        } else {
                            if (phoneNumberPointer.count > 0) {
                                while (phoneNumberPointer.moveToNext()) {
                                    val phoneNumValue = phoneNumberPointer.getString(phoneNumberPointer.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER))
                                    val item: Item = Item(name, phoneNumValue)
                                    itemList.add(item)
//                                    Toast.makeText(requireContext(), "$id $name $phoneNumber", Toast.LENGTH_LONG).show()
                                }
                            }
                            phoneNumberPointer.close()
                        }
                    }
//                    Toast.makeText(requireContext(), "$id $name $phoneNumber", Toast.LENGTH_LONG).show()
                }
//                Toast.makeText(requireContext(), "read all the contacts", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "No Contacts in the phone", Toast.LENGTH_LONG).show()
            }
            contactsPointer.close()
        }
    }

    private fun addContact() {

    }
}
