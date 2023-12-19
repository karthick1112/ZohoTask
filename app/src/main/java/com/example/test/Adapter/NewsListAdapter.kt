package com.example.test.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.example.test.Interface.OnLoadMoreListener
import com.example.test.ModelClass.GetNewsListResponseBody
import com.example.test.R
import com.example.test.Room.TblNewsList
import com.example.test.ViewMoreWebView
import com.squareup.picasso.Picasso
import java.util.Locale

class NewsListAdapter(
    private val context: Context,
    private var results: List<TblNewsList>,
    nestedSroll: NestedScrollView,
    onLoadMoreListener: OnLoadMoreListener,
) : RecyclerView.Adapter<NewsListAdapter.MyViewHolder?>() {
    var searchListDataall: List<TblNewsList> = ArrayList()
    init {
        searchListDataall = results
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewsListAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_list_item, parent, false)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position != null) {

            holder.tvTitle!!.text = results.get(position).title
            if(results.get(position).summary!!.length > 100){
                holder.tvSummary!!.text = results.get(position).summary!!.substring(0,100)
            }else{
                holder.tvSummary!!.text = results.get(position).summary
            }

            if(results.get(position).image_url != null && !results.get(position).image_url.equals("")){
                Picasso.get()
                    .load(results.get(position).image_url)
                    .error(context.resources.getDrawable(R.drawable.location_maps))
                    .fit()
                    .into(holder.thumImg);
            }

            Log.e("URLADAPTER", results.get(position).url.toString())
            holder.tvViewMore!!.setOnClickListener(object :View.OnClickListener{
                override fun onClick(p0: View?) {
                    if(holder.tvViewMore!!.text.equals(context.getString(R.string.view_more))){
                        holder.tvViewMore!!.text = context.getString(R.string.view_less)
                        holder.tvSummary!!.text = results.get(position).summary
                    }else{
                        holder.tvViewMore!!.text = context.getString(R.string.view_more)
                        holder.tvSummary!!.text = results.get(position).summary!!.substring(0,100)
                    }
                }
            })
            holder.cardView!!.setOnClickListener(object :View.OnClickListener{
                override fun onClick(p0: View?) {
                    val intent = Intent(context, ViewMoreWebView::class.java).putExtra("url", results.get(position).url.toString() )
                    context. startActivity(intent);
                }
            })

        }
    }

    override fun getItemCount(): Int =  results.size
    fun notifyDataSetChanged(newsListArray: List<TblNewsList>) {
            this.results = newsListArray
            notifyDataSetChanged()
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvTitle: TextView?=null
        var tvSummary: TextView?=null
        var tvViewMore: TextView?=null
        var cardView: CardView?=null
        var thumImg: ImageView?=null

        init {
            tvTitle = itemView.findViewById(R.id.tvTitle)
            tvSummary = itemView.findViewById(R.id.tvSummary)
            tvViewMore = itemView.findViewById(R.id.tvViewMore)
            cardView = itemView.findViewById(R.id.cardView)
            thumImg = itemView.findViewById(R.id.thum_img)

        }
    }
}