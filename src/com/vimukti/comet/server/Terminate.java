package com.vimukti.comet.server;



public class Terminate implements Message {

	public Terminate( final long sequence ){
		super();
		this.setSequence(sequence);
	}
	
	public int getCommand() {
		return Constants.TERMINATE_COMET_SESSION;
	}

	public String getObject() {
		return null;
	}

	private long sequence;
	
	public long getSequence(){
		return this.sequence;
	}
	
	void setSequence( final long sequence ){
		this.sequence = sequence;
	}

	@Override
	public String getData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStream() {
		// TODO Auto-generated method stub
		return null;
	}

}
