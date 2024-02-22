package veryval.dailybot.TGBot.Service;


import com.vdurmont.emoji.EmojiParser;
import lombok.*;
import org.apache.log4j.Logger;
import org.glassfish.jersey.internal.util.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import veryval.dailybot.TGBot.Config.TelegramBotConfig;

import veryval.dailybot.TGBot.Entity.Language;
import veryval.dailybot.TGBot.Entity.Purpose;
import veryval.dailybot.TGBot.Entity.User;
import veryval.dailybot.TGBot.Model.BotButtons;
import veryval.dailybot.TGBot.Model.BotCommands;

import veryval.dailybot.TGBot.Repository.UserRepository;


import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;


@Component
public class BotService extends TelegramLongPollingBot implements BotCommands {
    private static final Logger log = Logger.getLogger(BotService.class);
    private static final String START = "/start";
    private static final String ADMIN = "/admin";

    private int messageID;
    private boolean adminMessage = false;
    private boolean stud_already = false;

    String prevMessage;
    String clientName = "Пожалуйста введите Ваше имя ";
    String clientPhone = "Пожалуйста введите телефон для связи.";
    String clientAge = "Пожалуйста выберите возрастную группу:";


    TelegramBotConfig config;
    public BotService(TelegramBotConfig config) {
        this.config = config;
    }

    @Setter
    @Getter
    String userName;
    @Setter
    @Getter
    String token;
    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;



