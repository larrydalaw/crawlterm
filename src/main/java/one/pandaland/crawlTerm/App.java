package one.pandaland.crawlTerm;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
	import java.io.IOException;

	public class App {
	    /**
	     * @param crawlPath crawlPath is the path of the directory which maintains
	     *                  information of this crawler
	     * @param autoParse if autoParse is true,BreadthCrawler will auto extract
	     *                  links which match regex rules from pag
	     */
	public static void main(String[] args) throws IOException {
		Document d=Jsoup.connect("https://hongkong.asiaxpat.com/classifieds/free/").userAgent("Mozilla").timeout(6000).get();
		Elements ele=d.getElementsByClass("div.classified-body"); //<div class="classified-body listitem classified-summary 1">

		for (Element element : ele) {
			String img_url=element.select("a.fancybox img").attr("src");
			System.out.println(img_url);
			
			String title=element.select("h4.R a").text();
			System.out.println(title);
		}
	}

}
