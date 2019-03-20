package com.zolotarev.fakeSMTP

import com.zolotarev.fakeSMTP.bot.FakeSMTPBot
import com.zolotarev.fakeSMTP.storage.MapRegisteredEmailStorage
import org.subethamail.smtp.helper.SimpleMessageListenerAdapter
import org.subethamail.smtp.server.SMTPServer
import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.ApiContext
import org.telegram.telegrambots.meta.TelegramBotsApi
import java.util.*

class FakeSMTPApplication {

    fun start() {
        val properties = Properties().also { it.load(ClassLoader.getSystemResourceAsStream("fake_smtp.properties")) }

        val botsApi = initTelegramBotApi()
        val fakeSMTPBot = initTelegramBot(properties)
        botsApi.registerBot(fakeSMTPBot)

        startSMTPServer(fakeSMTPBot, properties)
    }

    private fun startSMTPServer(fakeSMTPBot: FakeSMTPBot, properties: Properties) {
        val smtp = SMTPServer(SimpleMessageListenerAdapter(fakeSMTPBot))
        smtp.port = properties["smtp.port"]?.toString()?.toIntOrNull() ?: DEFAULT_SMTP_PORT
        smtp.start()
    }

    private fun initTelegramBot(properties: Properties): FakeSMTPBot {
        val botOptions = ApiContext.getInstance(DefaultBotOptions::class.java)
        return FakeSMTPBot(botOptions, MapRegisteredEmailStorage(), properties)
    }

    private fun initTelegramBotApi(): TelegramBotsApi {
        ApiContextInitializer.init()
        return TelegramBotsApi()
    }

    companion object {
        const val DEFAULT_SMTP_PORT = 8080
    }
}