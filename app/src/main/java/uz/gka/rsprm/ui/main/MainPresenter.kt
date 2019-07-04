package uz.gka.rsprm.ui.main

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import uz.gka.rsprm.api.ApiFactory
import uz.gka.rsprm.api.TruckModel
import uz.gka.rsprm.ui.base.BaseObserver
import uz.gka.rsprm.ui.singleTruck.TruckActivity

class MainPresenter(val view: MainView) : TrucksAdapter.ItemsClickListener {


    private val ADD_TRUCK_REQUEST_CODE: Int = 123
    private val UPDATE_TRUCK_REQUEST_CODE: Int = 124

    private val disposable = CompositeDisposable()


    init {
        getData()
    }

    fun getData() {
        view.showLoading()
        ApiFactory
            .getRsprmService()
            .getTrucks()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseObserver<Response<ArrayList<TruckModel>>>(view) {
                override fun onSuccess(response: Response<ArrayList<TruckModel>>) {
                    super.onSuccess(response)
                    if (response.isSuccessful) {

                        view.getAdapter().items =

                            response.body()!!.filter {
                            return@filter !TextUtils.isEmpty(it.id)
                                    && !TextUtils.isEmpty(it.nameTruck)
                                    && !TextUtils.isEmpty(it.price)
                                    && !TextUtils.isEmpty(it.comment)
                        }.toMutableList()
                    } else {
                        view.showError(response.message())
                    }
                }
            })
    }


    fun addNewTruck() {
        if (view.isLoading()) return
        view.showLoading()

        view.startTruckActivity(ADD_TRUCK_REQUEST_CODE)
    }

    private fun deleteItemById(truckId: String) {
        view.getAdapter().items = view.getAdapter().items.filter {
            return@filter it.id!! != truckId
        }.toMutableList()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        view.hideLoading()
        if (resultCode == Activity.RESULT_OK) {
            val truckModel = data?.getSerializableExtra(TruckActivity.truck_extra) as? TruckModel

            truckModel?.let {
                when (requestCode) {
                    ADD_TRUCK_REQUEST_CODE -> addTruckToItems(it)
                    UPDATE_TRUCK_REQUEST_CODE -> updateTruckInItems(it)
                }
            }


        }
    }


    private fun updateTruckInItems(truckModel: TruckModel) {
        for (item in view.getAdapter().items) {
            if (item.id == truckModel.id) {

                item.nameTruck = truckModel.nameTruck
                item.price = truckModel.price
                item.comment = truckModel.comment

                view.getAdapter().notifyDataSetChanged()

                return
            }
        }
    }

    private fun addTruckToItems(truckModel: TruckModel) {
        view.getAdapter().items.add(0, truckModel)
        view.getAdapter().notifyDataSetChanged()
    }

    override fun onItemClick(truckModel: TruckModel) {
        view.showLoading()
        view.startTruckActivity(UPDATE_TRUCK_REQUEST_CODE, truckModel)
    }

    override fun onItemDeleteClick(truckId: String) {
        view.showLoading()

        ApiFactory
            .getRsprmService()
            .deleteTruck(truckId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseObserver<Response<Any>>(view) {
                override fun onSuccess(response: Response<Any>) {
                    super.onSuccess(response)
                    if (response.isSuccessful) {
                        deleteItemById(truckId)
                    } else {
                        view.showError(response.message())
                    }
                }
            })
    }

    fun onDestroy() {
        disposable.dispose()
    }
}
