package com.v_cognitio.VKFootballBot.bot.FootballInfo;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class CLInfoGetter {

    private static final String resultsURL = "https://soccer365.ru/competitions/19/results/";
    private static final String scheduleURL = "https://soccer365.ru/competitions/19/shedule/";

    private static final OkHttpClient client = new OkHttpClient();

    public static String getGroupTour(int tour) throws IOException, IllegalArgumentException {
        String query = "Групповая стадия | " + tour + "-й тур";

        Elements tours = loadResultsInfo();
        Element tourInfo = findTourByName(query, tours);
        if (tourInfo == null) {
            tours = loadScheduleInfo();
            tourInfo = findTourByName(query, tours);
            if (tourInfo == null) {
                throw new IllegalArgumentException("Tour " + tour + " doesn't exist");
            }
        }

        StringBuffer res = new StringBuffer();
        for (Element game : tourInfo.getElementsByClass("game_block")) {
            Element ht = game.select("[class=\"game_ht\"]").get(0);
            Element at = game.select("[class=\"game_at\"]").get(0);
            res.append(ht.text()).append(" ").append(at.text()).append('\n');
        }

        return res.toString();
    }

    public static String getAvailableTours() throws IOException {
        StringBuffer res = new StringBuffer();
        res.append("Прошедшие туры:\n")    .append(getToursFromElements(loadResultsInfo()))
           .append("\nПредстоящие туры:\n").append(getToursFromElements(loadScheduleInfo()));

        return res.toString();
    }

    private static Elements loadResultsInfo() throws IOException {
        return loadDataFromURL(resultsURL);
    }

    private static Elements loadScheduleInfo() throws IOException {
        return loadDataFromURL(scheduleURL);
    }

    private static Elements loadDataFromURL(String url) throws IOException {
        //TODO:: add logging
        /*Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request)
                .execute();

        Document doc = Jsoup.parse(response.body().byteStream(), "UTF-8", url);*/
        //TODO:: rewrite if via OkHTTP (like commented code)
        Document doc = Jsoup.connect(url).get();
        return doc.getElementsByClass("live_comptt_bd");
    }

    private static Element findTourByName(String name, Elements tours) {
        for (Element t : tours) {
            Elements stage = t.getElementsByClass("block_header");
            if (stage != null && stage.get(0).ownText().equals(name)) {
                return t;
            }
        }
        return null;
    }

    private static String getToursFromElements(Elements page) {
        StringBuffer res = new StringBuffer();
        for (Element t : page) {
            res.append("●").
                    append(t.getElementsByClass("block_header").get(0).ownText()).append('\n');
        }

        return res.toString();
    }

}
