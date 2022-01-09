package com.example.githubbrowser

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
import org.json.JSONException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [IssuesFragments.newInstance] factory method to
 * create an instance of this fragment.
 */
class IssuesFragments : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var imageList:ArrayList<String> = ArrayList();
    private var titleList:ArrayList<String> = ArrayList();
    private var issueCreatorList:ArrayList<String> = ArrayList();

    private lateinit var adapter:IssuesAdapter
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
        val view= inflater.inflate(R.layout.fragment_issues_fragments, container, false)
        val recyclerView =view.findViewById<RecyclerView>(R.id.recycleView)
        getIssueList()
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = IssuesAdapter(titleList,imageList ,issueCreatorList,requireContext())
        recyclerView.adapter=adapter


        return view
    }
    private fun getIssueList(){
        val requestQueue = Volley.newRequestQueue(context)
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("sharedPrefFile",
            Context.MODE_PRIVATE)
        val repoData = sharedPreferences.getString("currentRepo", "")
        val strs = repoData?.split(":")?.toTypedArray()
        val owner= strs?.get(0)
        val repoName=strs?.get(1)
        val syntax1="https://api.github.com/repos"
        val syntax2="/issues?state=open"
        val url= "$syntax1/$owner/$repoName$syntax2"
        Log.i("maryritiki3",url)
        val request = JsonArrayRequest(Request.Method.GET, url, null, {
                response ->try {
            for (i in 0 until response.length())
            {
                val issue = response.getJSONObject(i)
                val title=issue.getString("title")
                val user=issue.getJSONObject("user")
                val avatar_url=user.getString("avatar_url")
                val creator=user.getString("login")
                issueCreatorList.add(creator)
                titleList.add(title)
                imageList.add(avatar_url)
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
         * @return A new instance of fragment IssuesFragments.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            IssuesFragments().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}