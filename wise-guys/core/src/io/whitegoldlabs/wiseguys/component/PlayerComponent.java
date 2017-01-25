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
		OPEN,
		BEHIND_7_PROXIES
	}
	
	public PlayerState playerState;
	public AnonState anonState;
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
		this.anonState = AnonState.OPEN;
		
		this.score = score;
		this.coins = coins;
		this.lives = lives;
		this.time = 400;
	}
}
