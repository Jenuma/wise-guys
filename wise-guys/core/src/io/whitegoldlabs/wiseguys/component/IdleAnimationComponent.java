package io.whitegoldlabs.wiseguys.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

public class IdleAnimationComponent implements Component
{
	public Array<Sprite> idleSprites;
	
	public int idleFrame;
	public float idleAnimationTime;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public IdleAnimationComponent(Array<Sprite> idleSprites)
	{
		this.idleSprites = idleSprites;
		
		this.idleFrame = 0;
		this.idleAnimationTime = 0;
	}
}
