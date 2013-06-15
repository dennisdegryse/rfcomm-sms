package be.dennisdegryse.rfcommsms.atcommand;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class StringArgumentParserState extends ParserState {
	private final StringBuilder argument = new StringBuilder();
	private final CommandParserState originatingCommandState;
	private boolean closedString = false;

	public StringArgumentParserState(Parser parser,
			CommandParserState originatingCommandState) {
		super(parser);

		this.originatingCommandState = originatingCommandState;
	}

	private void returnStringArgument(boolean last) {
		transition(originatingCommandState);

		originatingCommandState.receiveStringArgument(argument.toString(), last);
	}

	@Override
	protected final void parseChar(char chr) {
		switch (chr) {
		case QUOTE:
			if (this.closedString)
				transitionError();
			else
				closedString = true;

			break;

		case CARRIAGE_RETURN:
			if (closedString)
				returnStringArgument(true);
			else
				sendError();

			break;

		case COLON:
			if (this.closedString) {
				returnStringArgument(false);
				break;
			}

		default:
			argument.append(chr);
			break;
		}
	}
}