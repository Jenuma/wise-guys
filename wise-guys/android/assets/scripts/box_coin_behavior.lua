Box_coin_behavior = {}

function Box_coin_behavior.execute(coin, startingY)
  local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
  local states = luajava.bindClass("io.whitegoldlabs.wiseguys.component.StateComponent")
  
  local coinPosition = mappers.position:get(coin)
  local coinState = mappers.state:get(coin)
  
  if coinPosition.y < startingY then
    coinState.enabledState = states.EnabledState.DISABLED
  end
end

return Box_coin_behavior
