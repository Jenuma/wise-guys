package io.whitegoldlabs.wiseguys.component;

import com.badlogic.ashley.core.Component;

public class PickupComponent implements Component
{
	public enum Pickup
	{
		COIN,
		CONNOLI,
		FINGERLESS_GLOVE,
		ONION,
		ONE_UP
	}
	
	public Pickup pickup;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public PickupComponent(Pickup pickup)
	{
		this.pickup = pickup;
	}
}
