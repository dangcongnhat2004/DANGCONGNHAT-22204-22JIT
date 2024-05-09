package com.example.a22it204_dangcongnhat_22jit
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.Filter
import android.widget.Filterable
import java.util.Locale
class CustomAdapter(private var originalDataList: List<Student>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>(),
    Filterable {

    private var filteredData: List<Student> = originalDataList

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvAddress: TextView = itemView.findViewById(R.id.tv_address)
        val tvDayOfBirth: TextView = itemView.findViewById(R.id.tv_dayOfBirth)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_user, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = originalDataList[position]
        holder.tvName.text = student.name
        holder.tvAddress.text = student.address
        holder.tvDayOfBirth.text = student.dayOfBirth
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = mutableListOf<Student>()

                if (constraint.isNullOrEmpty()) {
                    filteredList.addAll(originalDataList)
                } else {
                    val filterPattern = constraint.toString().toLowerCase(Locale.getDefault()).trim()

                    for (student in originalDataList) {
                        if (student.name.toLowerCase(Locale.getDefault()).contains(filterPattern)) {
                            filteredList.add(student)
                        }
                    }
                }

                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredData = results?.values as List<Student>
                notifyDataSetChanged()
            }
        }
    }

    fun updateOriginalDataList(newDataList: List<Student>) {
        originalDataList = newDataList
        notifyDataSetChanged()
    }
}

