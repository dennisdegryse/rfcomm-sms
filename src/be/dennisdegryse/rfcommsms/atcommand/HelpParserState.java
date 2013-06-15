package be.dennisdegryse.rfcommsms.atcommand;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class HelpParserState extends ParserState {
	private final String message;

	public HelpParserState(Parser paramParser, String message) {
		super(paramParser);
		this.message = message;
	}

	private void sendHelpMessage() {
		if (message != null)
			getParser().sendResponse(message);

		sendOk();
	}

	@Override
	protected final void parseChar(char chr) {
		switch (chr) {
		case LINE_FEED:
		case SPACE:
			break;
			
		case CARRIAGE_RETURN:
			sendHelpMessage();
			break;
			
		default:
			transitionError();
			break;
		}
	}
}