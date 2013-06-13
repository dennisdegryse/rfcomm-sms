package be.dennisdegryse.rfcommsms.atcommand;

import java.io.IOException;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class ATCommandParserState extends CommandParserState {
	public ATCommandParserState(Parser parser) {
		super(parser, null, 0);
	}

	@Override
	protected final void execute() throws IOException {
		sendOk();
	}
}