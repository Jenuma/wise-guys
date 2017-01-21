Teleport = {}

function Teleport.execute(game, camera)
	newGameScreen = luajava.newInstance("io.whitegoldlabs.wiseguys.view.GameScreen", game, camera, "world1-1a", 48, 128)
	game.currentScreen:dispose()
	game.currentScreen = newGameScreen
	game:setScreen(newGameScreen)
end

return Teleport
