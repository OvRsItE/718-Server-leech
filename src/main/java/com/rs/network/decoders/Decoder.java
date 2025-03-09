package com.rs.network.decoders;

import com.rs.network.Session;
import com.rs.network.io.InputStream;

public abstract class Decoder {

	protected Session session;

	public Decoder(Session session) {
		this.session = session;
	}

	public abstract void decode(InputStream stream);

}
