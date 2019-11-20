package com.v_cognitio.VKFootballBot.bot;


import com.v_cognitio.VKFootballBot.VKApiSDK.com.petersamokhin.bots.sdk.clients.User;
import com.v_cognitio.VKFootballBot.VKApiSDK.com.petersamokhin.bots.sdk.objects.Message;
import com.v_cognitio.VKFootballBot.bot.FootballInfo.CLInfoGetter;
import com.v_cognitio.VKFootballBot.bot.FootballInfo.LeagueInfoGetter;

import java.util.regex.Pattern;

public class FootballBot {

    private String key;
    private User user;

    public FootballBot(String key) {
        this.key = key;
        this.user = null;
    }

    public void start() {
        user = new User(key);
        user.onMessage(this::onMessageHolder);
        user.onChatJoin((Integer a, Integer b, Integer id) ->
                sendTextMessage(id, "Привет, я футбольный бот! " +
                        "Напиши /help, чтобы увидеть список команд")
        );
    }

    private void onMessageHolder(Message message) {
        String query = message.getText().toLowerCase();

        if (query.equals("тачки")) {
            new Message()
                    .from(user)
                    .to(resolveId(message))
                    .attachments("audio526111445_456239018")
                    .send();
        }
        else if (query.equals("хуй")) {
            sendTextMessage(resolveId(message),
                    "*id" + message.authorId() + "(сам хуй)");
        }
        else if (Pattern.matches("^/cl.*", query)) {
            holdCL(message, query);
        }
        else if (Pattern.matches("^/table.*", query)) {
            holdTable(message, query);
        }
        else if (Pattern.matches("^/help", query)) {
            sendTextMessage(resolveId(message),
                    "/cl [tour] - выводит результаты одного из туров Лиги Чемпионов\n\n" +
                            "/table [league] - выводит таблицу одного из топ-чемпионатов:  " +
                            "rfpl, epl, laliga, bundes, seriea, ligue1\n\n" +
                            "тачки - тачки :)");
        }

    }

    private void holdCL(Message message, String query) {
        if (Pattern.matches("^/cl\\s*[\\d]+", query)) {
            try {
                //sendTextMessage(resolveId(message),
                //        "fetching..");
                //TODO:: log this
                int groupTour = Integer.parseInt(query.split(" ")[1].trim());
                sendTextMessage(resolveId(message),
                        CLInfoGetter.getGroupTour(groupTour));
                //sendTextMessage(resolveId(message),
                //        "end..");
                //TODO:: log this
            }
            catch (IllegalArgumentException e) {
                sendTextMessage(resolveId(message),
                        e.getMessage());
            }
            catch (Exception e) {
                sendTextMessage(resolveId(message),
                        "Something wrong");
            }
        }
        else {
            sendTextMessage(resolveId(message),
                    "Invalid arguments");
        }
    }

    private void holdTable(Message message, String query) {
        if (Pattern.matches("^/table\\s+\\S+", query)) {
            try {
                //sendTextMessage(resolveId(message),
                //        "fetching..");
                //TODO:: log this
                sendTextMessage(resolveId(message),
                        LeagueInfoGetter.getTable(query.split(" ")[1]).trim());
                //sendTextMessage(resolveId(message),
                //        "end..");
                //TODO:: log this
            }
            catch (IllegalArgumentException e) {
                sendTextMessage(resolveId(message),
                        e.getMessage());
            }
            catch (Exception e) {
                sendTextMessage(resolveId(message),
                        "Something wrong");
            }
        }
        else {
            sendTextMessage(resolveId(message),
                    "Invalid number of arguments");
        }
    }

    private void sendTextMessage(Integer id, String text) {
        new Message()
                .from(user)
                .to(id)
                .text(text)
                .send();
    }

    private Integer resolveId(Message message) {
        return message.isMessageFromChat() ?
                message.getChatIdLong() :
                message.authorId();
    }
}
