package io.whitegoldlabs.wiseguys.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;

public class HitboxComponent implements Component
{
	public Rectangle hitbox;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                     |
	// ---------------------------------------------------------------------------------|
	public HitboxComponent(float x, float y, float width, float height)
	{
		this.hitbox = new Rectangle(x, y, width, height);
	}
}
