package sjsms.DBservice;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DBConnectionTest {

	/**
	 * @param args
	 */
	
	ApplicationContext ac = null;
	private static DBConnectionTest dbconn = null;
	static DriverManagerDataSource dataSource = null;

	public void setDatasource(DriverManagerDataSource dataSource) {
		this.dataSource = dataSource;
	}
	public static DBConnectionTest getInstance() {
		if (dbconn == null) {
			dbconn = new DBConnectionTest();
		}
		return dbconn;
	}

	public DriverManagerDataSource getDataSource() {
		return dataSource;
	}

	public DBConnectionTest() {

		// TODO Auto-generated constructor stub
		// ApplicationContext
		ac = new FileSystemXmlApplicationContext("D:/application-config.xml");
		dataSource = (DriverManagerDataSource) ac.getBean("dataSource");

		// dbconn = this;
	}

	public void test() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		System.out.println("Creating tables");
		// jdbcTemplate.execute("drop table customers if exists");
		jdbcTemplate
				.execute("create table customers("
						+ "id serial, first_name varchar(255), last_name varchar(255))"); 

		String[] names = "John Woo;Jeff Dean;Josh Bloch;Josh Long".split(";");
		for (String fullname : names) {
			String[] name = fullname.split(" ");
			System.out.printf("Inserting customer record for %s %s\n", name[0],
					name[1]);
			jdbcTemplate.update(
					"INSERT INTO customers(first_name,last_name) values(?,?)",
					name[0], name[1]);
		}

	}


	
/*	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBConnectionTest conn=DBConnectionTest.getInstance();
		conn.setDatasource(dataSource);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update("insert into smsrecv( mobile,receiver,content,sendtime,recvtime) values (?,?,?,?,?)","1","2","3","4","5");
		
	}*/
}
