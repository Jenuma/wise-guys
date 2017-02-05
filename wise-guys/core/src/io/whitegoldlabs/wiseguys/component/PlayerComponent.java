package io.whitegoldlabs.wiseguys.component;

import com.badlogic.ashley.core.Component;

public class PlayerComponent implements Component
{
	public enum PlayerState
	{
		NORMAL,
		SUPER,
		HACKER
	}
	
	public enum AnonState
	{
		EXPOSED,
		BEHIND_7_PROXIES
	}
	
	public PlayerState playerState;
	public AnonState anonState;
	
	public boolean damaged;
	public float damagedTime;
	
	public int stompChain;
	
	public int score;
	public int coins;
	public int lives;
	public short time;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public PlayerComponent(int score, int coins, int lives)
	{
		this.playerState = PlayerState.NORMAL;
		this.anonState = AnonState.EXPOSED;
		
		this.damaged = false;
		this.damagedTime = 0;
		
		this.stompChain = 0;
		
		this.score = score;
		this.coins = coins;
		this.lives = lives;
	}
}
