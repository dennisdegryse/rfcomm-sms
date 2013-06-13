package be.dennisdegryse.rfcommsms.atcommand;

import java.io.IOException;

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

	private void sendHelpMessage() throws IOException {
		if (message != null)
			getParser().sendResponse(message);

		sendOk();
	}

	@Override
	protected final void parseChar(char chr) throws IOException {
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