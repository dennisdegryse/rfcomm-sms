package be.dennisdegryse.rfcommsms.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import be.dennisdegryse.rfcommsms.client.ConnectionManager;
import be.dennisdegryse.rfcommsms.sms.Sms;
import be.dennisdegryse.rfcommsms.sms.SmsHelper;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class SmsReceiver extends BroadcastReceiver {
	private void sendAsUnsolicitedResponse(Context context, Sms sms) {
		final String response = "+CMT: \"" + sms.getAddress() + "\",,\"" + sms.serviceCenterTimeStamp() + "\"\r\n" + sms.getBody() + "\r\n";
		final ConnectionManager connectionManager = ConnectionManager.getInstance(context);
		
		if (connectionManager.isConnected())
			connectionManager.getClientConnection().write(response);
	}

	@Override
	public final void onReceive(Context context, Intent intent) {
		try {
			final Bundle bundle = intent.getExtras();
			final SmsHelper smsHelper = new SmsHelper(context);
	
			if (bundle != null) {
				final Object[] pduStrings = (Object[]) bundle.get("pdus");
	
				for (final Object pduString : pduStrings) {
					final Sms sms = smsHelper.parseFromPdu((byte[]) pduString);
					
					sendAsUnsolicitedResponse(context, sms);
				}
			}
		} catch (Exception e) {
			// IGNORE
		}
	}
}