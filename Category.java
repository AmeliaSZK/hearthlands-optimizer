package hearthlandsoptimizer;

/**
 * The different construction tabs in the game, in order of apparition from left
 * to right.
 * 
 * @author Amélia @SlayZeKyriarchy
 *
 */
public enum Category {
    ROAD("Road"),
    HOUSE("House"),
    RAW_MATERIALS("Raw Materials"),
    FARMING("Farming"),
    HUSBANDRY("Husbandry"),
    FOOD_INDUSTRY("Food Industry"),
    ALCOHOL_INDUSTRY("Alcohol Industry"),
    INDUSTRY("Industry"),
    STORAGE_DISTRIBUTION("Storage & Distribution"),
    INFRASTRUCTURE("Infrastructure"),
    BEAUTIFICATION("Beautification"),
    MILITARY("Military"),
    FORTIFICATIONS("Fortifications");
    
    private final String prettyName;
    
    private Category(String prettyName) {
        this.prettyName = prettyName;
    }

    public String getPrettyName() {
        return prettyName;
    }
    
}
