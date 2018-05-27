package xyz.twbkg.myviewmodel.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_view.view.*
import xyz.twbkg.myviewmodel.R
import xyz.twbkg.myviewmodel.persistence.User

class UserAdapter(val listener: UserAdapterCallback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface UserAdapterCallback {
        fun onViewUser(user: User)
        fun onUpdateUser(user: User)
    }

    private val TYPE_EMPTY = R.layout.empty_view
    private val TYPE_ITEM = R.layout.item_view

    private val items = arrayListOf<User>()


    fun addItems(items: List<User>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return if (viewType == TYPE_EMPTY) {
            EmptyViewHolder(view)
        } else {
            ItemViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return if (items.size == 0) 1
        else items.size
    }

    override fun getItemViewType(position: Int): Int {
        return try {
            if (items[position] != null) {
                TYPE_ITEM
            } else {
                TYPE_EMPTY
            }
        } catch (e: Exception) {
            TYPE_EMPTY
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.onBind(items[position])
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        init {
            itemView.edit_button.setOnClickListener(this)
            itemView.setOnClickListener(this)
        }

        fun onBind(user: User) {
            itemView.item_tv.text = user.name
        }

        override fun onClick(v: View) {
            if (v.id == itemView.edit_button.id) {
                listener.onUpdateUser(items[adapterPosition])
            } else {
                listener.onViewUser(items[adapterPosition])
            }

        }
    }

    inner class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}