package com.aaramon.aaramonjio.controller;import android.os.Handler;
import android.os.Message;

public class UIController extends Handler {

	private static UIController CONTROLLER;

	private IScreenView iScreenView;

	private UIController() {
	}

	public static UIController getController() {
		if (CONTROLLER == null) {
			CONTROLLER = new UIController();
		}
		return CONTROLLER;
	}

	public IScreenView getiScreenView() {
		return iScreenView;
	}

	public void setiScreenView(IScreenView iScreenView) {
		this.iScreenView = iScreenView;
	}

	public void handleMessage(Message msg) {

		UIMessage message = (UIMessage) msg.obj;
		if (iScreenView != null) {

			// if (message.getCommand() == DataStaticVariables.NOTIFICATION ||
			// message.getCommand() == DataStaticVariables.NEW_INVITATION
			// || message.getCommand() == DataStaticVariables.NEW_FILE_REQUEST
			// || message.getCommand() == DataStaticVariables.NEW_MESSAGE
			// || message.getCommand() == DataStaticVariables.UPDATE_ROSTERLIST)
			// {
			//
			//
			//
			// //
			// if(DataStaticVariables.hashchatCount.get(selectedJid.getUsername()
			// + "@"
			// // + selectedJid.getServername()).equals("1")) {
			// // int count = Datas.invitations.size() +
			// DataStaticVariables.hashchatCountVector.size();
			// //
			// // Toast.makeText((Context) iScreenView,
			// "u have "+count+" notification pending ",
			// Toast.LENGTH_SHORT).show();
			// // }
			// //
			// if(message.getCommand() == DataStaticVariables.NEW_MESSAGE ) {
			// SingleChat conversation = (SingleChat) message
			// .getScreenData();
			// Jid selectedJid = conversation.roster;
			// //int msg_count =0;
			//
			// Log.e("has chat count ","count "+DataStaticVariables.hashchatCount.get(selectedJid.getUsername()
			// + "@"
			// + selectedJid.getServername()));
			//
			//
			// if ( !
			// DataStaticVariables.hashchatCount.containsKey(selectedJid.getUsername()
			// + "@" + selectedJid.getServername())) {
			// // msg_count = ((Integer)
			// DataStaticVariables.hashchatCount.get(selectedJid
			// // .getUsername() + "@" +
			// selectedJid.getServername())).intValue();
			//
			// // if (msg_count == 1) {
			// // int count = Datas.invitations.size() +
			// DataStaticVariables.hashchatCountVector.size() + 1;
			// // Toast.makeText((Context) iScreenView,
			// "u have "+count+" notification pending ",
			// Toast.LENGTH_SHORT).show();
			// // notificationIntent();
			// // }
			//
			// int count = Datas.notifications.size() +
			// DataStaticVariables.hashchatCountVector.size() +1;
			// Toast.makeText((Context) iScreenView,
			// "u have "+count+" notification pending ",
			// Toast.LENGTH_SHORT).show();
			//
			// notificationIntent();
			// }
			//
			//
			// } else {
			// int count = Datas.notifications.size() +
			// DataStaticVariables.hashchatCountVector.size() +1;
			// Toast.makeText((Context) iScreenView,
			// "u have "+count+" notification pending ",
			// Toast.LENGTH_SHORT).show();
			//
			// notificationIntent();
			// }
			//
			//
			// }

			if (!iScreenView.updateScreen(message)) {

			}

		}
	}

	public void sendMessage(byte cmd, byte res, byte mode, Object obj) {
		UIMessage message = new UIMessage();
		message.setCommand(cmd);
		message.setResponseCode(res);
		message.setScreenData(obj);
		message.setMode(mode);

		Message msg = new Message();
		msg.obj = message;

	}

	public void sendMessage(byte cmd, byte res, Object obj) {
		UIMessage message = new UIMessage();
		message.setCommand(cmd);
		message.setResponseCode(res);
		message.setScreenData(obj);

		Message msg = new Message();
		msg.obj = message;
		sendMessage(msg);
	}

	public void sendMessage(byte msg) {
		UIMessage uimessage = new UIMessage();
		uimessage.setCommand(msg);

		Message message = new Message();
		message.obj = uimessage;
		sendMessage(message);
	}

	public void sendMessage(byte msg, Object rosList) {
		UIMessage uimessages = new UIMessage();
		uimessages.setCommand(msg);
		uimessages.setScreenData(rosList);

		Message messages = new Message();
		messages.obj = uimessages;
		sendMessage(messages);
	}
}
