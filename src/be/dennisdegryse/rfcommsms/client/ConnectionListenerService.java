package be.dennisdegryse.rfcommsms.client;

import android.app.IntentService;
import android.content.Intent;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class ConnectionListenerService extends IntentService {
	public ConnectionListenerService() {
		super("RFCOMM SMS Listener");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		ConnectionManager.getInstance(getApplicationContext()).listenForClient();
	}
}
