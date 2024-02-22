package veryval.dailybot.TGBot.Config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@Data
@PropertySource("bot.properties")

public class TelegramBotConfig {

    @Value("${telegrambot.userName}")
    String userName;
    @Value("${telegrambot.botToken}")
    String botToken;
    @Value("${telegrambot.botOwner}")
    long botOwner;
    @Value("${telegrambot.emailReciever}")
    String emailReciever;

}