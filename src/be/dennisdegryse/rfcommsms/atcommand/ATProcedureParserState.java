package be.dennisdegryse.rfcommsms.atcommand;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class ATProcedureParserState extends ProcedureParserState {
	public ATProcedureParserState(Parser parser) {
		super(parser);
	}

	@Override
	protected final void parseChar(char chr) {
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
	protected final void runProcedure() {
		sendOk();
	}
}