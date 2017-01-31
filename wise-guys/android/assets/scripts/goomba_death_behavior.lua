Goomba_death_behavior = {}

function Goomba_death_behavior.execute(goomba)
  local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")
  local states = luajava.bindClass("io.whitegoldlabs.wiseguys.component.StateComponent")
  
  local goombaBehavior = mappers.behavior:get(goomba)
  
  if goombaBehavior.behaviorState == "DEAD" then
    if mappers.hitbox:has(goomba) then
      local hitboxes = luajava.bindClass("io.whitegoldlabs.wiseguys.component.HitboxComponent")
    
      goomba:remove(hitboxes)
    end
    
    if goombaBehavior.behaviorTime > 1 then
      local goombaStateComponent = mappers.state:get(goomba)
    
      goombaStateComponent.enabledState = states.EnabledState.DISABLED
    end
  end
end

return Goomba_death_behavior
