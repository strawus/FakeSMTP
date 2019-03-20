package com.zolotarev.fakeSMTP.bot.command

import com.zolotarev.fakeSMTP.storage.RegisteredEmailStorage
import org.apache.commons.validator.routines.EmailValidator
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender

class RegisterCommand(private val registeredEmailStorage: RegisteredEmailStorage) :
        BotCommand("register", "Register email to listening") {

    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) {
        if (arguments.isEmpty()) {
            absSender.execute(SendMessage(chat.id, "You should specify at least one email. Example: /register foo@bar.ru"))
            return
        }
        val invalidEmails = arguments.filterNot { EmailValidator.getInstance(true).isValid(it) }
        if (invalidEmails.isNotEmpty()) {
            absSender.execute(SendMessage(chat.id, "Invalid emails: ${invalidEmails.joinToString { it }}"))
            return
        }
        arguments.forEach { registeredEmailStorage.add(it, chat.id) }
        absSender.execute(SendMessage(chat.id, "Register success"))
    }
}