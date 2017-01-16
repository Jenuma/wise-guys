package io.whitegoldlabs.wiseguys.component;

import com.badlogic.ashley.core.Component;

public class InventoryComponent implements Component
{
	public int score;
	public byte coins;
	public byte lives;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public InventoryComponent(int score, byte coins, byte lives)
	{
		this.score = score;
		this.coins = coins;
		this.lives = lives;
	}
}
