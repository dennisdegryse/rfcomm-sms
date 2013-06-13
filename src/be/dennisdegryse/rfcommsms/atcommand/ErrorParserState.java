package be.dennisdegryse.rfcommsms.atcommand;

import java.io.IOException;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class ErrorParserState extends ParserState {
	public ErrorParserState(Parser parser) {
		super(parser);
	}

	@Override
	protected final void parseChar(char chr) throws IOException {
		if (chr == CARRIAGE_RETURN)
			sendError();
	}
}