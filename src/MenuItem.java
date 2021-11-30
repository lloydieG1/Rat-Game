/**
 * @author lloyd
 *
 */
public class MenuItem {
	private ElementType itemType;
	private int replenishTimer;
    private int currentTick = 0;
    private int age = 0;
    private int tickSpeed = Game.FPS/2; //means logic only happens once this many ticks have passed

    public MenuItem(String itemType, int replenishTimer, int age, int currentTick) {
		this.itemType = itemStringToItemType(itemType);
		this.replenishTimer = replenishTimer;
        this.age = age;
        this.replenishTimer = replenishTimer;
	}
	
	/**
	 * converts string description of item to corresponding itemType
	 * 
	 * @param itemString e.g. "bomb"
	 */
	public ElementType itemStringToItemType(String itemString) {
		ElementType returnType;
		switch(itemString) {
			case "bomb":
				returnType = ElementType.Bomb;
				break;
			case "gas":
				returnType = ElementType.Gas;
				break;
			case "sterilize":
				returnType = ElementType.Sterilise;
				break;
			case "poison":
				returnType = ElementType.Poison;
				break;
			case "femaleGenderChange":
				returnType = ElementType.FemaleGenderChange;
				break;
            case "maleGenderChange":
                returnType = ElementType.MaleGenderChange;
                break;
			case "stopSign":
				returnType = ElementType.StopSign;
				break;
			case "deathRat":
				returnType = ElementType.DeathRat;
                break;
			default:
				returnType = null;
		}
		return returnType; 
	}

    public String typeAsString() {
        if(itemType == null) {
            return "-";
        }
        if (itemType.equals(ElementType.Bomb)) {
            return "bomb";
        } else if (itemType.equals(ElementType.Gas)) {
            return "gas";
        }else if (itemType.equals(ElementType.Sterilise)) {
            return "sterilize";
        }
        else if (itemType.equals(ElementType.Poison)) {
            return "poison";
        }
        else if (itemType.equals(ElementType.FemaleGenderChange)) {
            return "femaleGenderChange";
        }
        else if (itemType.equals(ElementType.MaleGenderChange)) {
            return "maleGenderChange";
        }
        else if (itemType.equals(ElementType.StopSign)) {
            return "stopSign";
        }
        else if (itemType.equals(ElementType.DeathRat)) {
            return "deathRat";
        } else {
            return "-";
        }
    }


    protected void tick() {
        System.out.println(age + " " + currentTick);
        currentTick++;
        if(currentTick > tickSpeed) {
            currentTick = 0;
            logic();
        }

    }
    private void logic() {
        age++;
        if (getReplenishTimer() <= age) {
            Game.addItem(getItemType());
            age = 0;
        }
    }

	public ElementType getItemType() {
		return itemType;
	}

	public int getReplenishTimer() {
		return replenishTimer;
	}

    public String asString() {
        return typeAsString() + "," +replenishTimer + "," + currentTick + "," + age;
    }
}