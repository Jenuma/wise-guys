package io.whitegoldlabs.wiseguys.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

public class AnimationComponent implements Component
{
	public Sprite standingSprite;
	public Sprite jumpingSprite;
	public Array<Sprite> walkingSprites;
	
	public int walkingFrame;
	public float movingAnimationTime;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public AnimationComponent(Sprite standingSprite, Sprite jumpingSprite, Array<Sprite> walkingSprites)
	{
		this.standingSprite = standingSprite;
		this.jumpingSprite = jumpingSprite;
		this.walkingSprites = walkingSprites;
		
		this.walkingFrame = 0;
		movingAnimationTime = 0;
	}
}
