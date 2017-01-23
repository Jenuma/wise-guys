package io.whitegoldlabs.wiseguys.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class HitboxComponent implements Component
{
	public Rectangle hitbox;
	public Sprite sprite;
	
	// ---------------------------------------------------------------------------------|
	// Constructors                                                                     |
	// ---------------------------------------------------------------------------------|
	public HitboxComponent(float x, float y, float width, float height, Sprite sprite)
	{
		this.hitbox = new Rectangle(x, y, width, height);
		this.sprite = sprite;
	}
	
	public HitboxComponent(float x, float y, float width, float height)
	{
		this.hitbox = new Rectangle(x, y, width, height);
	}
}
