package be.dennisdegryse.rfcommsms.atcommand;

import java.io.IOException;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class ATProcedureParserState extends ProcedureParserState {
	public ATProcedureParserState(Parser parser) {
		super(parser);
	}

	@Override
	protected final void parseChar(char chr) throws IOException {
		switch (chr) {
		case LINE_FEED:
		case SPACE:
			break;
			
		case CARRIAGE_RETURN:
			runProcedure();
			break;
			
		default:
			transitionError();
		}
	}

	@Override
	protected final void runProcedure() throws IOException {
		sendOk();
	}
}