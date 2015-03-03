package ru.sa2.parser_avito;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


public class RunApp {

    private static Logger log = Logger.getLogger(RunApp.class);

    public static void main(String[] args) throws IOException{
//change
        ArrayList<String> list_URLs = new ArrayList<String>();
        list_URLs.add("http://www.avito.ru/voronezh/doma_dachi_kottedzhi/prodam?p=1&pmax=250000&pmin=0");
        list_URLs.add("http://www.avito.ru/voronezh/doma_dachi_kottedzhi/prodam?p=2&pmax=250000&pmin=0");
        list_URLs.add("http://www.avito.ru/semiluki/doma_dachi_kottedzhi/prodam?p=1&pmax=250000&pmin=0");
        list_URLs.add("http://www.avito.ru/semiluki/doma_dachi_kottedzhi/prodam?p=2&pmax=250000&pmin=0");
        list_URLs.add("http://www.avito.ru/novaya_usman/doma_dachi_kottedzhi/prodam?p=1&pmax=250000&pmin=0");
        list_URLs.add("http://www.avito.ru/novaya_usman/doma_dachi_kottedzhi/prodam?p=2&pmax=250000&pmin=0");

        Document page = new Document("");
        System.out.println("change");
        Elements ads = new Elements();
        for (String URL : list_URLs){
            try {
                page = Jsoup.connect(URL).get();
            } catch (org.jsoup.HttpStatusException e) {
                log.error("BAD URL: " + URL);
            }
            for (Element table : page.select("div[class=b-catalog-table]")
                                     .select("div[class=item c-b-0 clearfix]")){
                ads.add(table);
            }
        }

        String cost;
        String name;
        String refer;
        String distance_to_city;
        String date;
        int counter = 0;

        for (Element ad : ads) {
            counter++;

            date = ad
                    .select("div[class=date]")
                    .text();

            if (!(date.contains("Вчера") || date.contains("Сегодня"))) continue;

            cost = ad
                    .select("div[class=description]")
                    .select("div[class=about]")
                    .text();

            if (cost.isEmpty()) continue;

            distance_to_city = ad
                    .select("div[class=description]")
                    .select("p[class=address]")
                    .text();

            int distance = 0;
            if (!distance_to_city.isEmpty()){
                distance = Integer.parseInt(distance_to_city.replaceAll("[^0-9]", ""));
                if (distance > 40) continue;
            }

            name = ad
                    .select("div[class=description")
                    .select("h3[class=title]")
                    .select("a[title]")
                    .text();

            refer = ad
                    .select("div[class=description")
                    .select("h3[class=title]")
                    .select("a[href]")
                    .attr("href");


            System.out.println(name);
            System.out.println(cost);
            System.out.println("DISTANCE_TO_CITY: " + distance + "km");
            System.out.println("http://avito.ru" + refer);
            System.out.println(date);
            System.out.println();
        }
    }


}
