package io.whitegoldlabs.wiseguys.component;

import com.badlogic.ashley.core.Component;

public class VelocityComponent implements Component
{
	public float x;
	public float y;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public VelocityComponent(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
}
