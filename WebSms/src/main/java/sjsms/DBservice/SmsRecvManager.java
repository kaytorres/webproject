package sjsms.DBservice;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion.Static;

public class SmsRecvManager extends JdbcDaoSupport {
//	public static ApplicationContext ac = null;
	//public static DriverManagerDataSource dataSource = null;

	public SmsRecvManager() {
		//ac = new FileSystemXmlApplicationContext("D:/application-config.xml");
		//dataSource = (DriverManagerDataSource) ac.getBean("dataSource");
	}

	
	//receiverΪappid
	public boolean addSmsRecv(SmsRecv smsrecv) {
		String sql = "insert into smsrecv( mobile,receiver,content,sendtime,recvtime) values (?,?,?,?,?)";
		//JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
	    JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		if (jdbcTemplate.update(
				sql,
				new Object[] { smsrecv.getMobile(), smsrecv.getReceiver(),
						smsrecv.getContent(), smsrecv.getSendtime(),
						smsrecv.getRecvtime() }) > 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/*public boolean test(String mobile, String receiver, String content) {
		String sql = "insert into smsrecv( mobile,receiver,content) values (?,?,?)";
	//	JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		if (jdbcTemplate.update(sql,mobile, receiver,content) > 0) {
			return true;
		}
		else {
			return false;
		}
	}*/
	
	public List<SmsRecv> querySmsRecv(SmsRecv smsrecv){
		//JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		 JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		 List<SmsRecv> stlist = null;
		 String sql = "select  id,mobile,receiver,content,sendtime,recvtime from smsrecv where receiver = \" "+smsrecv.getReceiver()+"\";";
		 stlist = jdbcTemplate.query(sql, new RowMapper<SmsRecv>() {
				public SmsRecv mapRow(ResultSet rs, int rowNum) throws SQLException {
					return new SmsRecv(rs.getInt("id"),
							rs.getString("mobile"),
							rs.getString("receiver"),
							rs.getString("content"),
							rs.getString("sendtime"),
							rs.getString("recvtime")
							);
				}
			});
			return stlist;
	}


		public int queryRecHisSize(String mobile,String starttime, String endtime) {
			String sql = "select count(*)  from smsrecv where id is not null ";

			List<Object> conditions = new ArrayList<Object>();
			if (!mobile.equalsIgnoreCase("")) {
				sql = sql + "and mobile = ? ";
				conditions.add(mobile);
			}
			if (!starttime.isEmpty() && !endtime.isEmpty()) {
				sql = sql + "and sendtime  between ? and ? ";
				starttime = starttime + " 00:00:00";
				endtime = endtime + " 23:59:59";
				conditions.add(starttime);
				conditions.add(endtime);
			}
			JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
			int size = jdbcTemplate.queryForInt(sql, conditions.toArray());
			return size;
		}
			

		public List<SmsRecv> queryRecHis(String mobile,String starttime, String endtime, int index, int pagesize) {
			List<SmsRecv> lst = null;
			String sql = "select id,mobile,receiver,content,sendtime,recvtime from smsrecv where id is not null ";
			List<Object> conditions = new ArrayList<Object>();

			if (!mobile.equalsIgnoreCase("")) {
				sql = sql + "and mobile = ? ";
				conditions.add(mobile);
			}
			if (!starttime.isEmpty() && !endtime.isEmpty()) {
				sql = sql + "and sendtime between ? and ? ";
				starttime = starttime + " 00:00:00";
				endtime = endtime + " 23:59:59";
				conditions.add(starttime);
				conditions.add(endtime);
			}
			sql = sql + " order by sendtime desc limit ?,? ";
			conditions.add(index);
			conditions.add(pagesize);

			JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
			lst = jdbcTemplate.query(sql, conditions.toArray(),
					new RowMapper<SmsRecv>() {
						public SmsRecv mapRow(ResultSet rs, int rowNum)
								throws SQLException {
							return new SmsRecv(
									rs.getInt("id"),
									rs.getString("mobile"),
									rs.getString("receiver"), 
									rs.getString("content"),
									rs.getString("sendtime"),
									rs.getString("recvtime")
									);
						}
					});
			return lst;
		}
		

}
