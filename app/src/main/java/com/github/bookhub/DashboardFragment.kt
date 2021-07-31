package com.github.bookhub

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import androidx.core.app.ActivityCompat as ActivityCompat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [dashboard.newInstance] factory method to
 * create an instance of this fragment.
 */
class dashboard : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var recyclerView: RecyclerView? = null
    private var items: ArrayList<Books> = arrayListOf()
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var recyclerAdapter: DashboardRecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        layoutManager = LinearLayoutManager(activity)
        recyclerView = view.findViewById(R.id.fragDashdRecyclerView)
        val alertDialog = AlertDialog.Builder(activity as Context)
        alertDialog.setTitle("Error")
        alertDialog.setMessage("Internet not found")
        alertDialog.setPositiveButton("Open settings") { _, _ ->
            val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            startActivity(settingsIntent)
            activity?.finish()
        }
        alertDialog.setNegativeButton("Exit") { _, _ ->
            ActivityCompat.finishAffinity(activity as Activity)
        }
        alertDialog.create()
        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val queue = Volley.newRequestQueue(activity as Context)
            val url = "http://13.235.250.119/v1/book/fetch_books/"
            val jsonObjectRequest:JsonObjectRequest? = try {
                object : JsonObjectRequest(Request.Method.GET, url, null,
                    Response.Listener {
                        val success = it.getBoolean("success")
                        if (success) {
                            val data = it.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val joBook = data.getJSONObject(i) // abbr. jsonObjectBook
                                items.add(
                                    Books(
                                        joBook.getString("id"),
                                        joBook.getString("book_id"),
                                        joBook.getString("name"),
                                        joBook.getString("author"),
                                        joBook.getString("rating"),
                                        joBook.getString("price"),
                                        joBook.getString("image")
                                    )
                                )
                            }
                            recyclerAdapter = DashboardRecyclerView(activity as Context, items)
    //        Log.d("child view", "onCreateView : ${recyclerView?.toString()}")
                            recyclerView?.adapter = recyclerAdapter
                            recyclerView?.layoutManager = layoutManager
                            recyclerView?.addItemDecoration(
                                DividerItemDecoration(
                                    recyclerView?.context,
                                    (layoutManager as LinearLayoutManager).orientation
                                )
                            )
                            Log.d("Response", "Success parsing")
                        } else {
                            Toast.makeText(
                                activity as Context,
                                "something went wrong",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    },
                    Response.ErrorListener {
                        Toast.makeText(activity as Context, "something went wrong", Toast.LENGTH_LONG)
                            .show()
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val header = HashMap<String, String>()
                        header["Content-type"] = "application/json"
                        header["token"] = "5ac85abecd8072"
                        return header
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(activity as Context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                return null
            }
            jsonObjectRequest?.let {
                queue.add(it)
            }
        } else {
            alertDialog.show()
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment dashboard.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            dashboard().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}