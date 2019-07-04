package uz.gka.rsprm.ui.main

import uz.gka.rsprm.api.TruckModel
import uz.gka.rsprm.ui.base.BaseView

interface MainView : BaseView {
    fun getAdapter(): TrucksAdapter
    fun startTruckActivity(requestCode: Int, truckModel: TruckModel? = null)
}

