package sjsms.DBservice;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import sjsms.service.Encrypt;

public class AccountInfoManager extends JdbcDaoSupport {

	//public static ApplicationContext ac = null;
	//public static DriverManagerDataSource dataSource = null;

	public AccountInfoManager() {
	//	ac = new FileSystemXmlApplicationContext("D:/application-config.xml");
	//	dataSource = (DriverManagerDataSource) ac.getBean("dataSource");
	}

	public boolean addAccount(AccountInfo accountinfo) throws Exception {
		//JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		Encrypt encrypt=new Encrypt();
		String pwd3desString=encrypt.encrypt(accountinfo.getPasswordfor3des());
		 JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "insert into accoutinfo( name,url,username,password,balance,passwordfor3des) values (?,?,?,?,?,?)";

		if (jdbcTemplate.update(sql, new Object[] { accountinfo.getName(),
				accountinfo.getUrl(), accountinfo.getUsername(), accountinfo.getPassword(),
				accountinfo.getBalance(), pwd3desString }) > 0)
			return true;
		else
			return false;
	}
	
	public boolean updateAccount(String account,String url,String username,String password,String passwordfor3des,String oldaccountname){
		//JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		//Encrypt encrypt=new Encrypt();
		//String pwd3desString=encrypt.encrypt(accountinfo.getPasswordfor3des());
		 JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "update accoutinfo set name=? ,url=? ,username=? ,password=? , passwordfor3des=? where name=?";
	//	String psd = accountinfo.getName() + "_" + accountinfo.getPassword();
	//	psd = Utility.getMD5(psd);
		if (jdbcTemplate.update(sql, account,url,username,password,passwordfor3des,oldaccountname) > 0){
			return true;
		}else
			return false;
	}
	
	public boolean accountExist(AccountInfo accountinfo) {
		//JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "select name from accoutinfo where name = \""+accountinfo.getName()+"\";";
		SqlRowSet sRowSet=jdbcTemplate.queryForRowSet(sql);
		if(sRowSet.next()){
			return true;
		}
		else {
			return false;
		}
	}
	public boolean urlccountExist(String receiver) {
		//JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "select name from accoutinfo where username = \""+receiver+"\";";
		SqlRowSet sRowSet=jdbcTemplate.queryForRowSet(sql);
		if(sRowSet.next()){
			return true;
		}
		else {
			return false;
		}
	}
	
	public List<AccountInfo> queryAccount(){
		//JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		 JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
			List<AccountInfo> stlist = null;
			String sql = "select name,url,username,password,balance,passwordfor3des from accoutinfo ";
			stlist = jdbcTemplate.query(sql, new RowMapper<AccountInfo>() {
				public AccountInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
					return new AccountInfo(rs.getString("name"),
							rs.getString("url"),
							rs.getString("username"),
							rs.getString("password"),
							rs.getFloat("balance"),
							rs.getString("passwordfor3des")
							);
				}
			});
			return stlist;
	}
	
	public boolean updateBanlance(AccountInfo accountinfo){
		//JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		 JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "update accoutinfo set balance=? where name= ?";
		if (jdbcTemplate.update(sql, new Object[] { accountinfo.getBalance(),accountinfo.getName()}) > 0)
			return true;
		else
			return false;
		
		
	}
}
