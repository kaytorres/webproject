package sjsms.DBservice;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import sjsms.service.Utility;

public class ApplicationInfoManager extends JdbcDaoSupport {

	/*public static ApplicationContext ac = null;
	public static DriverManagerDataSource dataSource = null;*/

	public ApplicationInfoManager() {
		/*ac = new FileSystemXmlApplicationContext("D:/application-config.xml");
		dataSource = (DriverManagerDataSource) ac.getBean("dataSource");*/
	}
	/*public static void main(String[] args) {
		
		ApplicationInfoManager applicationInfoManager=new ApplicationInfoManager();
		applicationInfoManager.updateApp("1", "web", "webapp", "123456", "1");
	}*/
	public boolean appLogin(ApplicationInfo applicationInfo){
	/*	String psd = applicationInfo.getAppid() + "_" +applicationInfo.getCaptcha();
		psd = Utility.getMD5(psd);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
*/		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "select appname from applicationinfo where appid = \""+applicationInfo.getAppid()+"\""+" and captcha= \""+applicationInfo.getCaptcha()+"\";";
		SqlRowSet sRowSet=jdbcTemplate.queryForRowSet(sql);
		if(sRowSet.next())
		{
			return true;         
		}
		else
			return false;
	}
	
	public List<ApplicationInfo> queryAppid(String appname){
		List<ApplicationInfo> stlist = null;
		if(!appname.isEmpty()){
			String sql = "select * from applicationinfo where appname = \""+appname+"\";";
			JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
			stlist = jdbcTemplate.query(sql, new RowMapper<ApplicationInfo>() {
				public ApplicationInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
					return new ApplicationInfo(rs.getString("appid"),
							rs.getString("appname"),
							rs.getString("externo"),
							rs.getString("captcha"),
							rs.getString("needresend")
							);
				}
			});
		}
		return stlist;	
	}
	public List<ApplicationInfo> queryAppname(String appid){
		List<ApplicationInfo> stlist = null;
		if(!appid.isEmpty()){
			String sql = "select * from applicationinfo where appid = \""+appid+"\";";
			JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
			stlist = jdbcTemplate.query(sql, new RowMapper<ApplicationInfo>() {
				public ApplicationInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
					return new ApplicationInfo(rs.getString("appid"),
							rs.getString("appname"),
							rs.getString("externo"),
							rs.getString("captcha"),
							rs.getString("needresend")
							);
				}
			});
		}
		return stlist;	
	}
	
	public List<ApplicationInfo> queryApplicationInfo(){
		List<ApplicationInfo> stlist = null;
		String sql = "select distinct appname from applicationinfo ";
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		stlist = jdbcTemplate.query(sql, new RowMapper<ApplicationInfo>() {
			public ApplicationInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new ApplicationInfo("",
						rs.getString("appname"),
						"",
						"",
						""
						);
			}
		});
		return stlist;	
	}
	public boolean appLoginOut(ApplicationInfo applicationInfo){
		//JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		 JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "select appname from applicationinfo where appid = \""+applicationInfo.getAppid()+"\";";
		SqlRowSet sRowSet=jdbcTemplate.queryForRowSet(sql);
		if(sRowSet.next())
		{
			return true;         
		}
		else
			return false;
	}
	
	
	public int queryAppSize() {
		String sql = "select count(*)  from applicationinfo ";
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		int size = jdbcTemplate.queryForInt(sql);
		return size;
	}
	public List<ApplicationInfo> queryApp(int index, int pagesize){
		List<ApplicationInfo> stlist = null;
		String sql = "select appid,appname,externo,captcha,needresend from applicationinfo where appid is not null  ";
		List<Object> conditions = new ArrayList<Object>();
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		sql = sql + " order by appid ASC limit ?,? ";
		conditions.add(index);
		conditions.add(pagesize);
		stlist = jdbcTemplate.query(sql, conditions.toArray(), new RowMapper<ApplicationInfo>() {
			public ApplicationInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new ApplicationInfo(rs.getString("appid"),
						rs.getString("appname"),
						rs.getString("externo"),
						rs.getString("captcha"),
						rs.getString("needresend")
						);
			}
		});

		return stlist;	
	}
	
	public boolean appExist(String appid){
		//String psd = account + "_" + password;
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "select appid from applicationinfo where appid = \""+appid+"\";";
		SqlRowSet sRowSet=jdbcTemplate.queryForRowSet(sql);
		if(sRowSet.next())
		{
			return true;         
		}
		else
			return false;
	}
	
	public boolean delApp(String appid){
		String sql = "delete from applicationinfo where appid = ?";
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		if(jdbcTemplate.update(sql, appid)>0)
			return true;
		else
			return false;	
	}
	

	public boolean updateApp(String appid,String appname,String externo,String captcha,String needresend,String oldappid) {
		/*String psd = appid + "_" + captcha;
		psd = Utility.getMD5(psd);
		 JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);*/
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql=null;
		if("-1".equals(needresend)){
			sql = "update applicationinfo set appid=? ,appname=? ,externo=? ,captcha=?  where appid=? ";
			if (jdbcTemplate.update(
					sql,appid,appname,externo,captcha,oldappid) > 0)
				return true;
			else
				return false;
		}else {
			sql = "update applicationinfo set appid=? ,appname=? ,externo=? ,captcha=?,needresend=?   where appid=? ";
			if (jdbcTemplate.update(
					sql,appid,appname,externo,captcha,needresend ,oldappid) > 0)
				return true;
			else
				return false;
		}
		
	}

	public boolean updateAppNonePwd(String appid,String appname,String externo,String needresend,String oldappid) {
		/*String psd = appid + "_" + captcha;
		psd = Utility.getMD5(psd);
		 JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);*/
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql=null;
		if("-1".equals(needresend)){
			sql = "update applicationinfo set appid=? ,appname=? ,externo=?  where appid=? ";
			if (jdbcTemplate.update(
					sql,appid,appname,externo,oldappid) > 0)
				return true;
			else
				return false;
		}else {
			sql = "update applicationinfo set appid=? ,appname=? ,externo=? ,needresend=? where appid=? ";
			if (jdbcTemplate.update(
					sql,appid,appname,externo,needresend,oldappid) > 0)
				return true;
			else
				return false;
		}
		
		
	}
	public boolean addApp(String appid,String appname,String externo,String captcha,String needresend){
	/*	String psd = appid + "_" + captcha;
		psd = Utility.getMD5(psd);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);*/
		String sql = "insert into applicationinfo( appid,appname,externo,captcha,needresend) values (?,?,?,?,?)";
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		if (jdbcTemplate.update(sql, appid,appname,externo,captcha,needresend) > 0)
			return true;
		else
			return false;
	}
}
