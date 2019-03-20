package com.zolotarev.fakeSMTP.storage

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentSkipListSet

class MapRegisteredEmailStorage : RegisteredEmailStorage {
    private val recipientToChat: MutableMap<String, MutableSet<Long>> = ConcurrentHashMap()

    override fun add(email: String, chatId: Long) =
            recipientToChat.getOrPut(email) { ConcurrentSkipListSet() }.add(chatId)

    override fun remove(email: String, chatId: Long) =
            recipientToChat[email]?.remove(chatId) ?: false

    override fun get(email: String) = recipientToChat[email]
}