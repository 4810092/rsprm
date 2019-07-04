package uz.gka.rsprm.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_truck.view.*
import uz.gka.rsprm.R
import uz.gka.rsprm.api.TruckModel

class TrucksAdapter(var listener: ItemsClickListener) : RecyclerView.Adapter<TrucksAdapter.TrucksVH>() {

    var items = mutableListOf<TruckModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): TrucksVH =
        TrucksVH(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_truck,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(viewHolder: TrucksVH, position: Int) = viewHolder.setTruck(items[position], listener)


    class TrucksVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setTruck(truckModel: TruckModel, listener: ItemsClickListener) {
            itemView.apply {
                truckId.text = truckModel.id ?: ""
                truckName.text = truckModel.nameTruck ?: ""
                truckPrice.text = truckModel.price ?: ""
                truckComment.text = truckModel.comment ?: ""

                setOnClickListener { listener.onItemClick(truckModel) }
                delete.setOnClickListener { listener.onItemDeleteClick(truckModel.id!!) }
            }
        }


    }


    interface ItemsClickListener {
        fun onItemClick(truckModel: TruckModel)
        fun onItemDeleteClick(truckId: String)
    }
}