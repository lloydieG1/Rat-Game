/**
 * Description.
 *
 * @author Lloyd
 * @author William Randle
 */
public class MenuItem {
    private ElementType itemType;
    private int replenishTimer;
    private int currentTick = 0;
    private int age = 0;
    private int tickSpeed = Game.FPS / 2; //means logic only happens once this many ticks have passed

    /**
     * constructs a menu item
     *
     * @param itemType Type of item in the sidebar
     * @param replenishTimer int the time it takes to add to the side bar
     * @param age int the current age of this menuitem
     */
    public MenuItem(String itemType, int replenishTimer, int age) {
        this.itemType = itemStringToItemType(itemType);
        this.age = age;
        this.replenishTimer = replenishTimer;
    }

    /**
     * Converts string description of item to corresponding itemType.
     *
     * @param itemString e.g. "bomb"
     */
    public ElementType itemStringToItemType(String itemString) {
        ElementType returnType;
        switch (itemString) {
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

    /**
     * gives a string version of the type
     *
     * @return String the itemtype as a string
     */
    public String typeAsString() {
        if (itemType == null) {
            return "-";
        }
        if (itemType.equals(ElementType.Bomb)) {
            return "bomb";
        } else if (itemType.equals(ElementType.Gas)) {
            return "gas";
        } else if (itemType.equals(ElementType.Sterilise)) {
            return "sterilize";
        } else if (itemType.equals(ElementType.Poison)) {
            return "poison";
        } else if (itemType.equals(ElementType.FemaleGenderChange)) {
            return "femaleGenderChange";
        } else if (itemType.equals(ElementType.MaleGenderChange)) {
            return "maleGenderChange";
        } else if (itemType.equals(ElementType.StopSign)) {
            return "stopSign";
        } else if (itemType.equals(ElementType.DeathRat)) {
            return "deathRat";
        } else {
            return "-";
        }
    }


    /**
     * the logic for a menu item
     */
    protected void tick() {
        currentTick++;
        if (currentTick > tickSpeed) {
            currentTick = 0;
            logic();
        }
    }

    /**
     * adds item to the sidebar if enough time has passed
     */
    private void logic() {
        age++;
        if (getReplenishTimer() <= age) {
            Game.addItem(getItemType());
            age = 0;
        }
    }

    /**
     *
     * @return ElementType type of the element
     */
    public ElementType getItemType() {
        return itemType;
    }

    /**
     *
     * @return int the time to replenish the item
     */
    public int getReplenishTimer() {
        return replenishTimer;
    }

    /**
     *
     * @return String the side menu item in string form so it can be parsed
     */
    public String asString() {
        return typeAsString() + "," + replenishTimer + "," + currentTick + "," + age;
    }
}