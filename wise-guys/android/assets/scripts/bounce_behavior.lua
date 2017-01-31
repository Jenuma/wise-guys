Bounce_behavior = {}

function Bounce_behavior.execute(thisEntity, startingY)
  local mappers = luajava.bindClass("io.whitegoldlabs.wiseguys.util.Mappers")

  local thisEntityBehaviorComponent = mappers.behavior:get(thisEntity)
  local thisEntityPositionComponent = mappers.position:get(thisEntity)
  
  -----------------
  -- Bouncing Up --
  -----------------
  if thisEntityBehaviorComponent.behaviorState == "BOUNCING_UP" then
    if math.floor(thisEntityPositionComponent.y) == startingY + 4 then
      thisEntityBehaviorComponent.behaviorState = "BOUNCING_DOWN"
    else
      thisEntityPositionComponent.y = thisEntityPositionComponent.y + 0.8
    end
    
  -------------------
  -- Bouncing Down --
  -------------------
  elseif thisEntityBehaviorComponent.behaviorState == "BOUNCING_DOWN" then
    if math.floor(thisEntityPositionComponent.y) < startingY - 2 then
      thisEntityBehaviorComponent.behaviorState = "BOUNCING_BACK_UP"
    else
      thisEntityPositionComponent.y = thisEntityPositionComponent.y - 0.8
    end
    
  ----------------------
  -- Bouncing Back Up --
  ----------------------
  elseif thisEntityBehaviorComponent.behaviorState == "BOUNCING_BACK_UP" then
    if math.floor(thisEntityPositionComponent.y) == startingY then
      local behaviors = luajava.bindClass("io.whitegoldlabs.wiseguys.component.BehaviorComponent")
      
      thisEntityPositionComponent.y = startingY
      thisEntity:remove(behaviors)
    else
      thisEntityPositionComponent.y = thisEntityPositionComponent.y + 0.8
    end
  end
end

return Bounce_behavior
