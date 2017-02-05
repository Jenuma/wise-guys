Points_behavior = {}

function Points_behavior.execute(game, digit1, digit2, digit3, x, y)
  local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
  
  local pointsBehavior = mappers.behavior:get(digit1)
  
  local digit1Position = mappers.position:get(digit1)
  local digit2Position = mappers.position:get(digit2)
  local digit3Position = mappers.position:get(digit3)
  
  digit1Position.y = digit1Position.y + 0.1
  digit2Position.y = digit2Position.y + 0.1
  digit3Position.y = digit3Position.y + 0.1
  
  if pointsBehavior.behaviorTime > 3 then
  	local states = luajava.bindClass("io.whitegoldlabs.wiseguys.component.StateComponent")
  
  	local pointsState = mappers.state:get(digit1)
  	
  	pointsState.enabledState = states.EnabledState.DISABLED
  end
end

return Points_behavior
