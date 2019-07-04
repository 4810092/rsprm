package uz.gka.rsprm.ui.base

import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

open class BaseObserver<T>(var baseView: BaseView) : SingleObserver<T> {
    override fun onSuccess(response: T) {
        baseView.hideLoading()

    }

    override fun onSubscribe(d: Disposable) {

    }

    override fun onError(e: Throwable) {
        baseView.hideLoading()
        baseView.showError(e.localizedMessage)
    }

}