package sjsms.DBservice;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import sjsms.service.Utility;

public class SyslogManager extends JdbcDaoSupport {
	public SyslogManager(){
		
	}

	public void addSyslog(String operator,String action,String content){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String operatetime =sdf.format(new java.util.Date());
		String sql="insert into syslog(operator,action,content,operatetime) values(?,?,?,?)";
		JdbcTemplate jdbcTemplate=this.getJdbcTemplate();
		jdbcTemplate.update(sql,operator,action,content,operatetime);
	}
	
/*	public List<Syslog> querySyslog(){
		List<Syslog> stlist = null;
		String sql = "select id,operator,action,content,operatetime from syslog ";
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		stlist = jdbcTemplate.query(sql, new RowMapper<Syslog>() {
			public Syslog mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Syslog(rs.getInt("id"),
						rs.getString("operator"),
						rs.getString("action"),
						rs.getString("content"),
						rs.getString("operatetime")
						);
			}
		});
		return stlist;
	}*/
	
	//查询syslog长度，用于分页显示
	public int querySyslogSize(String action, String operator,
			String starttime, String endtime) {
		String sql = "select count(*)  from syslog "
				+ " where content is not null ";

		List<Object> conditions = new ArrayList<Object>();
		if (!action.equalsIgnoreCase("-1")) {
			sql = sql + "and action = ? ";
			conditions.add(action);
		}
		if (!operator.equalsIgnoreCase("-1")) {
			sql = sql + "and operator = ? ";
			conditions.add(operator);
		}
		if (!starttime.isEmpty() && !endtime.isEmpty()) {
			sql = sql + "and operatetime  between ? and ? ";
			starttime = starttime + " 00:00:00";
			endtime = endtime + " 23:59:59";
			conditions.add(starttime);
			conditions.add(endtime);
		}
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		int size = jdbcTemplate.queryForInt(sql, conditions.toArray());
		return size;
	}
		
		//按条件查询日志:按操作码、操作人、操作时间
		//没有条件时，按照optime升序排序
	public List<Syslog> querySyslog(String action, String operator,
			String starttime, String endtime, int index, int pagesize) {
		List<Syslog> lst = null;
		String sql = "select id,operator,action,content,operatetime from syslog where content is not null ";
		List<Object> conditions = new ArrayList<Object>();
		if (!operator.equalsIgnoreCase("-1")) {
			sql = sql + "and operator = ? ";
			conditions.add(operator);
		}
		if (!action.equalsIgnoreCase("-1")) {
			sql = sql + "and action = ? ";
			conditions.add(action);
		}
		if (!starttime.isEmpty() && !endtime.isEmpty()) {
			sql = sql + "and operatetime between ? and ? ";
			starttime = starttime + " 00:00:00";
			endtime = endtime + " 23:59:59";
			conditions.add(starttime);
			conditions.add(endtime);
		}
		sql = sql + " order by operatetime desc limit ?,? ";
		conditions.add(index);
		conditions.add(pagesize);

		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		lst = jdbcTemplate.query(sql, conditions.toArray(),
				new RowMapper<Syslog>() {

					public Syslog mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						return new Syslog(rs.getInt("id"),

						rs.getString("operator"), rs.getString("action"), rs
								.getString("content"), rs
								.getString("operatetime"));
					}
				});
		return lst;
	}
	
	
	public List<Syslog> queryOperater(){
		List<Syslog> stlist = null;
		String sql = "select distinct operator from syslog ";
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		stlist = jdbcTemplate.query(sql, new RowMapper<Syslog>() {
			public Syslog mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Syslog(0,
						rs.getString("operator"),
						"",
						"",
						""
						);
			}
		});
		return stlist;
	}
	
	public boolean delSyslog(int id){
		String sql = "delete from syslog where id = ?";
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		if(jdbcTemplate.update(sql, id)>0)
			return true;
		else
			return false;	
	}
}
