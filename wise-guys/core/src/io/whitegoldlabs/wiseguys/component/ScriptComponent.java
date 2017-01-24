package io.whitegoldlabs.wiseguys.component;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;

public class ScriptComponent implements Component
{
	// ---------------------------------------------------------------------------------|
	// Fields                                                                           |
	// ---------------------------------------------------------------------------------|
	public String scriptName;
	public String moduleName;
	public LuaValue[] args;
	public boolean collidable;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public ScriptComponent(boolean collidable, String scriptName, Array<Object> args)
	{
		this.collidable = collidable;
		
		this.scriptName = scriptName;
		this.moduleName = scriptName.substring(8, 9).toUpperCase() +
			scriptName.substring(9, scriptName.indexOf('.'));
		
		this.args = new LuaValue[args.size];
		
		for(int i = 0; i < args.size; i++)
		{
			this.args[i] = CoerceJavaToLua.coerce(args.get(i));
		}
	}
}
