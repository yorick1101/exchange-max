package me.yorick.adapter.max.message;

/**
 * @author hauchung
 *
 */
public class ChallengeResponseMessage {
	private String cmd = "auth";
	private String access_key;
	private String answer;

	public ChallengeResponseMessage(String access_key) {
		this.access_key=access_key;
	}
	public String getCmd() {
		return cmd;
	}
	
	public String getAccess_key() {
		return access_key;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}


}
