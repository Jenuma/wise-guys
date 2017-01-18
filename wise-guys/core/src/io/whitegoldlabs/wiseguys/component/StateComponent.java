package io.whitegoldlabs.wiseguys.component;

import com.badlogic.ashley.core.Component;

public class StateComponent implements Component
{
	public enum MotionState
	{
		STILL,
		MOVING,
		SLOWING,
		SPRINTING
	}
	
	public enum DirectionState
	{
		RIGHT,
		LEFT,
		NOT_APPLICABLE
	}
	
	public enum AirborneState
	{
		GROUNDED,
		JUMPING,
		FALLING,
		NOT_APPLICABLE
	}
	
	public MotionState motionState;
	public DirectionState directionState;
	public AirborneState airborneState;
	
	public float time;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public StateComponent()
	{
		time = 0;
	}
}
