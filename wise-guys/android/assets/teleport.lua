Teleport = {}

function Teleport.execute(game, camera, player, engine)
	luajava.newInstance("io.whitegoldlabs.wiseguys.view.GameScreen", game, camera, player, engine, "world1-1a", 48, 64)
end

return Teleport
