package one.pandaland.crawlTerm;

import java.io.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Connection.Response;
import java.io.IOException;


public class App {
    private static final String FOLDERPATH = "./photo/";
    public static final String  matchWord  = "ThumbnailID";
    public static boolean       debug      = false;

    public static void main(String[] args) throws IOException {
        Document d =
                     Jsoup.connect("https://hongkong.asiaxpat.com/classifieds/free/")
                          .header("Accept-Encoding", "gzip, deflate")
                          .userAgent("Mozilla/5.0 (Windows NT 6.2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.69 Safari/537.36")
                          .timeout(6000).get();
        if (debug) {
            System.out.println(d.html());
            System.out.println("++++++++++++++++");
        }


        Elements products = d.getElementsByClass("classified-body"); //<div class="classified-body listitem classified-summary 1">
        if (debug) {
            System.out.println(">>>>>>>>>");
            System.out.println(products.size());
        }

        for (Element element : products) {
            Elements photos = element.select("img");
            if (debug) {
                System.out.println(photos.size());
            }

            String title = element.select("h4.R a").text();
            if (debug) {
                System.out.println(title);
            }
            int c = 0;
            for (Element photo : photos) {
                String urlp = photo.absUrl("src");
                if (debug) {
                    System.out.println(urlp);
                }

                getImages(urlp, title, c);
                c++;
            }
        }
    }

    private static void getImages(String src, String title, int c) throws IOException {

        //Exctract the name of the image from the src attribute
        if (debug) {
            System.out.println(src);
        }

        //Open a URL Stream
        if (src.indexOf(matchWord) >= 0) {
            try {

                //   URL url = new URL(src);
                Response resultImageResponse = Jsoup.connect(src).ignoreContentType(true).execute();

                FileOutputStream out = (new FileOutputStream(new java.io.File(FOLDERPATH + title + String.valueOf(c) + ".jpg")));
                out.write(resultImageResponse.bodyAsBytes()); // resultImageResponse.body() is where the image's contents are.
                out.close();
            }


            catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
