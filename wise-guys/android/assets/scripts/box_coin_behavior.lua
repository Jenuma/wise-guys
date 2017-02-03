Box_coin_behavior = {}

function Box_coin_behavior.execute(coin, game, startingY)
  local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
  local states = luajava.bindClass("io.whitegoldlabs.wiseguys.component.StateComponent")
  
  local coinPosition = mappers.position:get(coin)
  local coinState = mappers.state:get(coin)
  
  if coinPosition.y < startingY then
    local arrayInstantiator = luajava.bindClass("io.whitegoldlabs.wiseguys.util.ArrayInstantiator")
    
    coinState.enabledState = states.EnabledState.DISABLED
    
    local pointsEntity = luajava.newInstance("com.badlogic.ashley.core.Entity")
    
    local scriptArgs = arrayInstantiator:getNewArray()
    scriptArgs:add(game)
    scriptArgs:add(coinPosition.x)
    scriptArgs:add(coinPosition.y)
    
    local pointsBehaviorComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.BehaviorComponent", "scripts\\points_glyph_behavior.lua", scriptArgs)
    game.scriptManager:loadScript("scripts\\points_glyph_behavior.lua")
    pointsEntity:add(pointsBehaviorComponent)
    
    game.engine:addEntity(pointsEntity)
  end
end

return Box_coin_behavior
