package hearthlandsoptimizer;

/**
 * Enumerates the resources involved in production chains. More specifically,
 * contains the resources that are both produced by a {@link Producer} and
 * consumed by another Producer or a House.
 * 
 * Ordering subject to changes; do <i>not</i> rely on it.
 * 
 * @author Amélia @SlayZeKyriarchy
 *
 */
public enum Resource {
    /*
     * Current order is roughly sorted by order of apparition in the storehouse
     * interface.
     */
    APPLES, TURNIPS, HONEY, EGGS, FISH, CHEESE, WURST, BREAD, MILK, MEAT, GRAIN,
    FLOUR, HOPS, MEAD, WINE, CIDER, BEER, FURNITURE, CLOTHES, BOOTS, PILLOWS,
    MEDICINE, LOGS, PLANKS, WOOL, COTTON, TEXTILE, LEATHER, FEATHERS, STONE,
    COAL, IRON_ORE, GOLD, IRON, TREES, COINS, MUSH, FLOWERS;

}
