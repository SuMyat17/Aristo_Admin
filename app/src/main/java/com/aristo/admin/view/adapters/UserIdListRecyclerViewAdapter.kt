package com.aristo.admin.view.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.aristo.admin.databinding.UserIdListItemBinding
import com.aristo.admin.model.User
import com.aristo.admin.view.AddPointActivity
import java.util.Locale

class UserIdListRecyclerViewAdapter(
    val context: Context,
    private var originalList: ArrayList<User>
) : RecyclerView.Adapter<UserIdListRecyclerViewAdapter.UserIdListRecyclerViewHolder>(),
    Filterable {

    private var filteredList: ArrayList<User> = originalList

    class UserIdListRecyclerViewHolder(private val binding: UserIdListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User, context: Context,) {
            binding.tvId.text = "Id : ${user.userId}"

            itemView.setOnClickListener {
                val intent = Intent(context, AddPointActivity::class.java)
                intent.putExtra("user", user)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserIdListRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = UserIdListItemBinding.inflate(inflater, parent, false)
        return UserIdListRecyclerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: UserIdListRecyclerViewHolder, position: Int) {


        holder.bind(filteredList[position], context)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                val queryString = constraint?.toString()?.toLowerCase(Locale.getDefault())

                filteredList = if (queryString.isNullOrBlank()) {
                    originalList
                } else {
                    val tempList = ArrayList<User>()
                    for (item in originalList) {
                        if (item.userId.toString().toLowerCase(Locale.getDefault()).contains(queryString)) {
                            tempList.add(item)
                        }
                    }
                    tempList
                }

                filterResults.values = filteredList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as ArrayList<User>
                notifyDataSetChanged()
            }
        }
    }


    fun updateData(newData: ArrayList<User>) {
        originalList.clear()
        originalList.addAll(newData)

        notifyDataSetChanged()
    }
}