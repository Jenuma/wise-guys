Box_coin_behavior = {}

function Box_coin_behavior.execute(coin, game, startingY)
  local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
  local states = luajava.bindClass("io.whitegoldlabs.wiseguys.component.StateComponent")
  
  local coinPosition = mappers.position:get(coin)
  local coinState = mappers.state:get(coin)
  
  if coinPosition.y < startingY then
    local arrayInstantiator = luajava.bindClass("io.whitegoldlabs.wiseguys.util.ArrayInstantiator")
    local assetFiles = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Assets")
    local types = luajava.bindClass("io.whitegoldlabs.wiseguys.component.TypeComponent")
    
    local spriteSheet = game.assets.manager:get(assetFiles.spriteSheet)
    
    coinState.enabledState = states.EnabledState.DISABLED
    
    local digit1Entity = luajava.newInstance("com.badlogic.ashley.core.Entity")
    local digit2Entity = luajava.newInstance("com.badlogic.ashley.core.Entity")
    local digit3Entity = luajava.newInstance("com.badlogic.ashley.core.Entity")
    
    local digit1Sprite = luajava.newInstance("com.badlogic.gdx.graphics.g2d.Sprite", spriteSheet, 8, 144, 4, 8)
    local digit2Sprite = luajava.newInstance("com.badlogic.gdx.graphics.g2d.Sprite", spriteSheet, 0, 144, 4, 8)
    local digit3Sprite = luajava.newInstance("com.badlogic.gdx.graphics.g2d.Sprite", spriteSheet, 0, 144, 4, 8)
    
    local digit1PositionComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.PositionComponent", coinPosition.x, coinPosition.y)
	local digit2PositionComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.PositionComponent", coinPosition.x + 5, coinPosition.y)
	local digit3PositionComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.PositionComponent", coinPosition.x + 10, coinPosition.y)
    
    local pointsTypeComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.TypeComponent", types.Type.PRIORITY)
    local pointsStateComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.StateComponent")
    local pointsPhaseComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.PhaseComponent")

	local digit1SpriteComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.SpriteComponent", digit1Sprite)
	local digit2SpriteComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.SpriteComponent", digit2Sprite)
	local digit3SpriteComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.SpriteComponent", digit3Sprite)
    
    local scriptArgs = arrayInstantiator:getNewArray()
    scriptArgs:add(game)
    scriptArgs:add(digit1Entity)
    scriptArgs:add(digit2Entity)
    scriptArgs:add(digit3Entity)
    scriptArgs:add(coinPosition.x)
    scriptArgs:add(coinPosition.y)
    
    local pointsBehaviorComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.BehaviorComponent", "scripts\\points_behavior.lua", scriptArgs)
    game.scriptManager:loadScript("scripts\\points_behavior.lua")
    
    digit1Entity:add(pointsTypeComponent)
    digit1Entity:add(pointsStateComponent)
    digit1Entity:add(digit1PositionComponent)
    digit1Entity:add(pointsPhaseComponent)
    digit1Entity:add(digit1SpriteComponent)
    digit1Entity:add(digit1SpriteComponent)
    digit1Entity:add(pointsBehaviorComponent)
    
    digit2Entity:add(pointsTypeComponent)
    digit2Entity:add(pointsStateComponent)
    digit2Entity:add(digit2PositionComponent)
    digit2Entity:add(pointsPhaseComponent)
    digit2Entity:add(digit2SpriteComponent)
    digit2Entity:add(digit2SpriteComponent)
    digit2Entity:add(pointsBehaviorComponent)
    
    digit3Entity:add(pointsTypeComponent)
    digit3Entity:add(pointsStateComponent)
    digit3Entity:add(digit3PositionComponent)
    digit3Entity:add(pointsPhaseComponent)
    digit3Entity:add(digit3SpriteComponent)
    digit3Entity:add(digit3SpriteComponent)
    digit3Entity:add(pointsBehaviorComponent)
    
    game.engine:addEntity(digit1Entity)
    game.engine:addEntity(digit2Entity)
    game.engine:addEntity(digit3Entity)
  end
end

return Box_coin_behavior
