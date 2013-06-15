package be.dennisdegryse.rfcommsms.atcommand;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class ErrorParserState extends ParserState {
	public ErrorParserState(Parser parser) {
		super(parser);
	}

	@Override
	protected final void parseChar(char chr) {
		if (chr == CARRIAGE_RETURN)
			sendError();
	}
}