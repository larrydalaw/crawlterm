package one.pandaland.crawlTerm;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.ArrayList;
import java.util.List;

    @Table(name="products")
	 public class Product {
		
		// must have 0-arg constructor
		public Product() {
		    this.photoPath =  new ArrayList<>();
		} 
		
		// can also have convenience constructor
		public Product(String productTitle, String contact) {
			this.productTitle = productTitle;
			this.contact = contact;
		}
		
		// primary key, generated on the server side
		@Id
		@GeneratedValue 
		public long id;
		
		// a public property without getter or setter
		@Column(name="producttitle")  // must do this for Postgres
		public String productTitle; 
		
		// a private property with getter and setter below
		private String contact; 
		public String district ;
        public String datePosted;
        public String personContact;
        public int price  ;
        @Column 
        public List<String> photoPath;
		@Column(name="contact")  // must do this for Postgres
		public String getContact() {
			return contact;
		}
		
		public void setContact(String contact) {
			this.contact = contact;
		}

		
		public String toString() {
			return id + " " + productTitle + " " + contact;
		}
	}