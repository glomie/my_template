package com.temp.designPatterns.state;

public interface VoteState {

	public void vote(String user, String voteItem, VoteManager voteManager);
}