    @Override
    public void onUpdateReceived(Update update) {
        log.debug("Receive new Update. updateID: " + update.getUpdateId());
        Long chatId;
        Long userId;
        String inputText;
        chatId = 0L;
        //если получено сообщение текстом
        if(update.hasMessage()) {

            chatId = update.getMessage().getChatId();
            userName = update.getMessage().getFrom().getUserName();

            if(userRepository.findById(update.getMessage().getChatId()).isEmpty()){User newUser = new User();
                newUser.setChatId(chatId);
                newUser.setName(userName);
                userRepository.save(newUser);
            }


            if (update.getMessage().hasText()) {
                inputText = update.getMessage().getText();

                try {
                    botAnswerUtils(inputText, chatId, userName);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }

            //если нажата одна из кнопок бота
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            userId = update.getCallbackQuery().getFrom().getId();
            userName = update.getCallbackQuery().getFrom().getFirstName();
            messageID = update.getCallbackQuery().getMessage().getMessageId();
            inputText = update.getCallbackQuery().getData();


            try {
                botButtonUtils(inputText, chatId, userName, messageID);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void botAnswerUtils(String receivedMessage, long chatId, String userName) throws TelegramApiException {


        switch (receivedMessage){
            case (START):
                SendMessage message = new SendMessage();
                message.setChatId(String.valueOf(chatId));
                String emojiMsg = EmojiParser.parseToUnicode(":wave:" + "Добрый день, "+ userName +  "! \n" +
                        "Это Telegram бот образовательного центра \"Индиго\"");
                message.setText(emojiMsg);
                BotButtons buttons = new BotButtons();
                message.setReplyMarkup(buttons.inlineChooseMarkup());
                execute(message);


                break;


            case (ADMIN):
                if ((config.getBotOwner() == chatId) && (adminMessage == false)) {
                    SendMessage admMessage = new SendMessage();
                    BotButtons admButtons = new BotButtons();
                    admMessage.setReplyMarkup(admButtons.inlineChooseMarkup());
                } else {
                    sendMsg(chatId, "Прошу прощения, но Вы не администратор.");
                    startCommand(chatId,userName);
                }
                break;

            default:

                if (adminMessage) {
                    sendMessageToAll(receivedMessage);
                    adminMessage = false;
                    break;
                }

                if (prevMessage.equals(clientName)) {

                    if (!receivedMessage.isEmpty()){
                    Optional<User> optionalUser = userRepository.findById(chatId);
                    User newUser = optionalUser.get();
                    newUser.setName(receivedMessage);
                userRepository.save(newUser);
                getClientPhone(chatId);}
                    else {
                        sendMsg(chatId, "Поле не может быть пустым.");
                        getClientName(chatId);
                    }
                    break;
                } else
                if (prevMessage.equals(clientPhone)) {

                    if (!receivedMessage.isEmpty()) {
                        Optional<User> optionalUser = userRepository.findById(chatId);
                        User newUser = optionalUser.get();
                        newUser.setTelephone(receivedMessage);
                        userRepository.save(newUser);
                        getClientAge(chatId);
                    } else {
                        sendMsg(chatId, "Поле не может быть пустым.");
                        getClientPhone(chatId);
                    }
                    break;
                } else
                if (prevMessage.equals(clientAge)) {

                    if (!receivedMessage.isEmpty()) {
                        Optional<User> optionalUser = userRepository.findById(chatId);
                        User newUser = optionalUser.get();
                        newUser.setAge(Integer.parseInt(receivedMessage));
                        userRepository.save(newUser);
                        getPurpose(chatId);
                    }else {
                        sendMsg(chatId, "Поле не может быть пустым.");
                        getClientAge(chatId);
                    }
                    break;
                } else
                {
                    unknownCommand(chatId);}
                break;

        }
    }
    private void newOrderCommand(Long chatId) throws TelegramApiException {

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        String emojiMsg = EmojiParser.parseToUnicode("Пожалуйста, выберите язык, который планируете изучать :mortar_board:.");

        message.setText(emojiMsg);
        BotButtons buttons = new BotButtons();
        message.setReplyMarkup(buttons.inlineLanguageMarkup());
        execute(message);
    }

    private void getClientName(long chatId) throws TelegramApiException {

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        String emojiMsg = EmojiParser.parseToUnicode(clientName+":writing_hand:");
        message.setText(emojiMsg);
        execute(message);
        prevMessage = clientName;
    }


    private void getClientPhone(long chatId) throws TelegramApiException {

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        String emojiMsg = EmojiParser.parseToUnicode(clientPhone + ":phone:");

        message.setText(emojiMsg);

        execute(message);
        prevMessage = clientPhone;
    }
    private void getClientAge(long chatId) throws TelegramApiException {

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        String emojiMsg = EmojiParser.parseToUnicode(clientAge + ":date:");


        message.setText(emojiMsg);
        BotButtons buttons = new BotButtons();
        message.setReplyMarkup(buttons.inlineAgeMarkup());
        execute(message);
        prevMessage = clientAge;
    }

    private void getPurpose(long chatId) throws TelegramApiException {

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        String emojiMsg = EmojiParser.parseToUnicode("Пожалуйста выберите цель обучения"+ ":dart:");
        message.setText(emojiMsg);
        BotButtons buttons = new BotButtons();
        message.setReplyMarkup(buttons.inlinePurposeMarkup());
        execute(message);
    }

    private StringBuilder orderConstruct (long chatId){
        Optional<User> optionalUser = userRepository.findById(chatId);
        User newUser = optionalUser.get();

        StringBuilder orderStringBuilder = new StringBuilder();

        orderStringBuilder.append("Данные заявки:"+"\n")
                .append("   Имя :" + newUser.getName()+"\n")
                .append("   Возраст :" + displayAge(newUser)+"\n")
                .append("   Требуемый язык :" + newUser.getLanguage().getDisplayName()+"\n")
                .append("   Цель обучения :" + newUser.getPurpose().getDisplayName()+"\n")
                .append("   Контактный телефон : \n" + newUser.getTelephone() +"\n");


        return orderStringBuilder;
    }


    private void generateOrder(long chatId) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        prevMessage = "clean";

        StringBuilder order = orderConstruct(chatId);
        String emojiMsg = EmojiParser.parseToUnicode("Спасибо! :partying_face:." + "Ваша заявка принята. :white_check_mark: \n" +
                "Вы сделали первый шаг на пути к изучению языка."+
                "В ближайшее время мы Вам позвоним для знакомства :raised_hands: \n"+
                "Надеемся, Вам понравится подарок, который мы подготовили :hugging:  \n"
        );

        message.setText(emojiMsg+
                "Номер заявки №" + chatId +"\n" +
                order+ "\n");
        BotButtons buttons = new BotButtons();
        message.setReplyMarkup(buttons.inlineGiftMarkup());
        execute(message);


        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("veryval@mail.ru");
        mailMessage.setTo(config.getEmailReciever());
        mailMessage.setSubject("Заявка от телеграм бота №" + chatId);
        mailMessage.setText(order.toString());
        emailService.sendEmail(mailMessage);

    }

    public void sendMessageToAll (String textToSend){

        String emojiMsg = EmojiParser.parseToUnicode(textToSend);

        Iterable<User> users = userRepository.findAll();
        for (User user: users
             ) {
            long id = user.getChatId();
            try {
                sendMsg(id, emojiMsg);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public String getBotUsername() {
        return config.getUserName();
    }


    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    private void startCommand(Long chatId, String userName) throws TelegramApiException {
     //   User newUser = new User();
     //   newUser.setChatId(chatId);
     //   newUser.setName(userName);
     //   userRepository.save(newUser);
        String emojiMsg = EmojiParser.parseToUnicode(":wave:" + "Добрый день, "+ userName +  "! \n" +
                "Мы очень рады, что вы решили изучать иностранный язык. Это одно из тех решений, " +
                "которые по-настоящему меняют нашу жизнь :earth_africa:. \n После заполнения заявки ваш ожидает " +
                "приятный подарок! :gift: " );
        sendMsg(chatId, emojiMsg);
        newOrderCommand(chatId);

   }

    private void unknownCommand(Long chatId) throws TelegramApiException {
        var text = "Не удалось распознать команду!";
        sendMsg(chatId, text);
    }

    public void sendMsg(Long chatId, String text) throws TelegramApiException {
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage(chatIdStr, text);
        execute(sendMessage);
    }

    public String displayAge(User user){
        int age = user.getAge();
        String ageStr = "";
        if (age == 4){
            ageStr = "Ребенок";
        }
        if (age == 12){
            ageStr = "Подросток";
        }

        if (age == 18){
            ageStr = "Взрослый";
        }

        return ageStr;
    }


    private void botButtonUtils(String receivedMessage, long chatId, String userName, int messageID) throws ParseException, TelegramApiException {
        // Загрузка существующего пользователя из репозитория
        Optional<User> optionalUser = userRepository.findById(chatId);
        User newUser = optionalUser.get();


            switch (receivedMessage) {
                case ("ENG"):

                    newUser.setLanguage(Language.ENGLISH);
                    userRepository.save(newUser);
                    getClientName(chatId);

                    break;
                case ("DE"):

                    newUser.setLanguage(Language.DEUTCH);
                    userRepository.save(newUser);
                    getClientName(chatId);

                    break;
                case ("CH"):

                    newUser.setLanguage(Language.CHINEESE);
                    userRepository.save(newUser);
                    getClientName(chatId);

                    break;
                case ("FR"):

                    newUser.setLanguage(Language.FRENCH);
                    userRepository.save(newUser);
                    getClientName(chatId);

                    break;
                case ("SP"):

                    newUser.setLanguage(Language.SPANISH);
                    userRepository.save(newUser);
                    getClientName(chatId);

                    break;


                case ("FOR_EXAM"):

                    newUser.setPurpose(Purpose.FOR_EXAM);
                    userRepository.save(newUser);
                    generateOrder(chatId);

                    break;

                case ("FOR_SCHOOL"):

                    newUser.setPurpose(Purpose.FOR_SCHOOL);
                    userRepository.save(newUser);
                    generateOrder(chatId);

                    break;


                case ("FOR_SELF"):

                    newUser.setPurpose(Purpose.FOR_SELF);
                    userRepository.save(newUser);
                    generateOrder(chatId);

                    break;

                case ("FOR_WORK"):

                    newUser.setPurpose(Purpose.FOR_WORK);
                    userRepository.save(newUser);
                    generateOrder(chatId);

                    break;

                case ("FOR_TRAVEL"):

                    newUser.setPurpose(Purpose.FOR_TRAVEL);
                    userRepository.save(newUser);
                    generateOrder(chatId);

                    break;


                case ("OTHER"):

                    newUser.setPurpose(Purpose.OTHER);
                    userRepository.save(newUser);
                    generateOrder(chatId);

                    break;



                case ("CHILD"):

                    newUser.setAge(4);
                    userRepository.save(newUser);
                    getPurpose(chatId);
                    break;

                case ("TEENAGER"):

                    newUser.setAge(12);
                    userRepository.save(newUser);
                    getPurpose(chatId);
                    break;

                case ("ADULT"):

                    newUser.setAge(18);
                    userRepository.save(newUser);
                    getPurpose(chatId);
                    break;


                case ("GIFT"):
                    //sendMsg(chatId, "Отправили, дождитесь получения. Может занять немного времени.");
                    sendFile(chatId,"/root/images/Gift.pdf","pdf");
                    break;

                case ("NEW"):
                    //if (newUser.getStud_already()){
                    //    sendMsg(chatId, "/start");
                    //} else
                    startCommand(chatId, userName);
                    break;


                case ("STUDY"):

                    String emojiMsg = EmojiParser.parseToUnicode("Здравствуйте , "+ userName +  "! \n" +
                            "Это телеграм бот образовательного центра \"Индиго\" \n " +
                            "В этом чате мы будем информировать вас обо всех акциях, скидках, классных новостях, " +
                            "чтобы вы ничего не пропустили. :ok_hand: \n" +
                            "Сегодня мы подготовили для вас небольшой подарок :gift:. " +
                            "Самые эффективные лайвхаки для изучения иностранных языков! \n" +
                            "Надеемся, вам понравится! :hugging:" );


                    sendMsg(chatId, emojiMsg);

                    Optional<User> stud = userRepository.findById(chatId);
                    User studUser = stud.get();
                    studUser.setStud_already(true);
                    userRepository.save(studUser);


                    sendFile(chatId,"/root/images/Gift.pdf","pdf");

                    break;


                case ("SENDADMINMSG"):
                    adminMessage = true;
                    SendMessage admMessage = new SendMessage();
                    admMessage.setChatId(String.valueOf(chatId));
                    admMessage.setText("Отправьте сообщение всем пользователям:");
                    execute(admMessage);
                    adminMessage = false;
                    break;


                default:
                    unknownCommand(chatId);
                    break;
            }
    }


    private void deleteMessage(Long chatId) throws TelegramApiException {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageID);
        execute(deleteMessage);
    }

    public void sendFile(long chatId, String filePath, String fileType) throws TelegramApiException {
        SendDocument document = new SendDocument();
        document.setChatId(chatId);

        File file = new File(filePath);
        if (file.exists()) {
            document.setDocument(new InputFile(file));
            document.setCaption(fileType);
            try {
                execute(document);
            } catch (TelegramApiException e) {
                // Handle the exception by printing the stack trace or logging the error
                e.printStackTrace();
                sendMsg(Long.valueOf(510957313),"Failed to send document: " + e.getMessage());
                throw new RuntimeException("Failed to send document: " + e.getMessage());
            }
        } else {
            sendMsg(Long.valueOf(510957313),"Failed to send document: " + file.getAbsolutePath());
            throw new RuntimeException("File not found: " + file.getAbsolutePath());
        }
    }



}


