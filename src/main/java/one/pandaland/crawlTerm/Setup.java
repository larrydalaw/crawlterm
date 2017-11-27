package one.pandaland.crawlTerm;

public  class Setup {

public static void setSysProperties() {
		
		
		
		
        System.setProperty("norm.jdbcUrl", "jdbc:mysql://db:3306/petclinic?useSSL=false");
        System.setProperty("norm.user", "petclinic");
        System.setProperty("norm.password", "password");
		
		/*
		System.setProperty("norm.dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
		System.setProperty("norm.user", "postgres");
		System.setProperty("norm.password", "postgres");
		*/
		
	}
}

