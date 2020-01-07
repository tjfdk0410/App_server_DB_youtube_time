package com.example.madcamp_2nd.fb_app.tab1_fb

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.contacts.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_2nd.fb_app.tab2_fb.GalleryRVAdapter
import com.facebook.AccessToken
import com.google.gson.JsonArray

import io.github.rybalkinsd.kohttp.dsl.httpDelete
import io.github.rybalkinsd.kohttp.dsl.httpPost
import io.github.rybalkinsd.kohttp.ext.url
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL


class ContactFragment: Fragment() {

    var itemList = arrayListOf<Item>()

    var FACE_BOOK_ID: String? = null

    lateinit var mAdapter : MainRvAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(com.example.madcamp_2nd.R.layout.contacts, container, false)
        //loadMyContacts()

        val accessToken = AccessToken.getCurrentAccessToken()
        FACE_BOOK_ID = accessToken.userId

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // set recycler view
        mAdapter = MainRvAdapter(requireContext(), itemList)
        recycler_view.adapter = mAdapter

        val sync_button :Button = view.findViewById(com.example.madcamp_2nd.R.id.contact_sync_button)
        sync_button.setOnClickListener{
            JSONTaskGet().execute("http://192.249.19.254:8080/") //AsyncTask 시작시킴
            for (i in 0..100000000){
            }
            mAdapter.notifyDataSetChanged()
            recycler_view.adapter = mAdapter
        }

        // set contact add button listener
        contact_add_button.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val dialogView = layoutInflater.inflate(com.example.madcamp_2nd.R.layout.contact_popup, null)
            val user_name = dialogView.findViewById<EditText>(com.example.madcamp_2nd.R.id.popup_user_name)
            val phone_number = dialogView.findViewById<EditText>(com.example.madcamp_2nd.R.id.popup_phone_number)

            builder.setView(dialogView)
                .setPositiveButton("확인") {
                    dialogInterface, i ->
                    val userName = user_name.text.toString()
                    val phoneNumber = phone_number.text.toString()

                    JSONTaskUpLoad(userName, phoneNumber).execute("http://192.249.19.254:8080/")

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

    inner class JSONTaskGet : AsyncTask<String?, String?, String?>() {
        override fun doInBackground(vararg urls: String?): String? {
            try {
                var get = URL("http://192.249.19.254:8080/contacts/$FACE_BOOK_ID").readText()

                val conList: JSONArray = JSONArray(get)
                for (i in 0..conList.length()) {
                    val userName: String = conList.getJSONObject(i).getString("name")
                    val phoneNumber: String = conList.getJSONObject(i).getString("number")
                    itemList.add(Item(userName, phoneNumber))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }

    inner class JSONTaskUpLoad(NN:String, PN:String) : AsyncTask<String?, String?, String?>() {
        var NewName = NN
        var PhNum = PN
        override fun doInBackground(vararg urls: String?): String? {
            try {
                val cObject: JSONObject = JSONObject()
                cObject.put("name", "$NewName")
                cObject.put("number", "$PhNum")

                var post = httpPost { url("http://192.249.19.254:8080/contacts/$FACE_BOOK_ID")

                    body {
                        form {
                            "UID" to FACE_BOOK_ID
                            "contacts" to cObject
                        }
                    }
                }
                Log.i("post>>>>>>>>>>>>>>>>>>", post.message())

            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
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
