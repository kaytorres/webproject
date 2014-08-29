package sjsms.DBservice;

import java.text.SimpleDateFormat;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class SmsSendStatusManager extends JdbcDaoSupport {

	// public static ApplicationContext ac = null;
	// public static DriverManagerDataSource dataSource = null;

	public SmsSendStatusManager() {
		// ac = new
		// FileSystemXmlApplicationContext("D:/application-config.xml");
		// dataSource = (DriverManagerDataSource) ac.getBean("dataSource");
	}

	public boolean addSendStatus(SmsSendStatus smsSendStatus) {
		// JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "insert into smssendstatus( mainid,mobile,sendtype,sendcount) values (?,?,?,?)";              //@8.28
		if (jdbcTemplate.update(sql, new Object[] { smsSendStatus.getMainid(),
				smsSendStatus.getMobile() ,"0",0}) > 0)
			return true;
		else
			return false;
	}

	public boolean updateSendStatusSuc(SmsSendStatus smsSendStatus) {
		// JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "update smssendstatus set smsid=? , sendtime=?  where mainid=? and mobile=?";
		if (jdbcTemplate.update(sql, new Object[] { smsSendStatus.getSmsid(),
				sdf.format(new java.util.Date()), smsSendStatus.getMainid(),
				smsSendStatus.getMobile() }) > 0)
			return true;
		else
			return false;
	}

	public boolean updateSendStatusFail(SmsSendStatus smsSendStatus) {
		// JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "update smssendstatus set sendstatus=? where mainid=? and mobile=?";
		if (jdbcTemplate.update(
				sql,
				new Object[] { smsSendStatus.getSendstatus(),
						smsSendStatus.getMainid(), smsSendStatus.getMobile() }) > 0)
			return true;
		else
			return false;
	}

	public boolean updateSendStatus(SmsSendStatus smsSendStatus) {
		// JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "update smssendstatus set sendstatus=? where smsid=? and mobile= ?";
		if (jdbcTemplate.update(
				sql,
				new Object[] { smsSendStatus.getSendstatus(),
						smsSendStatus.getSmsid(), smsSendStatus.getMobile() }) > 0)
			return true;
		else
			return false;
	}
	public boolean urlUpdateSendStatus(SmsSendStatus smsSendStatus) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "update smssendstatus set sendstatus=? ,sendtime= ? where smsid=? and mobile= ?";
		if (jdbcTemplate.update(
				sql,
				new Object[] { smsSendStatus.getSendstatus(),smsSendStatus.getSendtime(),
						smsSendStatus.getSmsid(), smsSendStatus.getMobile() }) > 0)
			return true;
		else
			return false;
	}
	public boolean urlAddSendStatus(SmsSendStatus smsSendStatus) {
		// JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "insert into smssendstatus( smsid,mobile,sendstatus,sendtime) values (?,?,?,?)";
		if (jdbcTemplate.update(sql, new Object[] { smsSendStatus.getSmsid(),
				smsSendStatus.getMobile(),smsSendStatus.getSendstatus(),smsSendStatus.getSendtime()}) > 0)
			return true;
		else
			return false;
	}

	public boolean mesidExist(String mesid){
		//String psd = account + "_" + password;
		//psd = Utility.getMD5(psd);
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "select id from smssendstatus where smsid = \""+mesid+"\";";
		SqlRowSet sRowSet=jdbcTemplate.queryForRowSet(sql);
		if(sRowSet.next())
		{
			return true;         
		}
		else
			return false;
	}
	
	public boolean urlReSendStatus(String mainid,String mobile,String resender) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "update smssendstatus set sendcount=sendcount+1 ,sendtime= now(),resender=?,sendtype='1' where mainid=? and mobile= ?";
		if (jdbcTemplate.update(
				sql,
				new Object[] { resender,mainid,mobile}) > 0)
			return true;
		else
			return false;
	}
	public boolean ReSendStatus(int mainid) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "update smssendstatus set sendcount=sendcount+1 where mainid=?";
		if (jdbcTemplate.update(
				sql,
				new Object[] { mainid}) > 0)
			return true;
		else
			return false;
	}
}
