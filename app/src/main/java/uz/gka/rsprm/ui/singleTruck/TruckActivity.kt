package uz.gka.rsprm.ui.singleTruck

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.activity_truck.*
import uz.gka.rsprm.R
import uz.gka.rsprm.api.TruckModel


class TruckActivity : AppCompatActivity(), TruckView {


    companion object {
        val truck_extra = "TRUCK_EXTRA"

        fun startForResult(activity: AppCompatActivity, requestCode: Int, truckModel: TruckModel? = null) {
            val intent = Intent(activity, TruckActivity::class.java)
            intent.putExtra(truck_extra, truckModel)
            activity.startActivityForResult(intent, requestCode)
        }
    }

    lateinit var presenter: TruckPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_truck)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        presenter = TruckPresenter(this)

        pb.setOnClickListener({})

        presenter.setTruckModel(intent.getSerializableExtra(truck_extra) as? TruckModel)


    }


    override fun getTruckName(): String = truckName.text.toString()

    override fun getTruckPrice(): String = truckPrice.text.toString()

    override fun getTruckComment(): String = truckComment.text.toString()

    override fun setTruckNameError(errorId: Int) {
        truckName.error = getString(errorId)
    }

    override fun setTruckPriceError(errorId: Int) {
        truckPrice.error = getString(errorId)
    }

    override fun setTruckCommentError(errorId: Int) {
        truckComment.error = getString(errorId)
    }

    override fun setTruckData(truckModel: TruckModel) {

        truckId.text = truckModel.id ?: ""
        truckName.setText(truckModel.nameTruck ?: "")
        truckPrice.setText(truckModel.price ?: "")
        truckComment.setText(truckModel.comment ?: "")

    }

    override fun onBackPressed() {
        if (isLoading())
            return
        super.onBackPressed()
    }

    override fun returnResult(truckModel: TruckModel) {
        val intent = Intent()
        intent.putExtra(truck_extra, truckModel)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun isLoading(): Boolean = pb.visibility == View.VISIBLE

    override fun showError(message: String) {
        AlertDialog.Builder(this).setTitle(R.string.error).setMessage(message)
            .setNeutralButton(android.R.string.ok) { dialog, _ -> dialog?.dismiss() }
            .show()
    }

    override fun showError(message: Int) {
        showError(getString(message))
    }

    override fun hideLoading() {
        pb.visibility = View.GONE

    }

    override fun showLoading() {
        pb.visibility = View.VISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                presenter.saveTruck()
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus ?: View(this)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.action_save, menu)
        return true
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}