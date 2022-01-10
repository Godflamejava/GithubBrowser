package com.example.githubbrowser.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.githubbrowser.Adapter.BranchAdapter
import com.example.githubbrowser.R
import org.json.JSONException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BranchesFragments.newInstance] factory method to
 * create an instance of this fragment.
 */
class BranchesFragments : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var list:ArrayList<String> = ArrayList();
    private lateinit var adapter: BranchAdapter

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
        val view= inflater.inflate(R.layout.fragment_branches_fragments, container, false)
        val recyclerView =view.findViewById<RecyclerView>(R.id.recycleView)
        getBranchList()
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = BranchAdapter(list, requireContext())
        recyclerView.adapter=adapter



      return view
    }

    private fun getBranchList(){

            val requestQueue = Volley.newRequestQueue(context)
            val syntax1="https://api.github.com/repos"
            val syntax2="/branches"

        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("sharedPrefFile",
            Context.MODE_PRIVATE)
        val repoData = sharedPreferences.getString("currentRepo", "")
        val strs = repoData?.split(":")?.toTypedArray()
        val owner= strs?.get(0)
        val repoName=strs?.get(1)

            val url= "$syntax1/$owner/$repoName$syntax2"
            Log.i("maryritiki",url)


            val request = JsonArrayRequest(Request.Method.GET, url, null, {
                    response ->try {
                for (i in 0 until response.length())
                {
                    val branch = response.getJSONObject(i)
                    val name = branch.getString("name")
                    Log.i("maryritiki",name)

                    list.add(name)
                }
                adapter.notifyDataSetChanged()

            }
            catch (e: JSONException) {
                e.printStackTrace()
            }
            }, { error -> error.printStackTrace() })
            requestQueue.add(request)


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BranchesFragments.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BranchesFragments().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

