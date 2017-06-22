package com.aaramon.aaramonjio.controller;
public class UIMessage {

	// private IScreenView view;
	private Object screenData;
	private byte responseCode;
	private byte command;
	private byte mode;

	// public IScreenView getView() {
	// return view;
	// }
	// public void setView(IScreenView view) {
	// this.view = view;
	// }

	public Object getScreenData() {
		return screenData;
	}

	public byte getCommand() {
		return command;
	}

	public void setCommand(byte command) {
		this.command = command;
	}

	public void setScreenData(Object screenData) {
		this.screenData = screenData;
	}

	public byte getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(byte responseCode) {
		this.responseCode = responseCode;
	}

	public byte getMode() {
		return mode;
	}

	public void setMode(byte mode) {
		this.mode = mode;
	}

}
