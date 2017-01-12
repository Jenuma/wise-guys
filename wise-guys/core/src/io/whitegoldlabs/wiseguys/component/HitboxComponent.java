package io.whitegoldlabs.wiseguys.component;

import com.badlogic.ashley.core.Component;

public class HitboxComponent implements Component
{
	public float x;
	public float y;
	public float width;
	public float height;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public HitboxComponent(float x, float y, float width, float height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
}
