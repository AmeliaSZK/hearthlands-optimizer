package hearthlandsoptimizer;

/**
 * Enumerates the resources involved in production chains. More specifically,
 * contains the resources that are both produced by a {@link Producer} and
 * consumed by another Producer or a House.
 * 
 * They are sorted by order of apparition in the storehouse interface, then
 * Coins, then Trees and Mushrooms by order of apparition in the forestry, and
 * then Flowers because it's a forestry produce reserved to some cultures.
 * 
 * Resources with a plural name got it because that's how it's spelled in the
 * game.
 * 
 * @author Amélia @SlayZeKyriarchy
 *
 */
public enum Resource {
    APPLES, TURNIPS, HONEY, EGGS, FISH, CHEESE, WURST, BREAD, MILK, MEAT, GRAIN,
    FLOUR, HOPS, MEAD, WINE, CIDER, BEER, FURNITURE, CLOTHES, BOOTS, PILLOWS,
    MEDICINE, LOGS, PLANKS, WOOL, COTTON, TEXTILE, LEATHER, FEATHERS, STONE,
    COAL, IRON_ORE, GOLD, IRON, COINS, TREES, MUSHROOMS, FLOWERS;
    
    /**
     * Returns the name of this enum constant, converted to lowercase, with the
     * first letter in uppercase, and with underscores replaced with spaces.
     */
    @Override
    public String toString() {
        String prettyName = this.name();
        prettyName = prettyName.toLowerCase();
        prettyName = prettyName.replace('_', ' ');
        prettyName = Utils.capitalizeFirstLetter(prettyName);
        
        return prettyName;
    }
    
}
