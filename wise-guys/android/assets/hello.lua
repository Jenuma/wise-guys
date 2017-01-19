function execute(playerInventory, sfxCoin)
	sfxCoin:play()
	
	playerInventory.coins = playerInventory.coins + 1
	playerInventory.score = playerInventory.score + 200
end
