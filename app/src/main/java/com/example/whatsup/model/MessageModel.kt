package com.example.whatsup.model

import java.sql.Timestamp

data class MessageModel(
    var message : String?="",
    var senderuid: String?="",
    var timestamp: Long?=0
)
