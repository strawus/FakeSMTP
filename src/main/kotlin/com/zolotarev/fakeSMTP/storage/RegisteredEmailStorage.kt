package com.zolotarev.fakeSMTP.storage

interface RegisteredEmailStorage {

    fun add(email: String, chatId: Long): Boolean
    fun remove(email: String, chatId: Long): Boolean
    fun get(email: String): Set<Long>?
}