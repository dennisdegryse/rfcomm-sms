package be.dennisdegryse.rfcommsms.atcommand;

import be.dennisdegryse.rfcommsms.sms.Sms;
import be.dennisdegryse.rfcommsms.sms.SmsHelper;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class ATCMGDCommandParserState extends CommandParserState {
	private int indexArgument = -1;

	public ATCMGDCommandParserState(Parser parser) {
		super(parser, null, 1);
	}

	@Override
	protected final void execute() {
		final SmsHelper localSmsHelper = new SmsHelper(getParser().getContext());
		final Sms localSms = localSmsHelper.find(this.indexArgument);

		if (localSms != null)
			localSmsHelper.delete(localSms);

		sendOk();
	}

	@Override
	protected final void validateNumericArgument(int index, int value) throws IllegalArgumentException {
		this.indexArgument = value;
	}
}