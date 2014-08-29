package sjsms.DBservice;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.RowSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import sjsms.service.Utility;

public class SystemUserManager extends JdbcDaoSupport{
	/*public static ApplicationContext ac = null;
		public static DriverManagerDataSource dataSource = null;*/
	public SystemUserManager(){
			/*ac = new FileSystemXmlApplicationContext("D:/application-config.xml");
			dataSource = (DriverManagerDataSource) ac.getBean("dataSource");*/
	}
	
	/*public static void main(String[] args) {
		
		SystemUserManager systemUserManager=new SystemUserManager();
		if(systemUserManager.updateUser("test", "测试账号", "123456", "02", "test")){
			System.err.println("----------------------------");
		}
		
		//systemUserManager.addUser("gechao", "葛超", "123456", "01");
	}*/
	public boolean addUser(String account,String name,String password,String roleid){
		String sql = "insert into systemuser( account,name,password,roleid) values (?,?,?,?)";
		String psd = account + "_" + password;
		psd = Utility.getMD5(psd);
		//JdbcTemplate jdbcTemplate=new JdbcTemplate(dataSource);
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		if (jdbcTemplate.update(sql, account,name,psd,roleid) > 0)
			return true;
		else
			return false;
	}
	

	
	public List<User> queryName(String account){
		List<User> stlist = null;
		if(!account.isEmpty()){
			String sql = "select account,name,password,roleid from systemuser where account=\""+account+"\"";
			JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
			stlist = jdbcTemplate.query(sql, new RowMapper<User>() {
				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
					return new User(rs.getString("account"),
							rs.getString("name"),
							rs.getString("password"),
							rs.getString("roleid")
							);
				}
			});
		}
		return stlist;	
	}
	
	public boolean login(String account,String password){
		String psd = account + "_" + password;
		psd = Utility.getMD5(psd);
		//JdbcTemplate jdbcTemplate=new JdbcTemplate(dataSource);
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "select name from systemuser where account = \""+account+"\""+" and password= \""+psd+"\";";
		SqlRowSet sRowSet=jdbcTemplate.queryForRowSet(sql);
		if(sRowSet.next())
		{
			return true;         
		}
		else
			return false;
	}
	public boolean userExist(String account){
		//String psd = account + "_" + password;
		//psd = Utility.getMD5(psd);
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "select name from systemuser where account = \""+account+"\";";
		SqlRowSet sRowSet=jdbcTemplate.queryForRowSet(sql);
		if(sRowSet.next())
		{
			return true;         
		}
		else
			return false;
	}
	
	public boolean updateUser(String account,String name,String password,String roleid,String oldaccount) {
		 //JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String psd = account + "_" + password;
	    psd = Utility.getMD5(psd);
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "update systemuser set account=? ,name=? ,password=? ,roleid=?  where account=? ";
		if (jdbcTemplate.update(
				sql,account,name,psd,roleid,oldaccount) > 0)
			return true;
		else
			return false;
	}
	public boolean updateUserNonePwd(String account,String name,String roleid,String oldaccount) {
		 //JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "update systemuser set account=? ,name=? ,roleid=?  where account=? ";
		if (jdbcTemplate.update(
				sql,account,name,roleid,oldaccount) > 0)
			return true;
		else
			return false;
	}
	
	public int queryUserSize() {
		String sql = "select count(*)  from systemuser ";
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		int size = jdbcTemplate.queryForInt(sql);
		return size;
	}
	
	public List<User> queryUser(int index, int pagesize){
		List<User> stlist = null;
		String sql = "select account,name,password,roleid from systemuser where account is not null  ";
		List<Object> conditions = new ArrayList<Object>();
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		sql = sql + " order by account desc limit ?,? ";
		conditions.add(index);
		conditions.add(pagesize);
		stlist = jdbcTemplate.query(sql, conditions.toArray(), new RowMapper<User>() {
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new User(rs.getString("account"),
						rs.getString("name"),
						rs.getString("password"),
						rs.getString("roleid")
						);
			}
		});

		return stlist;	
	}
	
	public boolean delUser(String account){
		String sql = "delete from systemuser where account = ?";
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		if(jdbcTemplate.update(sql, account)>0)
			return true;
		else
			return false;	
	}
}
