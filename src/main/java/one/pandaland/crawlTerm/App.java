package one.pandaland.crawlTerm;

import java.io.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Connection.Response;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;





import com.dieselpoint.norm.*;
public class App {
    private static final String FOLDERPATH = "./photo/";
    public static final String  matchWord  = "ThumbnailID";
    public static boolean       debug      = true;

    public static void main(String[] args) throws IOException {
		Setup.setSysProperties();
        
        Database db = new Database();
		
		dataPrep (db); 

		
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
        List <Product> pList = new ArrayList<>();

        for (Element element : products) {
            Product p =new Product();
            Elements photos = element.select("img");
            if (debug) {
                System.out.println(photos.size());
            }
            Elements details = element.select("div.borderright");
            for (Element detail : details){
         if (debug) {
                System.out.println("wholetext:"  + detail.wholeText());
}
            String t = detail.wholeText();
            String[] tl = t.trim().split(" ");
            switch (tl[0]){
                
                case "Phone":
                    p.setContact(tl[1]);
                    break;
                case "Price":
                    if (tl[1].length()==3){
                    p.price = 0;
                }   else 
                {
                    p.price = Integer.valueOf(tl[1].substring(3));
                }
                case "Posted":
                    p.datePosted = tl[1];
                    break;
                case "District":
                    p.district = tl[1];
                    
            }
            }
            p.productTitle = element.select("h4.R a").text();
            if (debug) {
                System.out.println(p.productTitle);
            }
            int c = 0;
            for (Element photo : photos) {
                String urlp = photo.absUrl("src");
                if (debug) {
                    System.out.println(urlp);
                }
                
                String pathCreated = saveImages(urlp, p.productTitle, c);
                if (pathCreated!=null){
                    p.photoPath.add(pathCreated) ;
                    c++;
                }

            }
            pList.add(p);
        }
        db.insert(pList);
    }

    private static void dataPrep (Database db){
        		// db.setSqlMaker(new PostgresMaker()); // set this to match your sql flavor		
		
		/* test straight sql */
		db.sql("drop table if exists products").execute();
		
		/* test create table */
		db.createTable(Product.class);
		/* test inserts */
		Product john = new Product("John", "Doe");
		db.insert(john); 
		
		Product bill = new Product("Bill", "Smith");
		db.insert(bill);
		
		/* test where clause, also id and generated values */
		List<Product> list = db.where("productTitle=?", "John").results(Product.class);
		dump("john only:", list);
		
		/* test delete single record */
		db.delete(john);
		List<Product> list1 = db.orderBy("contact").results(Product.class);
		dump("bill only:", list1);
		
		/* test update single record */
		bill.productTitle = "Joe";
		int rowsAffected = db.update(bill).getRowsAffected();
		List<Product> list2 = db.results(Product.class);
		dump("bill is now joe, and rowsAffected=" + rowsAffected, list2);
		
		/* test using a primitive for results instead of a pojo */
		Long count = db.sql("select count(*) as count from products").first(Long.class);
		System.out.println("Num records (should be 1):" + count);
		
		/* test delete with where clause */
		db.table("products").where("productTitle=?", "Joe").delete();

		/* make sure the delete happened */
		count = db.sql("select count(*) as count from products").first(Long.class);
		System.out.println("Num records (should be 0):" + count);
		
		/* test transactions */
		db.insert(new Product("Fred", "Jones"));
		Transaction trans = db.startTransaction();
		db.transaction(trans).insert(new Product("Sam", "Williams"));
		db.transaction(trans).insert(new Product("George ", "Johnson"));
		trans.rollback();
		List<Product> list3 = db.results(Product.class);
		dump("fred only:", list3);
		
		
    }
    public static void dump(String label, List<Product> list) {
		System.out.println(label);
		for (Product n: list) {
			System.out.println(n.toString());
		}
	}
   
    private static String saveImages(String src, String title, int c) throws IOException {

        //Exctract the name of the image from the src attribute
        if (debug) {
            System.out.println(src);
        }

        //Open a URL Stream
        if (src.indexOf(matchWord) >= 0) {
            try {

                //   URL url = new URL(src);
                Response resultImageResponse = Jsoup.connect(src).ignoreContentType(true).execute();
                File pic  = new File(System.getProperty("user.dir") + title + String.valueOf(c) + ".jpg");
                pic.createNewFile();
                FileOutputStream out = (new FileOutputStream(pic));
                out.write(resultImageResponse.bodyAsBytes()); // resultImageResponse.body() is where the image's contents are.
                out.close();
                
                return pic.getAbsolutePath();
                
                }


            catch (IOException e) {
                e.printStackTrace();
                
                return null;
            }
            
        }
        return null;
    }
    
}
