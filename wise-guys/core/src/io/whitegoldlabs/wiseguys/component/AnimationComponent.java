package io.whitegoldlabs.wiseguys.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

public class AnimationComponent implements Component
{
	public Array<Sprite> stillSprites;
	public Array<Sprite> jumpingSprites;
	public Array<Sprite> movingSprites;
	public Array<Sprite> slowingSprites;
	public Array<Sprite> sprintingSprites;
	
	public int stillFrame;
	public int jumpingFrame;
	public int movingFrame;
	public int slowingFrame;
	public int sprintingFrame;
	
	public float stillAnimationTime;
	public float jumpingAnimationTime;
	public float movingAnimationTime;
	public float slowingAnimationTime;
	public float sprintingAnimationTime;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public AnimationComponent
	(
		Array<Sprite> stillSprites,
		Array<Sprite> jumpingSprites,
		Array<Sprite> movingSprites,
		Array<Sprite> slowingSprites,
		Array<Sprite> sprintingSprites
	)
	{
		this.stillSprites = stillSprites;
		this.jumpingSprites = jumpingSprites;
		this.movingSprites = movingSprites;
		this.slowingSprites = slowingSprites;
		this.sprintingSprites = sprintingSprites;
		
		this.stillFrame = 0;
		this.jumpingFrame = 0;
		this.movingFrame = 0;
		this.slowingFrame = 0;
		this.sprintingFrame = 0;
		
		this.stillAnimationTime = 0;
		this.jumpingAnimationTime = 0;
		this.movingAnimationTime = 0;
		this.slowingAnimationTime = 0;
		this.sprintingAnimationTime = 0;
	}
}
