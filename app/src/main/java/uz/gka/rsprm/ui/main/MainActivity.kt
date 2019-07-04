package uz.gka.rsprm.ui.main

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import uz.gka.rsprm.R
import uz.gka.rsprm.api.TruckModel
import uz.gka.rsprm.ui.singleTruck.TruckActivity

class MainActivity : AppCompatActivity(), MainView {


    override fun getAdapter(): TrucksAdapter {
        return trucksAdapter
    }

    lateinit var trucksAdapter: TrucksAdapter
    lateinit var llm: LinearLayoutManager


    lateinit var presenter: MainPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this)

        pb.setOnClickListener({})
        hideLoading()

        llm = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerList.layoutManager = llm

        trucksAdapter = TrucksAdapter(presenter)
        recyclerList.adapter = trucksAdapter

        srl.setOnRefreshListener { presenter.getData() }

    }



    override fun showLoading() {
        srl.isRefreshing = false
        pb.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        pb.visibility = View.GONE
        srl.isRefreshing = false

    }

    override fun isLoading(): Boolean = pb.visibility == View.VISIBLE

    override fun showError(message: String) {
        AlertDialog.Builder(this).setTitle(R.string.error).setMessage(message)
            .setNeutralButton(android.R.string.ok) { dialog, _ -> dialog?.dismiss() }.show()
    }

    override fun startTruckActivity(requestCode: Int, truckModel: TruckModel?) {
        TruckActivity.startForResult(this, requestCode, truckModel)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                presenter.addNewTruck()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.action_add, menu)
        return true
    }


    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.onActivityResult(requestCode, resultCode, data)
    }


}
