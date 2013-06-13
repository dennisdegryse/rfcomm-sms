package be.dennisdegryse.rfcommsms.sms;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class Sms {
	public static final String STAT_ALL = "ALL";
	public static final String STAT_REC_READ = "REC READ";
	public static final String STAT_REC_UNREAD = "REC UNREAD";
	private final String address;
	private final String body;
	private final Date date;
	private final int id;
	private final boolean read;
	private final int threadId;

	public Sms(int id, int threadId, boolean read, String address, Date date,
			String body) {
		this.id = id;
		this.threadId = threadId;
		this.read = read;
		this.address = address;
		this.date = date;
		this.body = body;
	}

	private static String zoneOffset(Date date) {
		final Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);

		final int offset = (calendar.get(15) + calendar.get(16)) / 900000;
		final String sign = offset < 0 ? "-" : "+";

		return String.format(Locale.getDefault(), "%s%02d", sign, offset);
	}

	public String getAddress() {
		return this.address;
	}

	public String getBody() {
		return this.body;
	}

	public Date getDate() {
		return this.date;
	}

	public int getId() {
		return this.id;
	}

	public int getThreadId() {
		return this.threadId;
	}

	public boolean isRead() {
		return this.read;
	}

	public String serviceCenterTimeStamp() {
		return new SimpleDateFormat("yy/MM/dd,hh:mm:ss", Locale.getDefault())
				.format(this.date) + zoneOffset(this.date);
	}

	public String status() {
		return read ? STAT_REC_READ : STAT_REC_UNREAD;
	}
}