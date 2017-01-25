Goal = {}

function Goal.execute(thisEntity, game, sfxStageClear)
	local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
	local types = luajava.bindClass("io.whitegoldlabs.wiseguys.component.TypeComponent")
	
	local collisionComponent = mappers.collision:get(thisEntity)
	local collidingEntity = collisionComponent.collidingWith
	local collidingEntityTypeComponent = mappers.type:get(collidingEntity)
	
	if collidingEntityTypeComponent.type == types.Type.PLAYER then
		local thread = luajava.bindClass("java.lang.Thread") 
		local worlds = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Worlds")
		
		sfxStageClear:play()
		thread:sleep(6000)
		game.wasSleeping = true
		
		local newMainMenuScreen = luajava.newInstance("io.whitegoldlabs.wiseguys.view.MainMenuScreen", game)
		game.currentScreen:dispose()
		game.currentScreen = newMainMenuScreen
		game:setScreen(newMainMenuScreen)
		
		game.engine:removeAllEntities()
		game.scriptManager = luajava.newInstance("io.whitegoldlabs.wiseguys.util.ScriptManager")
		worlds:unloadWorldEntities()
	end
end

return Goal
