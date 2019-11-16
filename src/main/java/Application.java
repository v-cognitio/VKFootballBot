import Bot.FootballBot;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);
    private static String key2;

    public static void main(String[] args) {

        System.setProperty("console.encoding", "UTF-8");
        BasicConfigurator.configure();

        LOG.info("hi");
        loadProperties();
        FootballBot bot = new FootballBot(key2);
        bot.start();
    }

    static void loadProperties() {
        Properties properties = new Properties();
        try (InputStream is = Application.class.getResourceAsStream("/config.properties")) {
            properties.load(is);
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }

        key2 = properties.getProperty("bot.vk.key2");
    }
}
