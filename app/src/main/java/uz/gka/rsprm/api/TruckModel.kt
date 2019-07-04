package uz.gka.rsprm.api

import java.io.Serializable

data class TruckModel(var id: String?, var nameTruck: String?, var price: String?, var comment: String?) :
    Serializable {
    constructor() : this(null, null, null, null)
}