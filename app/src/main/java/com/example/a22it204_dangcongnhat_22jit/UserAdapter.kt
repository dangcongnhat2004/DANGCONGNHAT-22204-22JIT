package com.example.a22it204_dangcongnhat_22jit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
class UserAdapter(var listUser: List<Student>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvId: TextView = itemView.findViewById(R.id.tv_id)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvImage: ImageView = itemView.findViewById(R.id.tv_image)
        val tvAddress: TextView = itemView.findViewById(R.id.tv_address)
        val tvDayOfBirth: TextView = itemView.findViewById(R.id.tv_dayOfBirth)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user: Student = listUser[position]
        holder.tvId.text = user.id.toString()
        holder.tvName.text = user.name
        // Set the image resource if it's a valid drawable resource ID
        if (isValidDrawableResource(holder.itemView.context, user.image.toInt())) {
            holder.tvImage.setImageResource(user.image.toInt())
        } else {
            // Handle the case where the image resource is not valid
            // For example, set a default image
            holder.tvImage.setImageResource(R.drawable.user)
        }
        holder.tvAddress.text = user.address
        holder.tvDayOfBirth.text = user.dayOfBirth
    }

    // Function to check if the drawable resource ID is valid
    private fun isValidDrawableResource(context: Context, resourceId: Int): Boolean {
        return try {
            ContextCompat.getDrawable(context, resourceId)
            true
        } catch (e: Exception) {
            false
        }
    }
    fun filterList(filteredList: List<Student>) {
        listUser = filteredList
        notifyDataSetChanged()
    }
    fun updateList(newList: List<Student>) {
        listUser = newList
        notifyDataSetChanged()
    }
}
