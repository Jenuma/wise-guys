package io.whitegoldlabs.wiseguys.util;

import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;

import com.badlogic.gdx.utils.Array;

import io.whitegoldlabs.wiseguys.component.ScriptComponent;

public class ScriptManager
{
	// ---------------------------------------------------------------------------------|
	// Fields                                                                           |
	// ---------------------------------------------------------------------------------|
	private Globals globals;
	private Array<String> loadedScripts;
	
	// ---------------------------------------------------------------------------------|
	// Constructor                                                                      |
	// ---------------------------------------------------------------------------------|
	public ScriptManager()
	{
		this.globals = JsePlatform.standardGlobals();
		this.loadedScripts = new Array<>();
	}
	
	// ---------------------------------------------------------------------------------|
	// Public Methods                                                                   |
	// ---------------------------------------------------------------------------------|
	public void loadScript(String scriptName)
	{
		if(!loadedScripts.contains(scriptName, false))
		{
			globals.loadfile(scriptName).call();
			loadedScripts.add(scriptName);
		}
	}
	
	public Array<String> getLoadedScripts()
	{
		return this.loadedScripts;
	}
	
	public void executeScriptImmediately(ScriptComponent script)
	{
		globals.get(script.moduleName).get("execute").invoke(script.args);
	}
}
