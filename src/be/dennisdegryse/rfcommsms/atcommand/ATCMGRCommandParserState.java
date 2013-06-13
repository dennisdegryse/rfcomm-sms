package be.dennisdegryse.rfcommsms.atcommand;

import be.dennisdegryse.rfcommsms.sms.Sms;
import be.dennisdegryse.rfcommsms.sms.SmsHelper;

import java.io.IOException;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class ATCMGRCommandParserState extends CommandParserState {
	private static final String RESPONSE_FORMAT = "+CMGR: \"%s\",\"%s\",,\"%s\"\r\n%s\r\n";
	private int indexArgument = -1;

	public ATCMGRCommandParserState(Parser parser) {
		super(parser, null, 1);
	}

	@Override
	protected final void execute() throws IOException {
		Sms sms = new SmsHelper(getParser().getContext()).find(indexArgument);

		if (sms != null)
			getParser().sendResponse(String.format(
					RESPONSE_FORMAT, 
					sms.status(),
					sms.getAddress(),
					sms.serviceCenterTimeStamp(),
					sms.getBody()));

		sendOk();
	}

	@Override
	protected final void validateNumericArgument(int index, int value) throws IllegalArgumentException {
		this.indexArgument = value;
	}
}