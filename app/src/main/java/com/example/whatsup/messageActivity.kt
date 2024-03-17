package com.example.whatsup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.whatsup.adapter.messageAdapter
import com.example.whatsup.databinding.ActivityMessageBinding
import com.example.whatsup.model.MessageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date

class messageActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMessageBinding
    private lateinit var database:FirebaseDatabase
    private lateinit var senderuid:String
    private lateinit var receiveuid:String
    private lateinit var senderRoom:String
    private lateinit var receiverRoom:String
    private lateinit var list : ArrayList<MessageModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        senderuid = FirebaseAuth.getInstance().uid.toString()
        receiveuid = intent.getStringExtra("uid")!!
        list = ArrayList()
        senderRoom = senderuid+receiveuid
        receiverRoom = receiveuid+senderuid
        database = FirebaseDatabase.getInstance()
        binding.imageView21.setOnClickListener{
            if(binding.messageBox.text.isEmpty()){
                Toast.makeText(this,"Type a Message first",Toast.LENGTH_SHORT).show()
            }
            else{
                val message = MessageModel(binding.messageBox.text.toString(),senderuid, Date().time)
                val randomKey = database.reference.push().key
                database.reference.child("Chats").child(senderRoom).child("message").child(randomKey!!).setValue(message).addOnSuccessListener {


                    database.reference.child("Chats").child(receiverRoom).child("message").child(randomKey).setValue(message).addOnSuccessListener {
                        binding.messageBox.text = null
                        Toast.makeText(this,"Message sent",Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }

        database.reference.child("Chats").child(senderRoom).child("message")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for(snapshot1 in snapshot.children){
                        val data = snapshot1.getValue(MessageModel::class.java)
                        list.add(data!!)
                    }
                    binding.recyclerreceiverViewMessage.adapter = messageAdapter(this@messageActivity,list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@messageActivity,"Error : $error",Toast.LENGTH_SHORT).show()
                }

            })
    }
}