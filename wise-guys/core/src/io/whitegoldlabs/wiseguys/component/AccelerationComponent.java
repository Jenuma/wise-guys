package io.whitegoldlabs.wiseguys.component;

import com.badlogic.ashley.core.Component;

public class AccelerationComponent implements Component
{
	public float x;
	public float y;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public AccelerationComponent(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
}
