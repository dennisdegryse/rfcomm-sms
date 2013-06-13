package be.dennisdegryse.rfcommsms.atcommand;

import java.io.IOException;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class RouterParserState extends ParserState {
	private static final String[] ACCEPTED_COMMANDS = { "AT", "AT+", "AT+CMGL", "AT+CMGR", "AT+CMGD", "AT+CMGS" };
	private final StringBuilder commandNameBuilder = new StringBuilder();

	public RouterParserState(Parser paramParser) {
		super(paramParser);
	}

	private void validateCommand() {
		final String commandName = commandNameBuilder.toString();

		if (commandName.equals("AT") || commandName.equals("AT+"))
			transition(new ATCommandParserState(getParser()));
		else if (commandName.equals("AT+CMGL"))
			transition(new ATCMGLCommandParserState(getParser()));
		else if (commandName.equals("AT+CMGR"))
			transition(new ATCMGRCommandParserState(getParser()));
		else if (commandName.equals("AT+CMGS"))
			transition(new ATCMGSCommandParserState(getParser()));
		else if (commandName.equals("AT+CMGD"))
			transition(new ATCMGDCommandParserState(getParser()));
		else
			transitionError();
	}

	private void validateHelp() {
		final String commandName = this.commandNameBuilder.toString();

		for (String command : ACCEPTED_COMMANDS)
			if (commandName.equals(command)) {
				transition(new HelpParserState(getParser(), null));
				return;
			}

		transitionError();
	}

	private void validateProcedure(boolean executeNow) throws IOException {
		final String commandName = this.commandNameBuilder.toString();
		ProcedureParserState procedureState = null;
		
		if (commandName.length() == 0) {
			reinitializeParser();
			return;
		}
		
		else if (commandName.equals("AT") || commandName.equals("AT+"))
			procedureState = new ATProcedureParserState(getParser());
		
		if (procedureState != null) {
			transition(procedureState);
			
			if (executeNow)
				procedureState.runProcedure();
		} else if (executeNow)
			sendError();
		else
			transitionError();
	}

	@Override
	public final void parseChar(char chr) throws IOException {
		switch (chr) {
		case LINE_FEED:
			break;
		case QUESTION_MARK:
			validateHelp();
			break;
		case EQUALITY_SIGN:
			validateCommand();
			break;
		case SPACE:
			validateProcedure(false);
			break;
		case CARRIAGE_RETURN:
			validateProcedure(true);
			break;
		default:
			this.commandNameBuilder.append(chr);
			break;
		}
	}
}