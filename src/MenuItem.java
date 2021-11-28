/**
 * @author lloyd
 */
public class MenuItem {
	private ElementType itemType;
	private int replenishTimer;
    private int currentTick = 0;
    private int age = 0;
    private int tickSpeed = Game.FPS/2; //means logic only happens once this many ticks have passed

    public MenuItem(String itemType, int replenishTimer) {
		this.itemType = itemStringToItemType(itemType);
		this.replenishTimer = replenishTimer;
	}
	
	/*
	 * converts string description of item to corresponding itemType
	 * 
	 * @param itemString e.g. "bomb"
	 */
	public ElementType itemStringToItemType(String itemString) {
		ElementType returnType;
		switch(itemString) {
			case "Bomb":
				returnType = ElementType.Bomb;
				break;
			case "Gas":
				returnType = ElementType.Gas;
				break;
			case "Sterilise":
				returnType = ElementType.Sterilise;
				break;
			case "Poison":
				returnType = ElementType.Poison;
				break;
			case "GenderChange":
				returnType = ElementType.GenderChange;
				break;
			case "NoEntry":
				returnType = ElementType.NoEntry;
				break;
			case "DeathRat":
				returnType = ElementType.DeathRat;
			default:
				returnType = null;
		}
		return returnType; 
	}


    protected void tick() {
        currentTick++;
        if(currentTick > tickSpeed) {
            currentTick = 0;
            logic();
        }

    }
    private void logic() {
        age++;
        if (getReplenishTimer() % age == 0) {
            Game.addItem(getItemType());
        }
    }

	public ElementType getItemType() {
		return itemType;
	}

	public int getReplenishTimer() {
		return replenishTimer;
	}
}