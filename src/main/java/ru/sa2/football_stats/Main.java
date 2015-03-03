package ru.sa2.football_stats;

import com.google.gson.Gson;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.xml.sax.SAXException;
import ru.sa2.football_stats.match.MatchResultType;
import ru.sa2.football_stats.match_json.Book;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by AlVyaSmirnov on 11.02.2015.
 */
public class Main extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
        String param1 = req.getParameter("param1");
        String matchId = req.getParameter("match");

        String cookies = getCookies("http://24score.com/");
        String URL = "http://24score.com/api/?auth=demo&format=json&sport_id=fbl&match_id=" + matchId + "&result&lang=eng";

        Document page = Jsoup.connect(URL)
                .userAgent("Mozilla/5.0")
                .cookie("PHPSESSID", cookies)
                .get();

        String json = Parser.unescapeEntities(page.body().text(), true);


        PrintWriter writer = resp.getWriter();
        writer.println("<title>" + matchId + "</title>");
        writer.println("<h1>" + "Test" + "</h1>");
        writer.println("<p>" + param1 + "</p>");
        writer.println("<p>" + json + "</p>");

    }

    public static void main(String[] args) throws IOException, JAXBException, ParserConfigurationException, SAXException {

//        List<MatchResultType> matches = getMatchResultTypes(325280, 325290);

//        List<Book> matches = getMatches(325280, 325290);

        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMMM dd, yyyy").withLocale(Locale.US);
        DateTime dateTime = formatter.parseDateTime("January 13, 2012");

        System.out.println();
    }

    private static List<Book> getMatches(int startMatchId, int endMatchId) throws IOException {
        List<Book> matches = new ArrayList<Book>();
        String cookies = getCookies("http://24score.com/");
        Gson gson = new Gson();

        for (int i = startMatchId; i <= endMatchId; i++) {
            String URL = "http://24score.com/api/?auth=demo&format=json&sport_id=fbl&match_id=" + i + "&result&lang=eng";

            Document page = Jsoup.connect(URL)
                    .userAgent("Mozilla/5.0")
                    .cookie("PHPSESSID", cookies)
                    .get();

            String json = Parser.unescapeEntities(page.body().text(), true);
            Book match = gson.fromJson(json, Book.class);
            matches.add(match);
        }
        return matches;

    }
    private static List<MatchResultType> getMatchResultTypes(int startMatchId, int endMatchId) throws IOException, JAXBException {
        List<MatchResultType> matches = new ArrayList<MatchResultType>();
        String cookies = getCookies("http://24score.com/");

        for (int i = startMatchId; i <= endMatchId; i++) {
            String URL = "http://24score.com/api/?auth=demo&sport_id=fbl&match_id=" + i + "&result&lang=eng";

            Document page = Jsoup.connect(URL)
                    .userAgent("Mozilla/5.0")
                    .cookie("PHPSESSID", cookies)
                    .get();
            Element mainData = page.select("match_result").first();

            JAXBContext jaxbContext = JAXBContext.newInstance(MatchResultType.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            String string = mainData.toString().replaceAll("&quot;", "");
            MatchResultType match = (MatchResultType) unmarshaller.unmarshal(new StringReader(Parser.unescapeEntities(string, true)));
            match.setMatchId(i);
            matches.add(match);
        }
        return matches;
    }

    private static String getCookies(String loginURL) throws IOException {
        Connection.Response res = Jsoup.connect(loginURL)
                .method(Connection.Method.POST)
                .execute();
        return res.cookie("PHPSESSID");
    }
}
