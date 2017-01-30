Bricks = {}

function Bricks.execute(bricks, game)
  --if collided by the player from below (copy from box)
    --if player state not NORMAL
      --play brick break sfx
      --animate brick breaking
    --else
      --play bump sfx
      --give bump behavior (copy from box_behavior, possibly use same script)
end

return Bricks
