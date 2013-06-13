package be.dennisdegryse.rfcommsms.atcommand;

import java.io.IOException;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public abstract class ParserState {
	protected static final char CARRIAGE_RETURN = '\r';
	protected static final char COLON = ',';
	protected static final char EQUALITY_SIGN = '=';
	protected static final char LINE_FEED = '\n';
	protected static final char QUESTION_MARK = '?';
	protected static final char QUOTE = '"';
	protected static final char SPACE = ' ';
	protected static final char SUBSTITUTE = '\032';
	private final Parser parser;
	private boolean terminated = false;

	public ParserState(Parser parser) {
		this.parser = parser;
	}

	protected final Parser getParser() {
		return parser;
	}

	public final int parse(byte[] buffer, int offset, int size) throws IOException {
		while (!terminated && offset < size)
			parseChar((char) buffer[offset++]);

		return offset;
	}

	protected abstract void parseChar(char chr) throws IOException;

	protected final void reinitializeParser() {
		transition(new RouterParserState(parser));
	}

	protected final void sendError() throws IOException {
		parser.sendResponse("\r\nERROR\r\n");
		reinitializeParser();
	}

	protected final void sendOk() throws IOException {
		parser.sendResponse("\r\nOK\r\n");
		reinitializeParser();
	}

	protected final void transition(ParserState state) {
		parser.setState(state);
		terminated = true;
	}

	protected final void transitionError() {
		transition(new ErrorParserState(parser));
	}
}