package be.dennisdegryse.rfcommsms.atcommand;

import android.content.Context;
import be.dennisdegryse.rfcommsms.client.Connection;

/**
 * 
 * @author	Dennis Degryse <dennisdegryse@gmail.com>
 */
public class Parser {
	private final Connection clientConnection;
	private ParserState state = new RouterParserState(this);

	public Parser(Connection clientConnection) {
		this.clientConnection = clientConnection;
	}

	protected final Context getContext() {
		return clientConnection.getContext();
	}

	public final void parse(byte[] buffer, int size) {
		int offset = 0;

		do
			offset = state.parse(buffer, offset, size);
		while (offset < size);
	}

	protected final void sendResponse(String response) {
		clientConnection.write(response);
	}

	protected final void setState(ParserState state) {
		this.state = state;
	}
}
