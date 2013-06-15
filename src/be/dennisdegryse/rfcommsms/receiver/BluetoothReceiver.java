package be.dennisdegryse.rfcommsms.receiver;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import be.dennisdegryse.rfcommsms.client.ConnectionManager;
import be.dennisdegryse.rfcommsms.client.ListenerService;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class BluetoothReceiver extends BroadcastReceiver {
	private void listenForConnections(Context context) {
		context.startService(new Intent(context, ListenerService.class));
	}

	@Override
	public final void onReceive(Context context, Intent intent) {
		final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

		switch (state) {
		case BluetoothAdapter.STATE_ON:
			listenForConnections(context);
			break;

		case BluetoothAdapter.STATE_TURNING_OFF:
			ConnectionManager.getInstance().closeAll();
			break;
		}
	}
}