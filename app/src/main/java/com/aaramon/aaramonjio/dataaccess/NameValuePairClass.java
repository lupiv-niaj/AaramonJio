package com.aaramon.aaramonjio.dataaccess;

import org.apache.http.message.BasicNameValuePair;

import java.io.Serializable;

public class NameValuePairClass extends BasicNameValuePair implements
        Serializable {

	public NameValuePairClass(String name, String value) {
		super(name, value);
	}

}
