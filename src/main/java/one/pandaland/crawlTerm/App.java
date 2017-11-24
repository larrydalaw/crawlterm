package one.pandaland.crawlTerm;

	import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
	import cn.edu.hfut.dmic.webcollector.model.Page;
	import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
	import org.jsoup.nodes.Document;

	public class App extends BreadthCrawler {
	    /**
	     * @param crawlPath crawlPath is the path of the directory which maintains
	     *                  information of this crawler
	     * @param autoParse if autoParse is true,BreadthCrawler will auto extract
	     *                  links which match regex rules from pag
	     */
	    public App(String crawlPath, boolean autoParse) {
	        super(crawlPath, autoParse);
	        /*start page*/
	        this.addSeed("https://hongkong.asiaxpat.com/classifieds/free/911ae6f1-a64a-4bf1-9d44-210a2b572c42/small-fridge-($100)/");

	        /*fetch url like http://news.hfut.edu.cn/show-xxxxxxhtml*/
	        this.addRegex("https://hongkong.asiaxpat.com/classifieds/free/*/*/");
	        /*do not fetch jpg|png|gif*/
	        this.addRegex("-.*\\.(png|gif).*");
	        /*do not fetch url contains #*/
	        this.addRegex("-.*#.*");

	        setThreads(50);
	        getConf().setTopN(100);

//	        setResumable(true);
	    }

	    @Override
	    public void visit(Page page, CrawlDatums next) {
	        String url = page.url();
	        /*if page is news page*/
	        if (page.matchUrl("https://hongkong.asiaxpat.com/classifieds/free/*/*/")) {

	            /*extract title and content of news by css selector*/
	            String title = page.selectText("div#h3 classified-preview-title");
	            String content = page.select("div#gallery clearfix gallery-on-details-page").;//	            <ul class="gallery clearfix gallery-on-details-page">
                

	            System.out.println("URL:\n" + url);
	            System.out.println("title:\n" + title);
	            System.out.println("content:\n" + content);

	            /*If you want to add urls to crawl,add them to nextLink*/
	            /*WebCollector automatically filters links that have been fetched before*/
	            /*If autoParse is true and the link you add to nextLinks does not match the 
	              regex rules,the link will also been filtered.*/
	            //next.add("http://xxxxxx.com");
	        }
	    }

	    public static void main(String[] args) throws Exception {
	        App crawler = new App("crawl", true);
	        /*start crawl with depth of 4*/
	        crawler.start(4);
	    }

}
