package be.dennisdegryse.rfcommsms.atcommand;

import be.dennisdegryse.rfcommsms.sms.Cursor;
import be.dennisdegryse.rfcommsms.sms.Sms;
import be.dennisdegryse.rfcommsms.sms.SmsHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class ATCMGLCommandParserState extends CommandParserState {
	private static final String HELP_MESSAGE = "+CMGL: (\"REC UNREAD\", \"REC READ\", \"ALL\")";
	private static final String RESPONSE_FORMAT = "+CMGL: %d,\"%s\",\"%s\",,\"%s\"\r\n%s\r\n";
	private static final Map<String, String> conditionMap = new HashMap<String, String>();
	private String statusArgument = null;

	static {
		conditionMap.put("ALL", null);
		conditionMap.put("REC READ", "read=1");
		conditionMap.put("REC UNREAD", "read=0");
	}

	public ATCMGLCommandParserState(Parser parser) {
		super(parser, HELP_MESSAGE, 1);
	}

	@Override
	protected final void execute() throws IOException {
		final String where = conditionMap.get(statusArgument);
		final String orderBy = "date DESC";
		final Cursor cursor = new SmsHelper(getParser().getContext()).query(where, orderBy);

		if (cursor.moveToFirst())
			do {
				final Sms sms = cursor.read();

				getParser().sendResponse(String.format(
						RESPONSE_FORMAT, 
						sms.getId(), 
						sms.status(), 
						sms.getAddress(), 
						sms.serviceCenterTimeStamp(), 
						sms.getBody()));
			} while (cursor.moveToNext());

		cursor.close();
		sendOk();
	}

	@Override
	protected final void validateStringArgument(int index, String value) throws IllegalArgumentException {
		if (!conditionMap.containsKey(value))
			throw new IllegalArgumentException();
		
		statusArgument = value;
	}
}