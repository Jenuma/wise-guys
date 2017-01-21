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
	private ScriptComponent scriptToExecute;
	
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
	
	public void setScriptToExecute(ScriptComponent script)
	{
		this.scriptToExecute = script;
	}
	
	public void executeScriptIfReady()
	{
		if(scriptToExecute != null)
		{
			globals.get(scriptToExecute.moduleName).get("execute").invoke(scriptToExecute.args);
			scriptToExecute = null;
		}
	}
}
