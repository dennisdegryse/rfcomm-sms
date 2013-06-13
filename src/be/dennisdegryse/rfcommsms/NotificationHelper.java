package be.dennisdegryse.rfcommsms;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class NotificationHelper {
	private static int counter = 0;

	public static final void notify(Context context, String message) {
		final NotificationManager localNotificationManager = (NotificationManager) context.getSystemService("notification");
		final Notification localNotification = new Notification(R.drawable.ic_stat_notify_rfcomm, message, System.currentTimeMillis());

		localNotification.setLatestEventInfo(context, "RFCOMM SMS", message, PendingIntent.getBroadcast(context, 1, new Intent(), Notification.FLAG_AUTO_CANCEL));
		localNotificationManager.notify(counter++, localNotification);
	}
}
