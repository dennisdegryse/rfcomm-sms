package be.dennisdegryse.rfcommsms.atcommand;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class ATCommandParserState extends CommandParserState {
	public ATCommandParserState(Parser parser) {
		super(parser, null, 0);
	}

	@Override
	protected final void execute() {
		sendOk();
	}
}