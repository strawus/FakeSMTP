package com.zolotarev.fakeSMTP.bot

import com.zolotarev.fakeSMTP.bot.command.DeregisterCommand
import com.zolotarev.fakeSMTP.bot.command.RegisterCommand
import com.zolotarev.fakeSMTP.bot.command.StartCommand
import com.zolotarev.fakeSMTP.storage.RegisteredEmailStorage
import org.apache.commons.mail.util.MimeMessageParser
import org.subethamail.smtp.helper.SimpleMessageListener
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import java.io.InputStream
import java.util.*
import javax.mail.internet.MimeMessage

class FakeSMTPBot(botOptions: DefaultBotOptions,
                  private val registeredEmailStorage: RegisteredEmailStorage,
                  private val properties: Properties) :
        TelegramLongPollingCommandBot(botOptions, "fake_smtp_bot"), SimpleMessageListener {

    init {
        register(StartCommand())
        register(RegisterCommand(registeredEmailStorage))
        register(DeregisterCommand(registeredEmailStorage))
    }

    override fun getBotToken() = properties["bot.token"]?.toString() ?: throw IllegalStateException("Telegram bot token was not specified")

    override fun processNonCommandUpdate(update: Update) {
        execute(SendMessage(update.message.chatId, "Unsupported command"))
    }

    override fun deliver(from: String, recipient: String, data: InputStream) {
        registeredEmailStorage.get(recipient)?.let {
            val message = parseMessage(data)
            it.forEach { execute(SendMessage(it, message)) }
        }
    }

    override fun accept(from: String, recipient: String) = true

    private fun parseMessage(data: InputStream): String {
        val messageParser = MimeMessageParser(MimeMessage(null, data)).parse()
        return buildMessage(messageParser.from,
                messageParser.to.joinToString { it.toString() },
                messageParser.subject,
                parseBody(messageParser))
    }

    private fun buildMessage(from: String, to: String, subject: String, body: String) = """
            |From: $from
            |To: $to
            |Subject: $subject
            |Body: $body
            """.trimMargin()

    private fun parseBody(messageParser: MimeMessageParser) = when {
        messageParser.hasPlainContent() -> messageParser.plainContent
        messageParser.hasHtmlContent() -> messageParser.htmlContent
        else -> ""
    }
}