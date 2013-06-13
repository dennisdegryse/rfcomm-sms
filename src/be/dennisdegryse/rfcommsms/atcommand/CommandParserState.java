package be.dennisdegryse.rfcommsms.atcommand;

import java.io.IOException;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public abstract class CommandParserState extends ParserState {
	private final String helpMessage;
	private final int maxArgumentsCount;
	private final int minArgumentsCount;
	private int argumentsCount = 0;
	private boolean canEvolveToHelp = true;

	public CommandParserState(Parser paramParser, String helpMessage, int argumentsCount) {
		this(paramParser, helpMessage, argumentsCount, argumentsCount);
	}

	public CommandParserState(Parser parser, String helpMessage, int minArgumentsCount, int maxArgumentsCount) {
		super(parser);
		
		this.helpMessage = helpMessage;
		this.minArgumentsCount = minArgumentsCount;
		this.maxArgumentsCount = maxArgumentsCount;
	}

	private int allocateArgumentIndex(boolean last) throws IllegalArgumentException {
		final int index = argumentsCount++;

		if (argumentsCount == maxArgumentsCount && !last || argumentsCount < minArgumentsCount && last)
			throw new IllegalArgumentException();

		return index;
	}

	@Override
	protected final void parseChar(char chr) throws IOException {
		if (this.canEvolveToHelp) {
			switch (chr) {
			case SPACE:
				return;

			case QUESTION_MARK:
				transition(new HelpParserState(getParser(), this.helpMessage));
				return;
			}

			this.canEvolveToHelp = false;
		}

		switch (chr) {
		case QUOTE:
			transition(new StringArgumentParserState(getParser(), this));
			break;
			
		case COLON:
			validateEmptyArgument(argumentsCount++);
			break;
			
		default:
			if (NumericArgumentParserState.isNumericDigit(chr))
				transition(new NumericArgumentParserState(getParser(), this, chr - 0x30));
			else
				transitionError();

			break;
		}
	}

	protected final void receiveNumericArgument(int value, boolean last) throws IOException {
		try {
			validateNumericArgument(allocateArgumentIndex(last), value);
			
			if (last)
				execute();
		} catch (IllegalArgumentException e) {
			if (last)
				sendError();
			else
				transitionError();
		}
	}

	protected final void receiveStringArgument(String value, boolean last) throws IOException {
		try {
			validateStringArgument(allocateArgumentIndex(last), value);
			
			if (last)
				execute();
		} catch (IllegalArgumentException e) {
			if (last)
				sendError();
			else
				transitionError();
		}
	}

	protected void validateData(String data) throws IOException {
	}

	protected void validateEmptyArgument(int index) throws IllegalArgumentException, IOException {
		throw new IllegalArgumentException();
	}

	protected void validateNumericArgument(int index, int value) throws IllegalArgumentException, IOException {
		throw new IllegalArgumentException();
	}

	protected void validateStringArgument(int index, String value) throws IllegalArgumentException, IOException {
		throw new IllegalArgumentException();
	}

	protected abstract void execute() throws IOException;
}