package be.dennisdegryse.rfcommsms.atcommand;

import be.dennisdegryse.rfcommsms.sms.SmsHelper;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class ATCMGSCommandParserState extends CommandParserState {
	private static final String RESPONSE_FROMAT = "+CMGS: %d\r\n";
	private String numberArgument = null;

	public ATCMGSCommandParserState(Parser parser) {
		super(parser, null, 1);
	}

	@Override
	protected final void execute() {
		getParser().sendResponse("> ");
		transition(new DataParserState(getParser(), this));
	}

	@Override
	protected final void validateData(String data) {
		final SmsHelper smsHelper = new SmsHelper(getParser().getContext());
		final int reference = smsHelper.send(numberArgument, data);

		getParser().sendResponse(String.format(RESPONSE_FROMAT, reference));
		sendOk();
	}

	@Override
	protected final void validateStringArgument(int index, String value) throws IllegalArgumentException {
		numberArgument = value;
	}
}