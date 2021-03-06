package be.dennisdegryse.rfcommsms.atcommand;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class DataParserState extends ParserState {
	private final CommandParserState commandState;
	private final StringBuilder data = new StringBuilder();

	public DataParserState(Parser parser, CommandParserState originatingCommandState) {
		super(parser);
		
		this.commandState = originatingCommandState;
	}

	private void returnData() {
		transition(commandState);
		commandState.validateData(data.toString());
	}

	@Override
	protected final void parseChar(char chr) {
		if (chr == SUBSTITUTE)
			returnData();
		else
			data.append(chr);
	}
}