package com.temp.designPatterns.state;

public class NormalVoteState implements VoteState {

	@Override
	public void vote(String user, String voteItem, VoteManager voteManager) {
		
		System.out.println("恭喜你投票成功");
	}

}
