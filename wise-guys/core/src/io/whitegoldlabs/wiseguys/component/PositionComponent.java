package io.whitegoldlabs.wiseguys.component;

import com.badlogic.ashley.core.Component;

public class PositionComponent implements Component
{
	public float x;
	public float y;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public PositionComponent(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
}
