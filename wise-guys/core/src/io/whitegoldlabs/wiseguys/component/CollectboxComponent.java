package io.whitegoldlabs.wiseguys.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class CollectboxComponent implements Component
{
	public enum Type
	{
		COIN,
		TELEPORT_DOWN,
		TELEPORT_RIGHT
	}
	
	public Rectangle collectbox;
	public Type type;
	public Sprite sprite;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public CollectboxComponent(float x, float y, float width, float height, Type type, Sprite sprite)
	{
		this.collectbox = new Rectangle(x, y, width, height);
		this.type = type;
		this.sprite = sprite;
	}
}
