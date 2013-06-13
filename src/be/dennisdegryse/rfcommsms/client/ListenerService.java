package be.dennisdegryse.rfcommsms.client;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import be.dennisdegryse.rfcommsms.NotificationHelper;

import java.io.IOException;
import java.util.UUID;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class ListenerService extends IntentService {
	private static final String BT_SERVICE_NAME = "RFCOMM SMS";
	private static final UUID BT_SERVICE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	public ListenerService() {
		super("Serial SMS Service");
	}

	private void waitForConnection(BluetoothServerSocket serverSocket) throws IOException {
		final BluetoothSocket clientSocket = serverSocket.accept();

		ConnectionManager.getInstance().registerConnection(getApplicationContext(), clientSocket);
	}

	@Override
	protected final void onHandleIntent(android.content.Intent paramIntent) {
		final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		BluetoothServerSocket serverSocket = null;

		if (adapter != null)
			try {
				serverSocket = adapter.listenUsingInsecureRfcommWithServiceRecord(BT_SERVICE_NAME, BT_SERVICE_UUID);

				NotificationHelper.notify(getApplicationContext(), "SMS over RFCOMM activated");
				waitForConnection(serverSocket);
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