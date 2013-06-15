package be.dennisdegryse.rfcommsms.client;

import android.bluetooth.BluetoothSocket;
import android.content.Context;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class ConnectionManager {
	private static ConnectionManager instance = null;
	private volatile Object lock = new Object();
	private final Map<Connection, Thread> clientConnectionThreads = new HashMap<Connection, Thread>();
	private final Connection.Observer cleanupObserver = new Connection.Observer() {
		@Override
		public final void onClosed(Connection connection) {
			synchronized (lock) {
				clientConnectionThreads.remove(connection);
			}
		}
	};

	public final static ConnectionManager getInstance() {
		if (instance == null)
			instance = new ConnectionManager();
		
		return instance;
	}

	public final Set<Connection> connections() {
		synchronized (lock) {
			return new HashSet<Connection>(clientConnectionThreads.keySet());
		}
	}

	public final void registerConnection(Context context, BluetoothSocket clientSocket) {
		final Connection connection = new Connection(context, clientSocket, cleanupObserver);
		final Thread connectionThread = new Thread(connection);

		synchronized (lock) {
			this.clientConnectionThreads.put(connection, connectionThread);
			connectionThread.start();
		}
	}
	
	public final void closeAll() {
		synchronized (lock) {
			for (Connection connection : clientConnectionThreads.keySet())
				connection.close();
		}
	}
}