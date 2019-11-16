package Bot.FootballInfo;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class CLInfoGetter {

    private static final String resultsURL  = "https://soccer365.ru/competitions/19/results/";
    private static final String scheduleURL = "https://soccer365.ru/competitions/19/shedule/";

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

    private static Elements loadResultsInfo() throws IOException {
        System.out.println("startParse");
        Connection con = Jsoup.connect(resultsURL).timeout(10);
        System.out.println("endParse");
        Document doc = con.get();
        return doc.getElementsByClass("live_comptt_bd");
    }

    private static Elements loadScheduleInfo() throws IOException {
        Document doc  = Jsoup.connect(scheduleURL).get();
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

}
