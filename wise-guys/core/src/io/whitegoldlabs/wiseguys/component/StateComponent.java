package io.whitegoldlabs.wiseguys.component;

import com.badlogic.ashley.core.Component;

public class StateComponent implements Component
{
	public enum State
	{
		ON_GROUND,
		RUNNING,
		IN_AIR,
		DEAD
	}
	
	public State currentState;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public StateComponent(State state)
	{
		this.currentState = state;
	}
}
