package io.whitegoldlabs.wiseguys.util;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

public class ArrayInstantiator
{
	public static Array<Object> getNewArray()
	{
		return new Array<Object>();
	}
	
	public static Animation<Sprite> getNewAnimation(float duration, Array<Sprite> frames, Animation.PlayMode playMode)
	{
		return new Animation<Sprite>(duration, frames, playMode);
	}
}
