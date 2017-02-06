Player_projectile_behavior = {}

function Player_projectile_behavior.execute(projectile, game)
  local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
  
  local projectileBehavior = mappers.behavior:get(projectile)
  
  if projectileBehavior.behaviorTime > 5 then
    local states = luajava.bindClass("io.whitegoldlabs.wiseguys.component.StateComponent")
    local projectileState = mappers.state:get(projectile)
    
    projectileState.enabledState = states.EnabledState.DISABLED
  end
end

return Player_projectile_behavior
