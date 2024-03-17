package com.example.whatsup.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsup.R
import com.example.whatsup.databinding.ReceiverItemLayoutBinding
import com.example.whatsup.databinding.SentItemLayoutBinding
import com.example.whatsup.messageActivity
import com.example.whatsup.model.MessageModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth

class messageAdapter(var context: Context, var list : ArrayList<MessageModel>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var ITEM_SENT = 1
    var ITEM_RECEIVED = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType==ITEM_SENT)
            SentViewHolder(
                LayoutInflater.from(context).inflate(R.layout.sent_item_layout,parent,false)
            )
        else ReceiverViewHolder(
            LayoutInflater.from(context).inflate(R.layout.receiver_item_layout,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (FirebaseAuth.getInstance().uid == list[position].senderuid)ITEM_SENT else ITEM_RECEIVED
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = list[position]
        if(holder.itemViewType == ITEM_SENT){
            val viewHolder = holder as SentViewHolder
            viewHolder.binding.userMsg.text = message.message
        }
        else{
            val viewHolder = holder as ReceiverViewHolder
            viewHolder.binding.userMsg.text = message.message
        }

    }
    inner class SentViewHolder(view: View):RecyclerView.ViewHolder(view){
        var binding = SentItemLayoutBinding.bind(view)
    }
    inner class ReceiverViewHolder(view: View):RecyclerView.ViewHolder(view){
        var binding = ReceiverItemLayoutBinding.bind(view)

    }
}