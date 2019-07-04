package uz.gka.rsprm.ui.base

interface BaseView {
    fun showLoading()
    fun hideLoading()
    fun showError(message: String)
    fun isLoading(): Boolean
}