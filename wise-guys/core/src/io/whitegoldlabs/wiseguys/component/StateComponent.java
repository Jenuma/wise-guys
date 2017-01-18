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
	
	public enum PlayerState
	{
		NORMAL,
		SUPER,
		HACKER,
		BEHIND_7_PROXIES
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
		motionState = MotionState.STILL;
		directionState = DirectionState.NOT_APPLICABLE;
		airborneState = AirborneState.NOT_APPLICABLE;
		
		time = 0;
	}
}
