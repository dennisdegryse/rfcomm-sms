package be.dennisdegryse.rfcommsms.atcommand;

import java.io.IOException;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public abstract class ProcedureParserState extends ParserState {
	public ProcedureParserState(Parser parser) {
		super(parser);
	}

	protected abstract void runProcedure() throws IOException;
}