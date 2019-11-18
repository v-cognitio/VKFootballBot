package com.v_cognitio.VKFootballBot.bot.FootballInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;

import java.io.IOException;

public class LeagueInfoGetter {

    private static String generateLeagueLink(String league) {
        switch (league) {
            case "rfpl":
                return "https://www.livesport.ru/football/rfpl/calendar/";
            case "epl":
                return "https://www.livesport.ru/football/english-premier-league/calendar/";
            case "laliga":
                return "https://www.livesport.ru/football/primera-division/calendar/";
            case "bundes":
                return "https://www.livesport.ru/football/bundesliga/calendar/";
            case "seriea":
                return "https://www.livesport.ru/football/serie-a/calendar/";
            case "ligue1":
                return "https://www.livesport.ru/football/ligue-1/calendar/";
            default:
                return null;
        }
    }

    public static String getTable(String league) throws IOException {
        Document doc;
        try {
            doc = Jsoup.connect(generateLeagueLink(league)).get();
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("League with name " + league + " isn't supported");
        }
        StringBuffer res = new StringBuffer();
        for (Node node : doc.getElementsByClass("cal_tt").get(0).childNodesCopy()) {
            int ind = 0;
            if (node.nodeName().equals("#text")) continue;
            for (Node sn : node.childNodesCopy()) {
                if (ind == 0 || ind == 7) {
                    res.append(sn.childNodes().get(0));
                }
                if (ind == 1) {
                    res.append(sn.childNodes().get(1)).append(" ");
                }
                ++ind;
            }
            res.append('\n');
        }

        return res.toString();
    }

}
