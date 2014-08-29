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



public class SensitiveWordManager extends JdbcDaoSupport{
	//public static ApplicationContext ac = null;
	//public static DriverManagerDataSource dataSource = null;
	
	public SensitiveWordManager(){
	//	ac = new FileSystemXmlApplicationContext("D:/application-config.xml");
	//	dataSource = (DriverManagerDataSource) ac.getBean("dataSource");
	}
	
	public boolean senWordExist(String word){
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "select id from sensitiveword where word = \""+word+"\";";
		SqlRowSet sRowSet=jdbcTemplate.queryForRowSet(sql);
		if(sRowSet.next())
		{
			return true;         
		}
		else
			return false;
	}
	public boolean addSensitiveWord(String word){
		String sql = "insert into sensitiveword(word) values (?)";
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		if (jdbcTemplate.update(sql, word) > 0)
			return true;
		else
			return false;
	}
	
	public List<SensitiveWord> querySenWord(int id){
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		//JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<SensitiveWord> stlist = null;
		String sql = "select id,word from sensitiveword where id =  \" "+id+"\";";
		
		stlist = jdbcTemplate.query(sql, new RowMapper<SensitiveWord>() {
			public SensitiveWord mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new SensitiveWord(rs.getInt("id"),
						rs.getString("word")
						);
			}
		});

		return stlist;
		
	}
	public List<SensitiveWord> queryUser(){
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		//JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<SensitiveWord> stlist = null;
		String sql = "select id,word from sensitiveword";
		
		stlist = jdbcTemplate.query(sql, new RowMapper<SensitiveWord>() {
			public SensitiveWord mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new SensitiveWord(rs.getInt("id"),
						rs.getString("word")
						);
			}
		});

		return stlist;
		
	}
	public boolean delSensitiveWord(int id){
		String sql = "delete from sensitiveword where id = ?";
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		if(jdbcTemplate.update(sql, id)>0)
			return true;
		else
			return false;	
	}
	public boolean updateSenWord(int id,String word,int oldid) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "update sensitiveword set id=? ,word=?  where id=? ";
		if (jdbcTemplate.update(
				sql,id,word,oldid) > 0)
			return true;
		else
			return false;
	}
	public int querySenWordSize() {
		String sql = "select count(*)  from sensitiveword ";
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		int size = jdbcTemplate.queryForInt(sql);
		return size;
	}
	
	public List<SensitiveWord> querySenWord(int index, int pagesize){
		List<SensitiveWord> stlist = null;
		String sql = "select id,word from sensitiveword where id is not null  ";
		List<Object> conditions = new ArrayList<Object>();
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		sql = sql + " order by id ASC limit ?,? ";
		conditions.add(index);
		conditions.add(pagesize);
		stlist = jdbcTemplate.query(sql, conditions.toArray(), new RowMapper<SensitiveWord>() {
			public SensitiveWord mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new SensitiveWord(rs.getInt("id"),
						rs.getString("word")
						);
			}
		});

		return stlist;	
	}
	
}
