package be.dennisdegryse.rfcommsms.sms;

import android.content.Context;
import android.net.Uri;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import java.util.Date;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class SmsHelper {
	private static final String SMS_CONTENT_URI_PREFIX = "content://sms/conversations/";
	private static final String[] SMS_INBOX_CONTENT_COLUMNS = { "_id", "thread_id", "read", "address", "date", "body" };
	private static final Uri SMS_INBOX_CONTENT_URI = Uri.parse("content://sms/inbox");
	private static int counter = 0;
	private final Context context;

	public SmsHelper(Context context) {
		this.context = context;
	}

	public final void delete(Sms sms) {
		final Uri smsUrl = Uri.parse(SMS_CONTENT_URI_PREFIX + sms.getThreadId());

		context.getContentResolver().delete(smsUrl, "_id = " + sms.getId(), null);
	}

	public final Sms find(int index) {
		final Cursor cursor = query("_id = " + index, "date DESC");
		Sms localSms = null;

		if (cursor.moveToFirst())
			localSms = cursor.read();

		cursor.close();

		return localSms;
	}

	public final Sms parseFromPdu(byte[] pduString) {
		final SmsMessage smsMessage = SmsMessage.createFromPdu(pduString);

		return new Sms(
				smsMessage.getIndexOnIcc(), 
				-1, 
				false,
				smsMessage.getOriginatingAddress(), 
				new Date(smsMessage.getTimestampMillis()),
				smsMessage.getMessageBody());
	}

	public final Cursor query(String where, String orderBy) {
		return new Cursor(context.getContentResolver().query(SMS_INBOX_CONTENT_URI, SMS_INBOX_CONTENT_COLUMNS, where, null, orderBy));
	}

	public final int send(String address, String text) {
		SmsManager.getDefault().sendTextMessage(address, null, text, null, null);

		return counter++;
	}
}