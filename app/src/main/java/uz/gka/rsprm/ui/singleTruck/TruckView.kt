package uz.gka.rsprm.ui.singleTruck

import uz.gka.rsprm.api.TruckModel
import uz.gka.rsprm.ui.base.BaseView

interface TruckView : BaseView {
    fun setTruckData(truckModel: TruckModel)
    fun hideKeyboard()
    fun showError(message: Int)
    fun returnResult(truckModel: TruckModel)
    fun getTruckName(): String
    fun getTruckPrice(): String
    fun getTruckComment(): String
    fun setTruckNameError(errorId: Int)
    fun setTruckPriceError(errorId: Int)
    fun setTruckCommentError(errorId: Int)

}