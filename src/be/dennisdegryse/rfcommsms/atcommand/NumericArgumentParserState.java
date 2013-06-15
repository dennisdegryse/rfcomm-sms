package be.dennisdegryse.rfcommsms.atcommand;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class NumericArgumentParserState extends ParserState {
	private final CommandParserState commandState;
	private int argument;

	public NumericArgumentParserState(Parser parser, CommandParserState originatingCommandState, int firstDigit) {
		super(parser);

		this.commandState = originatingCommandState;
		this.argument = firstDigit;
	}

	protected final static boolean isNumericDigit(char paramChar) {
		return paramChar >= 0x30 && paramChar <= 0x39;
	}

	private void returnNumericArgument(boolean paramBoolean) {
		transition(this.commandState);
		commandState.receiveNumericArgument(argument, paramBoolean);
	}

	@Override
	protected final void parseChar(char chr) {
		switch (chr) {
		case COLON:
			returnNumericArgument(false);
			break;
			
		case CARRIAGE_RETURN:
			returnNumericArgument(true);
			break;

		default:
			if (isNumericDigit(chr))
				argument = 10 * argument + (chr - 0x30);
			else
				transitionError();
		}
	}
}