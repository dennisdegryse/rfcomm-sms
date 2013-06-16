package be.dennisdegryse.rfcommsms.client;

import java.io.IOException;
import java.util.UUID;

import be.dennisdegryse.rfcommsms.NotificationHelper;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class ConnectionManager {
	private static final String BT_SERVICE_NAME = "RFCOMM SMS";
	private static final UUID BT_SERVICE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private static ConnectionManager instance = null;
	
	private final Context context;
	private final Connection.Observer cleanupObserver = new Connection.Observer() {
		@Override
		public final void onClosed(Connection clientConnection) {
			final Context context = clientConnection.getContext();

			closeClientConnection();
			
			if (BluetoothAdapter.getDefaultAdapter().isEnabled())
				waitForClientConnection(context);
		}
	};
	private BluetoothServerSocket serverSocket = null;
	private Connection clientConnection = null;
	
	public final static ConnectionManager getInstance(Context context) {
		if (instance == null)
			instance = new ConnectionManager(context);
		
		return instance;
	}
	
	private ConnectionManager(Context context) {
		this.context = context;
	}
	private BluetoothServerSocket getServerSocket() throws IOException {
		final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

		if (serverSocket == null) {
			serverSocket = adapter.listenUsingInsecureRfcommWithServiceRecord(BT_SERVICE_NAME, BT_SERVICE_UUID);			

			NotificationHelper.notify(context, "SMS over RFCOMM activated");
		}
		
		return serverSocket;
	}
	
	private synchronized void closeClientConnection() {
		clientConnection = null;
	}
	
	protected final synchronized void registerClientConnection(Context context, BluetoothSocket clientSocket) {
		clientConnection = new Connection(context, clientSocket, cleanupObserver);
		new Thread(clientConnection).start();
	}
	
	public void listenForClient() {
		try {
			final BluetoothSocket clientSocket = getServerSocket().accept();
			
			registerClientConnection(context, clientSocket);
		} catch (IOException e) {
			serverSocket = null;
		}
	}
	
	public final synchronized boolean isConnected() {
		return clientConnection != null;
	}
	
	public final synchronized Connection getClientConnection() {
		return clientConnection;
	}

	public final void waitForClientConnection(final Context context) {	
		context.startService(new Intent(context, ConnectionListenerService.class));
	}
}