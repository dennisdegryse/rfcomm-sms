package be.dennisdegryse.rfcommsms.receiver;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import be.dennisdegryse.rfcommsms.client.ConnectionManager;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class BluetoothReceiver extends BroadcastReceiver {
	@Override
	public final void onReceive(Context context, Intent intent) {
		final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
		final ConnectionManager connectionManager = ConnectionManager.getInstance(context);

		switch (state) {
		case BluetoothAdapter.STATE_ON:
			connectionManager.waitForClientConnection(context);
			break;

		case BluetoothAdapter.STATE_TURNING_OFF:
			
			if (connectionManager.isConnected())
				connectionManager.getClientConnection().close();
			
			break;
		}
	}
}