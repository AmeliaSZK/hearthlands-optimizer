package hearthlandsoptimizer;

import java.util.EnumSet;

/**
 * Available buildings sorted by :
 * 
 * 1) Order of apparation from left to right in the game.
 * 
 * 2) Order of Culture.
 * 
 * 3) At no point should a culture be able to see an available building appear
 * out of order from its in-game presentation.
 * 
 * 
 * @author Amélia @SlayZeKyriarchy
 *
 */
public enum Type {
    ROAD("Road", Category.ROAD),
    HOUSE("House", Category.HOUSE),
    WOODCUTTER("Woodcutter's lodge", Category.RAW_MATERIALS),
    FORESTER("Forester's lodge", Category.RAW_MATERIALS),
    MASONRY("Masonry", Category.RAW_MATERIALS),
    COAL_DIGGER("Coal digger's lodge", Category.RAW_MATERIALS),
    IRON_DIGGER("Iron digger's lodge", Category.RAW_MATERIALS),
    GOLD_DIGGER("Gold digger's lodge", Category.RAW_MATERIALS),
    QUARRY("Quarry", Category.RAW_MATERIALS),
    COAL_MINE("Coal mine", Category.RAW_MATERIALS),
    IRON_MINE("Iron mine", Category.RAW_MATERIALS),
    GOLD_MINE("Gold mine", Category.RAW_MATERIALS),
    GROWER("Grower's lodge", Category.FARMING),
    OAST_HOUSE("Oast House", Category.FARMING),
    BEEKEEPER("Beekeeper's lodge", Category.FARMING),
    AVIARY("Aviary", Category.HUSBANDRY),
    SHEPERD("Sheperd's lodge", Category.HUSBANDRY),
    PIGSTRY("Pigstry", Category.HUSBANDRY),
    HUNTER("Hunter's lodge", Category.FOOD_INDUSTRY),
    MILL("Mill", Category.FOOD_INDUSTRY),
    BAKERY("Bakery", Category.FOOD_INDUSTRY),
    FISHING_QUAY("Fishing quay", Category.FOOD_INDUSTRY),
    SMOKEHOUSE("Smokehouse", Category.FOOD_INDUSTRY),
    DIARY("Diary", Category.FOOD_INDUSTRY),
    WELL("Well", Category.FOOD_INDUSTRY),
    BEER_BREWERY("Beer brewery", Category.ALCOHOL_INDUSTRY),
    CIDER_BREWERY("Cider brewery", Category.ALCOHOL_INDUSTRY),
    WINERY("Winery", Category.ALCOHOL_INDUSTRY),
    MEAD_BREWERY("Mead brewery", Category.ALCOHOL_INDUSTRY),
    SAWMILL("Sawmill", Category.INDUSTRY),
    CARPENTER("Carpenter's shop", Category.INDUSTRY),
    SHOEMAKER("Shoemaker's shop", Category.INDUSTRY),
    WEAVER("Weaver's shop", Category.INDUSTRY),
    CLOTHIER("Clothier's shop", Category.INDUSTRY),
    PILLOW_MAKER("Pillow maker's shop", Category.INDUSTRY),
    HERBALIST("Herbalist's shop", Category.INDUSTRY),
    WOODBURNER("Woodburner's shop", Category.INDUSTRY),
    SMELTERY("Smeltery", Category.INDUSTRY),
    MINT("Mint", Category.INDUSTRY),
    STORAGE_YARD("Storage yard", Category.STORAGE_DISTRIBUTION),
    PEDDLER("Peddler's tent", Category.STORAGE_DISTRIBUTION),
    APOTHECARY("Apothecary", Category.STORAGE_DISTRIBUTION),
    MARKETPLACE("Marketplace", Category.STORAGE_DISTRIBUTION);
    
    public static final EnumSet<Type> DIGGERS = EnumSet.of(MASONRY, COAL_DIGGER,
            IRON_DIGGER, GOLD_DIGGER);
    public static final EnumSet<Type> MINES   = EnumSet.of(QUARRY, COAL_MINE,
            IRON_MINE, GOLD_MINE);
    
    private final String   prettyName;
    private final Category category;
    
    private Type(String prettyName, Category category) {
        this.prettyName = prettyName;
        this.category = category;
    }
    
    public String getPrettyName() {
        return prettyName;
    }
    
    public Category getCategory() {
        return category;
    }
    
    /**
     * Tests wether {@code this} is a Quarry, Coal Mine, Iron Mine, or Gold
     * Mine.
     * 
     * @return if {@code this} is a mine.
     */
    public boolean isMine() {
        return MINES.contains(this);
    }
    
    /**
     * Tests wether {@code this} is a Masonry, Coal Digger, Iron Digger, or Gold
     * Digger.
     * 
     * @return if {@code this} is a digger.
     */
    public boolean isDigger() {
        return DIGGERS.contains(this);
    }
    
}
