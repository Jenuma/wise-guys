package io.whitegoldlabs.wiseguys.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class HitboxComponent implements Component
{
	public enum Type
	{
		HITBOX,
		COLLECTBOX
	}
	
	public Rectangle hitbox;
	public Type type;
	public Sprite sprite;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public HitboxComponent(float x, float y, float width, float height, Type type, Sprite sprite)
	{
		this.hitbox = new Rectangle(x, y, width, height);
		this.type = type;
		this.sprite = sprite;
	}
}
