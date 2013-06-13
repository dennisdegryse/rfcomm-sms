package be.dennisdegryse.rfcommsms.sms;

import java.util.Date;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class Cursor {
	private final android.database.Cursor wrappedCursor;

	protected Cursor(android.database.Cursor cursor) {
		this.wrappedCursor = cursor;
	}

	public final void close() {
		wrappedCursor.close();
	}

	public final boolean moveToFirst() {
		return wrappedCursor.moveToFirst();
	}

	public final boolean moveToNext() {
		return wrappedCursor.moveToNext();
	}

	public final Sms read() {
		return new Sms(
				wrappedCursor.getInt(wrappedCursor.getColumnIndexOrThrow("_id")),
				wrappedCursor.getInt(wrappedCursor.getColumnIndexOrThrow("thread_id")),
				wrappedCursor.getInt(wrappedCursor.getColumnIndexOrThrow("read")) == 1,
				wrappedCursor.getString(wrappedCursor.getColumnIndexOrThrow("address")), 
				new Date(wrappedCursor.getLong(wrappedCursor.getColumnIndexOrThrow("date"))),
				wrappedCursor.getString(wrappedCursor.getColumnIndexOrThrow("body")));
	}
}
