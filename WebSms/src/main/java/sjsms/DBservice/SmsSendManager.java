package sjsms.DBservice;

import org.apache.xml.resolver.apps.resolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

//import com.mysql.jdbc.PreparedStatement;

public class SmsSendManager extends JdbcDaoSupport {

	// public static ApplicationContext ac = null;
	// public static DriverManagerDataSource dataSource = null;

	public SmsSendManager() {
		// ac = new
		// FileSystemXmlApplicationContext("D:/application-config.xml");
		// dataSource = (DriverManagerDataSource) ac.getBean("dataSource");
	}

	public List<SmsSend> querySmsSend(int id) {
		// JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		List<SmsSend> stlist = null;
		String sql = "select id,appid,smstype, mobile,sender, content, islongsms,longsmsid, smsid, sendingstatus,sendingtime,receiverinfo from smssend where id = "
				+ id + ";";
		stlist = jdbcTemplate.query(sql, new RowMapper<SmsSend>() {
			public SmsSend mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new SmsSend(rs.getInt("id"), rs.getString("appid"), rs
						.getString("smstype"), rs.getString("mobile"), rs
						.getString("sender"), rs.getString("content"), rs
						.getString("islongsms"), rs.getString("longsmsid"), rs
						.getString("smsid"), rs.getString("sendingstatus"), rs
						.getString("sendingtime"), rs.getString("receiverinfo"));
			}
		});
		return stlist;

	}

	public List<SmsSend> reQuerySmsSend() {
		// JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		List<SmsSend> stlist = null;
		String sql = "select smssend.id,smssend.appid,smstype, smssend.mobile,sender, content, islongsms,longsmsid, smssend.smsid, sendingstatus,sendingtime,receiverinfo " +
				"from smssend,applicationinfo,smssendstatus where smssend.id= smssendstatus.mainid and smssend.appid=applicationinfo.appid" +
				" and needresend='1' and sendingstatus<>'03' and sendtype<>'1' and to_days(now())-to_days(sendingtime)<1" +
				" order by sendingtime desc limit 50;";                //@8.28
		stlist = jdbcTemplate.query(sql, new RowMapper<SmsSend>() {
			public SmsSend mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new SmsSend(rs.getInt("id"), rs.getString("appid"), rs
						.getString("smstype"), rs.getString("mobile"), rs
						.getString("sender"), rs.getString("content"), rs
						.getString("islongsms"), rs.getString("longsmsid"), rs
						.getString("smsid"), rs.getString("sendingstatus"), rs
						.getString("sendingtime"), rs.getString("receiverinfo"));
			}
		});
		return stlist;

	}

	public List<SmsSend> querySmsSendStatus(SmsSend smsSend) {
		// JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		List<SmsSend> stlist = null;
		String sql = "select id,appid,smstype, mobile,sender, content, islongsms,longsmsid, smsid, sendingstatus,sendingtime,receiverinfo "
				+ "from smssend where appid = \""
				+ smsSend.getAppid()
				+ "\""
				+ " and id= " + smsSend.getId() + ";";
		stlist = jdbcTemplate.query(sql, new RowMapper<SmsSend>() {
			public SmsSend mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new SmsSend(rs.getInt("id"), rs.getString("appid"), rs
						.getString("smstype"), rs.getString("mobile"), rs
						.getString("sender"), rs.getString("content"), rs
						.getString("islongsms"), rs.getString("longsmsid"), rs
						.getString("smsid"), rs.getString("sendingstatus"), rs
						.getString("sendingtime"), rs.getString("receiverinfo"));
			}
		});
		return stlist;

	}

	public int addSmsSend(final SmsSend smsSend) {
		// final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		final JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		final String sql = "insert into smssend( appid,smstype, mobile,sender, content, islongsms,longsmsid, smsid, sendingstatus,sendingtime,receiverinfo) values (?,?,?,?,?,?,?,?,?,?,?)";
		KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public java.sql.PreparedStatement createPreparedStatement(
					Connection con) throws SQLException {
				java.sql.PreparedStatement ps = jdbcTemplate
						.getDataSource()
						.getConnection()
						.prepareStatement(
								sql,
								new String[] { "appid", "smstype", "mobile",
										"sender", "content", "islongsms",
										"longsmsid", "smsid", "sendingstatus",
										"sendingtime", "receiverinfo" });
				ps.setString(1, smsSend.getAppid());
				ps.setString(2, smsSend.getSmstype());
				ps.setString(3, smsSend.getMobile());
				ps.setString(4, smsSend.getSender());
				ps.setString(5, smsSend.getContent());
				ps.setString(6, smsSend.getIslongsms());
				ps.setString(7, smsSend.getLongsmsid());
				ps.setString(8, smsSend.getSmsid());
				ps.setString(9, smsSend.getSendingstatus());
				ps.setString(10, smsSend.getSendingtime());
				ps.setString(11, smsSend.getReceiverinfo());
				return ps;
			}
		}, holder);
		return holder.getKey().intValue();
	}

	public boolean updateFailSmsStatus(SmsSend smsSend) {
		// JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "update smssend set Sendingstatus=?  where id=? ";
		jdbcTemplate.update(sql, new Object[] { smsSend.getSendingstatus(),
				smsSend.getId() });
		return true;
	}

	public int querySendHisSize(String appname, String mobile,
			String starttime, String endtime, String sender, String sendstatus,
			String sendingstatus) {
		String sql = "select count(*) from smssend,smssendstatus,applicationinfo "
				+ "  where smssend.id =smssendstatus.mainid and applicationinfo.appid= smssend.appid  "
				+ "and appname is not null ";

		List<Object> conditions = new ArrayList<Object>();
		if (!appname.equalsIgnoreCase("-1")) {
			sql = sql + "and appname = ? ";
			conditions.add(appname);
		}
		if (!mobile.equalsIgnoreCase("")) {
			sql = sql + "and smssendstatus.mobile = ? ";
			conditions.add(mobile);
		}
		if (!sender.equalsIgnoreCase("")) {
			sql = sql + "and sender = ? ";
			conditions.add(sender);
		}
		if (!sendstatus.equalsIgnoreCase("-1")) {
			if (sendstatus.equals("2")) {
				sql = sql + "and sendstatus is null ";
			} else {
				sql = sql + "and sendstatus = ? ";
				conditions.add(sendstatus);
			}
		}
		if (!sendingstatus.equalsIgnoreCase("-1")) {
			sql = sql + "and sendingstatus = ? ";
			conditions.add(sendingstatus);
		}
		if (!starttime.isEmpty() && !endtime.isEmpty()) {
			sql = sql + "and sendingtime  between ? and ? ";
			starttime = starttime + " 00:00:00";
			endtime = endtime + " 23:59:59";
			conditions.add(starttime);
			conditions.add(endtime);
		}

		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		int size = jdbcTemplate.queryForInt(sql, conditions.toArray());
		return size;
	}

	public List<SendHistory> querySendHis(String appname, String mobile,
			String starttime, String endtime, String sender, String sendstatus,
			String sendingstatus, int index, int pagesize) {
		List<SendHistory> lst = null;
		String sql = "select mainid,applicationinfo.appname,smssendstatus.mobile,smssend.sendingtime,smssendstatus.sendstatus,smssend.sendingstatus,"
				+ "smssend.content,smssend.receiverinfo,smssend.sender,sendcount, sendtype,resender from smssend,smssendstatus,applicationinfo"
				+ "  where smssend.id =smssendstatus.mainid and applicationinfo.appid= smssend.appid "
				+ "and appname is not null ";
		List<Object> conditions = new ArrayList<Object>();
		if (!appname.equalsIgnoreCase("-1")) {
			sql = sql + "and appname = ? ";
			conditions.add(appname);
		}
		if (!mobile.equalsIgnoreCase("")) {
			sql = sql + "and smssendstatus.mobile = ? ";
			conditions.add(mobile);
		}
		if (!sender.equalsIgnoreCase("")) {
			sql = sql + "and sender = ? ";
			conditions.add(sender);
		}
		if (!sendstatus.equalsIgnoreCase("-1")) {
			if (sendstatus.equals("2")) {
				sql = sql + "and sendstatus is null ";
			} else {
				sql = sql + "and sendstatus = ? ";
				conditions.add(sendstatus);
			}
		}
		if (!sendingstatus.equalsIgnoreCase("-1")) {
			sql = sql + "and sendingstatus = ? ";
			conditions.add(sendingstatus);
		}
		if (!starttime.isEmpty() && !endtime.isEmpty()) {
			sql = sql + "and sendingtime  between ? and ? ";
			starttime = starttime + " 00:00:00";
			endtime = endtime + " 23:59:59";
			conditions.add(starttime);
			conditions.add(endtime);
		}

		sql = sql + " order by sendingtime desc limit ?,? ";
		conditions.add(index);
		conditions.add(pagesize);

		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		lst = jdbcTemplate.query(sql, conditions.toArray(),
		new RowMapper<SendHistory>() {

			public SendHistory mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				return new SendHistory(
						rs.getInt("mainid"),
						rs.getString("appname"),
						rs.getString("mobile"),
						rs.getString("sender"),
						rs.getString("receiverinfo"),
						rs.getString("sendingtime"),
						rs.getString("sendingstatus"),
						rs.getString("sendstatus"),
						rs.getInt("sendcount"),
						rs.getString("sendtype"),
						rs.getString("resender"),
						rs.getString("content"));
			}
		});
		/*lst = jdbcTemplate.query(sql, conditions.toArray(),
				new RowMapper<SmsSend>() {

					public SmsSend mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						return new SmsSend(0, rs.getString("appname"), "", rs
								.getString("mobile"),
								rs.getString("sender") == null ? "" : rs
										.getString("sender"), rs
										.getString("content"), "", "", rs
										.getString("sendstatus") == null ? "2"
										: rs.getString("sendstatus"), rs
										.getString("sendingstatus"), rs
										.getString("sendingtime"), rs
										.getString("receiverinfo") == null ? ""
										: rs.getString("receiverinfo"));
					}
				});*/
		return lst;
	}

	public boolean updateSucSmsStatus(SmsSend smsSend) {
		// JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "update smssend set Sendingstatus=? ,smsid=?  where id=? ";
		jdbcTemplate.update(sql, new Object[] { smsSend.getSendingstatus(),
				smsSend.getSmsid(), smsSend.getId() });
		return true;
	}

	public List<SmsSend> smssendExist(SmsSend smsSend) {
		List<SmsSend> stlist = null;
		// JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		String sql = "select id,sendingtime  from smssend where mobile = \""
				+ smsSend.getMobile() + "\" and content=\""
				+ smsSend.getContent() + "\"";
		stlist = jdbcTemplate.query(sql, new RowMapper<SmsSend>() {
			public SmsSend mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new SmsSend(rs.getInt("id"), "", "", "", "", "", "", "",
						"", "", rs.getString("sendingtime"), "");
			}
		});
		return stlist;
	}

	public List<SmsSend> querySender() {
		// JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		List<SmsSend> stlist = null;
		String sql = "select distinct sender from smssend where sender is not null and sender<>''";
		stlist = jdbcTemplate.query(sql, new RowMapper<SmsSend>() {
			public SmsSend mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new SmsSend(0, "", "", "", rs.getString("sender"), "",
						"", "", "", "", "", "");
			}
		});
		return stlist;

	}
}
