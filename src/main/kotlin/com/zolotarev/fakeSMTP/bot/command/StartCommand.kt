package com.zolotarev.fakeSMTP.bot.command

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.bots.AbsSender

class StartCommand : BotCommand("start", "Start using FakeSMTP") {
    override fun execute(absSender: AbsSender, user: User, chat: Chat, arguments: Array<out String>) {
        absSender.execute(SendMessage(chat.id, """FakeSMTP is a tool, which used for testing applications with sending email functional
                        |/register <email> - Start listening all messages, which sent to this email address
                        |/deregister <email> - Stop listening email""".trimMargin()))
    }

}