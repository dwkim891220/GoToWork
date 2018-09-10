package android.dwkim.pr.gotothesugarhill

import android.arch.persistence.room.Room
import android.content.Intent
import android.dwkim.pr.gotothesugarhill.db.Arrive
import android.dwkim.pr.gotothesugarhill.db.ArrivesDB
import android.dwkim.pr.gotothesugarhill.util.AndroidExtensionsViewHolder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_arrive.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(){
    private val mAdapter = RecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startService(Intent(this, WifiCatchService::class.java))

        setLayouts()
    }

    private fun setLayouts(){
        rv_main_arriveList.run {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mAdapter
        }
    }

    override fun onResume() {
        super.onResume()

        getArrives()
    }

    private fun getArrives(){
        val dao =
                Room.databaseBuilder(
                        applicationContext,
                        ArrivesDB::class.java,
                        "ArrivesDB").build()

        dao.arriveDao().loadAll().observeForever{ list ->
            list?.run {
                mAdapter.addItems(this)
            }
        }
    }

    inner class RecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private var mItems : List<Arrive> = emptyList()

        fun addItems(list: List<Arrive>?){
            list?.run{
                mItems = this
                notifyDataSetChanged()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ViewHolder(
                    LayoutInflater.from(parent.context)
                            .inflate(R.layout.item_arrive, parent, false))
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as? ViewHolder)?.run {
                val item = mItems[position]
                this.tv_time.text = SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.KOREA).format(item.date)
                this.tv_wifiName.text = item.wifiName
            }
        }

        override fun getItemCount(): Int = mItems.size

        inner class ViewHolder(view: View) : AndroidExtensionsViewHolder(view)
    }
}
