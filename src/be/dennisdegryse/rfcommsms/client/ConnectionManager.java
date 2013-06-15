package be.dennisdegryse.rfcommsms.client;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class ConnectionManager {
	private static ConnectionManager instance = null;
	
	private final Connection.Observer cleanupObserver = new Connection.Observer() {
		@Override
		public final void onClosed(Connection connection) {
			final Context context = connection.getContext();

			closeConnection();
			
			if (BluetoothAdapter.getDefaultAdapter().isEnabled())
				waitForConnection(context);
		}
	};
	private Connection connection = null;
	
	public final static ConnectionManager getInstance() {
		if (instance == null)
			instance = new ConnectionManager();
		
		return instance;
	}

	private synchronized void closeConnection() {
		connection = null;
	}
	
	protected final synchronized void registerConnection(Context context, BluetoothSocket clientSocket) {
		this.connection = new Connection(context, clientSocket, cleanupObserver);
		new Thread(connection).start();
	}
	
	public final synchronized boolean isConnected() {
		return connection != null;
	}
	
	public final synchronized Connection getConnection() {
		return connection;
	}

	public final void waitForConnection(final Context context) {
		context.startService(new Intent(context, ConnectionListenerService.class));
	}
}