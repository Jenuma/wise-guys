Jules_death = {}

function Jules_death.execute(game)
	local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
	local assetFiles = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Assets")
	local playerStates = luajava.bindClass("io.whitegoldlabs.wiseguys.component.PlayerComponent")
	
	local julesDeathSfx = game.assets.manager:get(assetFiles.sfxJulesDeath)
		
	local player = mappers.player:get(game.player)
	
	julesDeathSfx:play()
	player.lives = player.lives - 1
	
	if player.playerState ~= playerStates.PlayerState.NORMAL then
		game:powerdownNormalJules()
	end
	
	game.engine:removeAllEntities()
	
	if player.lives > 0 then
		newScreen = luajava.newInstance("io.whitegoldlabs.wiseguys.view.WorldIntroScreen", game, game.currentWorld)
	else
		newScreen = luajava.newInstance("io.whitegoldlabs.wiseguys.view.GameOverScreen", game)
	end
	
	game.currentScreen:dispose()
	game.currentScreen = newScreen
	game:setScreen(newScreen)
end

return Jules_death
