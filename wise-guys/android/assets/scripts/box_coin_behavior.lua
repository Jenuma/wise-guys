Box_coin_behavior = {}

function Box_coin_behavior.execute(coin, game, startingY)
  local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
  local states = luajava.bindClass("io.whitegoldlabs.wiseguys.component.StateComponent")
  
  local coinPosition = mappers.position:get(coin)
  local coinState = mappers.state:get(coin)
  
  if coinPosition.y < startingY then
    local arrayInstantiator = luajava.bindClass("io.whitegoldlabs.wiseguys.util.ArrayInstantiator")
    
    coinState.enabledState = states.EnabledState.DISABLED
    
    local digit1Entity = luajava.newInstance("com.badlogic.ashley.core.Entity")
    local digit2Entity = luajava.newInstance("com.badlogic.ashley.core.Entity")
    local digit3Entity = luajava.newInstance("com.badlogic.ashley.core.Entity")
    
    local scriptArgs = arrayInstantiator:getNewArray()
    scriptArgs:add(game)
    scriptArgs:add(200)
    scriptArgs:add(digit1Entity)
    scriptArgs:add(digit2Entity)
    scriptArgs:add(digit3Entity)
    scriptArgs:add(coinPosition.x)
    scriptArgs:add(coinPosition.y)
    
    local pointsBehaviorComponent = luajava.newInstance("io.whitegoldlabs.wiseguys.component.BehaviorComponent", "scripts\\points_behavior.lua", scriptArgs)
    pointsBehaviorComponent.behaviorState = "NOT_INITIALIZED"
    game.scriptManager:loadScript("scripts\\points_behavior.lua")
    
    digit1Entity:add(pointsBehaviorComponent)
    digit2Entity:add(pointsBehaviorComponent)
    digit3Entity:add(pointsBehaviorComponent)
    
    game.engine:addEntity(digit1Entity)
    game.engine:addEntity(digit2Entity)
    game.engine:addEntity(digit3Entity)
  end
end

return Box_coin_behavior
