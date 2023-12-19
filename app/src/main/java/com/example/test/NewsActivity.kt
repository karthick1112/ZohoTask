package com.example.test

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.Adapter.NewsListAdapter
import com.example.test.Interface.OnLoadMoreListener
import com.example.test.ModelClass.GetNewsListResponseBody
import com.example.test.NetworkUtil.NetworkUtil
import com.example.test.Retrofit.ApiClient
import com.example.test.Room.OfflineRoomDatabase
import com.example.test.Room.TblNewsList
import com.example.test.databinding.NewsLayoutBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import rx.Observer
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.Locale

class NewsActivity : HomeScreen(), OnLoadMoreListener {
    lateinit var mactivity: AppCompatActivity
    private var getNewsListArray: ArrayList<GetNewsListResponseBody.Result> = ArrayList()
    lateinit var newsListAdapter: NewsListAdapter
    lateinit var activityNewsactivity: NewsLayoutBinding
    lateinit var bck: LinearLayout

    lateinit var rvNewsList: RecyclerView
    var searchView: SearchView? = null
    var idPBLoading: ProgressBar? = null
    var mcontext: Context? = null
    var subscriptions: Subscription? = null
    var paged: Int = 0
    var fbScrollTop: FloatingActionButton? = null
    private var offlineRoomDatabase: OfflineRoomDatabase? = null
    var tblNewsList: List<TblNewsList> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityNewsactivity = NewsLayoutBinding.inflate(layoutInflater)
        setView(activityNewsactivity.getRoot(), "News")
        mcontext = this
        mactivity = mcontext as NewsActivity

        //id
        rvNewsList = findViewById(R.id.rvNewsList)
        bck = findViewById(R.id.bck)
        idPBLoading = findViewById(R.id.idPBLoading)
        fbScrollTop = findViewById(R.id.fbScrollTop)
        searchView = findViewById(R.id.searchview)

        getNewsListArray = ArrayList()

        offlineRoomDatabase =
            OfflineRoomDatabase.getDataBaseInstance(this@NewsActivity)
        newsListAdapter = NewsListAdapter(
            this@NewsActivity,
            tblNewsList, activityNewsactivity.nestedSroll, this@NewsActivity)


        rvNewsList.layoutManager = LinearLayoutManager(this@NewsActivity)
        rvNewsList.adapter = newsListAdapter

        if (NetworkUtil.checkActiveInternetConnection(this@NewsActivity)) {
            getNewsListAPIFromServer(paged)
        }else{
            getListFromoffline()
        }

        fbScrollTop!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                activityNewsactivity.nestedSroll.smoothScrollTo(0, 0)
            }

        })

        activityNewsactivity.nestedSroll.setOnScrollChangeListener(object :
            NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if (scrollY >= v.getChildAt(v.getChildCount() - 1)
                            .getMeasuredHeight() - v.getMeasuredHeight() &&
                        scrollY > oldScrollY
                    ) {
                           onLoadMore()
                    }
                }
            }
        })

        searchView!!.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                fileterList(newText)
                return true
            }
        })
        bck.setOnClickListener {
            onBackPressed()
        }
    }

    private fun fileterList(query: String?) {
        if (query != null){
            var filterListData = ArrayList<TblNewsList>()
            for (i in tblNewsList){
                if(i.title!!.toLowerCase(Locale.ROOT).contains(query)){
                    filterListData.add(i)
                }
            }
            if(filterListData.isEmpty()){
//                Toast.makeText(this,"No Data Found",Toast.LENGTH_SHORT).show()
            }else{
                newsListAdapter.notifyDataSetChanged(filterListData)
            }
        }
    }

    private fun getNewsListAPIFromServer(paged: Int) {
        if (NetworkUtil.checkActiveInternetConnection(this@NewsActivity)) {
            idPBLoading!!.visibility = View.VISIBLE
            subscriptions = ApiClient.apiService.getNewsLists(paged.toString())
                .observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribe(object : Observer<GetNewsListResponseBody> {
                    override fun onCompleted() {
                        //TODO:
                    }

                    override fun onError(e: Throwable?) {
                        Log.e("Profile Detail", e.toString())
                    }

                    override fun onNext(response: GetNewsListResponseBody) {
                        idPBLoading!!.visibility = View.GONE
                        if (response != null && response.results != null && response.results!!.size > 0) {

                            getNewsListArray!!.addAll(response.results!!)
                            for (i in 0..response!!.results!!.size-1){
                                var tblListbyId : TblNewsList = offlineRoomDatabase!!.iTblNewsList()!!.getNewsListbyId(response!!.results!!.get(i).id!!)
                                if(tblListbyId == null) {
                                    var tbllist: TblNewsList = TblNewsList()
                                    tbllist.newsid = response!!.results!!.get(i).id!!
                                    tbllist.title = response!!.results!!.get(i).title!!
                                    tbllist.url = response!!.results!!.get(i).url!!
                                    tbllist.image_url = response!!.results!!.get(i).imageUrl!!
                                    tbllist.summary = response!!.results!!.get(i).summary!!
                                    offlineRoomDatabase!!.iTblNewsList()!!.insertNews(tbllist)
                                }
                            }
                            getListFromoffline()

                        }
                    }

                })
        } else {
                Toast.makeText(this,getString(R.string.check_internet),Toast.LENGTH_SHORT).show()
        }
    }

    private fun getListFromoffline() {
         tblNewsList = offlineRoomDatabase!!.iTblNewsList()!!.getAllNewsList()
        if(tblNewsList != null && tblNewsList.size > 0){
            newsListAdapter.notifyDataSetChanged(tblNewsList)
            if (paged > 2) {
                fbScrollTop!!.show()
            }
        }
    }

    override fun onLoadMore() {
        paged++
        getNewsListAPIFromServer(paged)
    }
}