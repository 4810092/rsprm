package uz.gka.rsprm.ui.singleTruck

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import uz.gka.rsprm.R
import uz.gka.rsprm.api.ApiFactory
import uz.gka.rsprm.api.TruckModel
import uz.gka.rsprm.ui.base.BaseObserver

class TruckPresenter(val view: TruckView) {

    lateinit var truck: TruckModel

    fun setTruckModel(truckModel: TruckModel?) {
        truck = truckModel ?: TruckModel()
        view.setTruckData(truck)
    }

    private val disposable = CompositeDisposable()


    fun saveTruck() {
        if (view.isLoading()) return

        view.hideKeyboard()

        if (dataIsNotComplete()) return

        view.showLoading()

        truck.apply {
            nameTruck = view.getTruckName()
            price = view.getTruckPrice()
            comment = view.getTruckComment()
        }

        val request = if (truck.id != null)
            ApiFactory.getRsprmService().updateTruck(truck.id!!, truck)
        else {
            ApiFactory.getRsprmService().addTruck(truck)
        }

        request.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

            .subscribe(object : BaseObserver<Response<TruckModel>>(view) {
                override fun onSuccess(response: Response<TruckModel>) {
                    super.onSuccess(response)

                    if (response.isSuccessful) {
                        editIsSuccessful(response.body())
                    } else {
                        view.showError(response.message())
                    }
                }

            })
    }

    private fun dataIsNotComplete(): Boolean {
        if (view.getTruckName().isEmpty())
            view.setTruckNameError(R.string.error_empty)

        if (view.getTruckPrice().isEmpty())
            view.setTruckPriceError(R.string.error_empty)

        if (view.getTruckComment().isEmpty())
            view.setTruckCommentError(R.string.error_empty)

        return view.getTruckName().isEmpty() || view.getTruckPrice().isEmpty() || view.getTruckComment().isEmpty()
    }

    private fun editIsSuccessful(truckModel: TruckModel?) {
        if (truckModel != null) {
            view.returnResult(truckModel)
        } else {
            view.showError(R.string.error_response_is_empty)
        }
    }

    fun onDestroy() {
        disposable.dispose()
    }

}