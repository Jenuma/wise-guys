package io.whitegoldlabs.wiseguys.component;

import com.badlogic.ashley.core.Component;

public class AirbornStateComponent implements Component
{
	public enum State
	{
		ON_GROUND,
		FALLING,
		JUMPING
	}
	
	public State currentState;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public AirbornStateComponent(State state)
	{
		this.currentState = state;
	}
}
