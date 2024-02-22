package veryval.dailybot.TGBot.Model;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import veryval.dailybot.TGBot.Entity.Language;
import veryval.dailybot.TGBot.Entity.Purpose;

import java.util.ArrayList;
import java.util.List;

public class BotButtons {


    private static String emojiEn = EmojiParser.parseToUnicode(":gb:");
    private static String emojiDe = EmojiParser.parseToUnicode(":de:");
    private static String emojiFr = EmojiParser.parseToUnicode(":fr:");
    private static String emojiCh = EmojiParser.parseToUnicode(":cn:");
    private static String emojiSp = EmojiParser.parseToUnicode(":es:");

    private static String emojiConf = EmojiParser.parseToUnicode(":rocket:");
    private static String emojiReset = EmojiParser.parseToUnicode(":wastebasket:");


    private static String emojiStudent = EmojiParser.parseToUnicode(":man_student:  :woman_student: ");
    private static String emojiSchool = EmojiParser.parseToUnicode(":school:");
    private static String emojiWork = EmojiParser.parseToUnicode(":computer:");
    private static String emojiTravel = EmojiParser.parseToUnicode(":bridge_at_night:");
    private static String emojiSelf = EmojiParser.parseToUnicode(":smiling_face_with_hearts:");
    private static String emojiOther = EmojiParser.parseToUnicode(":jigsaw:");

    private static final InlineKeyboardButton ENGLISH = new InlineKeyboardButton(Language.ENGLISH.getDisplayName()+emojiEn);
    private static final InlineKeyboardButton DEUTCH = new InlineKeyboardButton(Language.DEUTCH.getDisplayName()+emojiDe);
    private static final InlineKeyboardButton FRENCH = new InlineKeyboardButton(Language.FRENCH.getDisplayName()+emojiFr);
    private static final InlineKeyboardButton CHINEESE = new InlineKeyboardButton(Language.CHINEESE.getDisplayName()+emojiCh);
    private static final InlineKeyboardButton SPANISH = new InlineKeyboardButton(Language.SPANISH.getDisplayName()+emojiSp);


    private static final InlineKeyboardButton CHILD = new InlineKeyboardButton("Ребенок");
    private static final InlineKeyboardButton TEENAGER = new InlineKeyboardButton("Подросток");
    private static final InlineKeyboardButton ADULT = new InlineKeyboardButton("Взрослый");

    private static final InlineKeyboardButton GIFT = new InlineKeyboardButton("Получить подарок");


    private static final InlineKeyboardButton NEWSTUDENT = new InlineKeyboardButton("Хочу стать студентом Индиго");

    private static final InlineKeyboardButton ALREADYSTUDY = new InlineKeyboardButton("Я уже учусь в Индиго");
    private static final InlineKeyboardButton SENDADMINMSG = new InlineKeyboardButton("Отправить всем");
    private static final InlineKeyboardButton PRINTALL = new InlineKeyboardButton("Посмотреть всех пользователей");

    private static final InlineKeyboardButton FOR_EXAM = new InlineKeyboardButton(Purpose.FOR_EXAM.getDisplayName()+emojiStudent);
    private static final InlineKeyboardButton FOR_SCHOOL = new InlineKeyboardButton(Purpose.FOR_SCHOOL.getDisplayName()+emojiSchool);
    private static final InlineKeyboardButton FOR_SELF = new InlineKeyboardButton(Purpose.FOR_SELF.getDisplayName()+emojiSelf);
    private static final InlineKeyboardButton FOR_TRAVEL = new InlineKeyboardButton(Purpose.FOR_TRAVEL.getDisplayName()+emojiTravel);
    private static final InlineKeyboardButton FOR_WORK = new InlineKeyboardButton(Purpose.FOR_WORK.getDisplayName()+emojiWork);
    private static final InlineKeyboardButton OTHER = new InlineKeyboardButton(Purpose.OTHER.getDisplayName()+emojiOther);
    public static InlineKeyboardMarkup inlineLanguageMarkup() {

        ENGLISH.setCallbackData("ENG");
        DEUTCH.setCallbackData("DE");
        FRENCH.setCallbackData("FR");
        CHINEESE.setCallbackData("CH");
        SPANISH.setCallbackData("SP");
        List<InlineKeyboardButton> row1 = List.of(ENGLISH, DEUTCH);
        List<InlineKeyboardButton> row2 = List.of(SPANISH, CHINEESE);
        //List<InlineKeyboardButton> row3 = List.of(SPANISH);
    List<List<InlineKeyboardButton>> rowsInLine = List.of(row1, row2);
    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }



    public ReplyKeyboard inlinePurposeMarkup() {
        OTHER.setCallbackData("OTHER");
        FOR_EXAM.setCallbackData("FOR_EXAM");
        FOR_SCHOOL.setCallbackData("FOR_SCHOOL");
        FOR_SELF.setCallbackData("FOR_SELF");
        FOR_WORK.setCallbackData("FOR_WORK");
        FOR_TRAVEL.setCallbackData("FOR_TRAVEL");
        List<InlineKeyboardButton> row1 = List.of(FOR_EXAM);
        List<InlineKeyboardButton> row2 = List.of(FOR_SCHOOL);
        List<InlineKeyboardButton> row3 = List.of(FOR_SELF);
        List<InlineKeyboardButton> row4 = List.of(FOR_WORK);
        List<InlineKeyboardButton> row5 = List.of(FOR_TRAVEL);
        List<InlineKeyboardButton> row6 = List.of(OTHER);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(row1, row2, row3, row4, row5, row6);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;


    }

    public ReplyKeyboard inlineAgeMarkup() {
        CHILD.setCallbackData("CHILD");
        TEENAGER.setCallbackData("TEENAGER");
        ADULT.setCallbackData("ADULT");
        List<InlineKeyboardButton> row1 = List.of(CHILD);
        List<InlineKeyboardButton> row2 = List.of(TEENAGER);
        List<InlineKeyboardButton> row3 = List.of(ADULT);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(row1, row2, row3);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    public ReplyKeyboard inlineGiftMarkup() {

        GIFT.setCallbackData("GIFT");
        List<InlineKeyboardButton> row1 = List.of(GIFT);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(row1);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);
        return markupInline;
    }


    public ReplyKeyboard inlineChooseMarkup() {

        NEWSTUDENT.setCallbackData("NEW");
        ALREADYSTUDY.setCallbackData("STUDY");
        List<InlineKeyboardButton> row1 = List.of(NEWSTUDENT);
        List<InlineKeyboardButton> row2 = List.of(ALREADYSTUDY);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(row1, row2);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);
        return markupInline;
    }

    public ReplyKeyboard inlineAdminMarkup() {

        SENDADMINMSG.setCallbackData("SENDADMINMSG");
        PRINTALL.setCallbackData("PRINTALL");
        List<InlineKeyboardButton> row1 = List.of(SENDADMINMSG);
        List<InlineKeyboardButton> row2 = List.of(PRINTALL);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(row1, row2);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);
        return markupInline;
    }
}