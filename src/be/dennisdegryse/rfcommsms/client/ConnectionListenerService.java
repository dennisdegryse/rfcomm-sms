package be.dennisdegryse.rfcommsms.client;

import java.io.IOException;
import java.util.UUID;

import be.dennisdegryse.rfcommsms.NotificationHelper;
import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.content.Context;
import android.content.Intent;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class ConnectionListenerService extends IntentService {
	private static final String BT_SERVICE_NAME = "RFCOMM SMS";
	private static final UUID BT_SERVICE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	public ConnectionListenerService() {
		super("RFCOMM SMS Listener");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		final Context context = getApplicationContext();
		final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		BluetoothServerSocket serverSocket = null;

		try {
			serverSocket = adapter.listenUsingInsecureRfcommWithServiceRecord(BT_SERVICE_NAME, BT_SERVICE_UUID);

			NotificationHelper.notify(context, "SMS over RFCOMM activated");
			ConnectionManager.getInstance().registerConnection(context, serverSocket.accept());
		} catch (IOException e) {
			// IGNORE
		} finally {
			if (serverSocket != null)
				try {
					serverSocket.close();
				} catch (IOException e) {
					// IGNORE
				}
		}
	}

}
