package com.rs.network.encoders.impl;

import com.rs.utility.Censor;
import com.rs.utility.Utils;

public class ChatMessage {

	private String message;
	private String filteredMessage;

	public ChatMessage(String message) {
		filteredMessage = Censor.getFilteredMessage(message);
		this.message = Utils.fixChatMessage(message);
	}

	public String getMessage(boolean filtered) {
		return filtered ? filteredMessage : message;
	}
}
