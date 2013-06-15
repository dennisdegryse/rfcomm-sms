package be.dennisdegryse.rfcommsms.atcommand;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public abstract class ProcedureParserState extends ParserState {
	public ProcedureParserState(Parser parser) {
		super(parser);
	}

	protected abstract void runProcedure();
}