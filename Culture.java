package hearthlandsoptimizer;

/**
 * Available cultures in order of apparition when creating a new game.
 * 
 * @author Amélia @SlayZeKyriarchy
 *
 */
public enum Culture {
    WESTERNERS,
    EASTERNERS,
    SOUTHERNERS,
    NORTHERNERS;

    /**
     * Returns the name of this enum constant, converted to lowercase, with the
     * first letter in uppercase.
     */
    @Override
    public String toString() {
        String prettyName = this.name();
        prettyName = prettyName.toLowerCase();
        prettyName = Utils.capitalizeFirstLetter(prettyName);
        
        return prettyName;
    }
}
