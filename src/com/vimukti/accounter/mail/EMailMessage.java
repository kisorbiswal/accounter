package com.vimukti.accounter.mail;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class EMailMessage {
	
	private String from;
	
	public String subject;

	public String content;

	File[] attachment;

	Set<String> recepeiants = new HashSet<String>();

	private String replayTO;

	public boolean isPlain;

	public Set<String> getRecipeants() {
		return recepeiants;
	}

	public void setRecepeants(Set<String> to) {
		this.recepeiants = to;
	}

	public void setRecepeant(String to) {
		this.recepeiants.add(to);
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public File[] getAttachment() {
		return attachment;
	}

	public void setAttachment(File[] attachment) {
		this.attachment = attachment;
	}

	public void setReplayTO(String replayTO) {
		this.replayTO = replayTO;
	}

	public String getReplayTO() {
		return replayTO;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

}
