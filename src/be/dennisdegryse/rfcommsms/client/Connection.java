package be.dennisdegryse.rfcommsms.client;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import be.dennisdegryse.rfcommsms.atcommand.Parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class Connection implements Runnable {
	private final BluetoothSocket clientSocket;
	private final Context context;
	private final Observer observer;
	private final Parser parser;
	private volatile boolean stopRequested = false;

	protected Connection(Context context, BluetoothSocket clientSocket, Observer observer) {
		this.context = context;
		this.clientSocket = clientSocket;
		this.observer = observer;
		this.parser = new Parser(this);
	}

	private int read(byte[] buffer, int timeout) {
		final int maxTimeout = timeout / 50;
		int elapsedTimeout = 0;

		try {
			final InputStream inputStream = clientSocket.getInputStream();
			
			while (inputStream.available() == 0 && elapsedTimeout++ < maxTimeout)
				try {				
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// IGNORE
				}
			
			return inputStream.read(buffer);
		} catch (IOException e) {
			close();
		}
		
		return 0;
	}

	private void terminate() {
		if (clientSocket != null)
			try {
				clientSocket.close();
			} catch (IOException e) {
				// IGNORE
			}

		observer.onClosed(this);
	}
	
	public final void close() {
		stopRequested = true;
	}

	public final Context getContext() {
		return context;
	}

	@Override
	public final void run() {
		final byte[] buffer = new byte[512];

		while (!stopRequested) {
			int size = read(buffer, 500);
			
			if (size > 0)
				parser.parse(buffer, size);
		}

		terminate();
	}

	public final void write(String paramString) {
		try {
			write(paramString.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// IGNORE
		}
	}

	public final void write(byte[] paramArrayOfByte) {
		try {
			clientSocket.getOutputStream().write(paramArrayOfByte);
		} catch (IOException e) {
			close();
		}
	}

	public static abstract interface Observer {
		public abstract void onClosed(Connection connection);
	}
}