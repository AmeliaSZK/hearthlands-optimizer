package hearthlandsoptimizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A building in Hearthlands. Currently a placeholder for later.
 * 
 * @author Amélia @SlayZeKyriarchy
 *
 */
public abstract class Building {
    private static final String   PROD_CLASS_MARKER = "prod";
    protected static final String COLUMNS_DELIMITER = ";";
    /*
     * Excel decided to make its CSV export use semicolons...
     */
    
    private static final int TOTAL_NB_OF_COLUMNS  = 11;
    private static final int NB_OF_COMMON_COLUMNS = 7;
    // Column indexes of the common specifications.
    private static final int NAME_COLUMN    = 0;
    private static final int TYPE_COLUMN    = 1;
    private static final int CULTURE_COLUMN = 2;
    private static final int CLASS_COLUMN   = 6;
    
    // Column indexes of the particular specifications
    // For Producers :
    protected static final int STAFF_COLUMN       = 7;
    protected static final int LOADS_COLUMN       = 8;
    protected static final int CONSUMPTION_COLUMN = 9;
    protected static final int PRODUCTION_COLUMN  = 10;
    // For Other buildings :
    // Nothing.
    
    protected static Set<Building> allBuildings;
    
    protected String           name;
    protected Category         category;
    protected Type             type;
    protected EnumSet<Culture> cultures;
    protected int              localStaff;
    protected int              totalStaff;
    
    protected EnumMap<Culture, DependencyChain> allChains;
    
    protected Building() {}
    
    public static void buildAll(String allCsvRecords, boolean includeHunters,
            boolean includeDiggers, boolean includeMines) {
        ArrayList<String> allSpecifications = new ArrayList<String>(
                Arrays.asList(allCsvRecords.split("\\R")));
        
        int size = allSpecifications.size();
        
        HashSet<Building> tempBuildingsSet = new HashSet<>(size + 1, 1);
        
        for (String specifications : allSpecifications) {
            tempBuildingsSet.add(createBuilding(specifications));
        }
        
        HashSet<Building> buildingsToRemove = new HashSet<>();
        
        for (Building building : tempBuildingsSet) {
            Type thisType = building.getType();
            
            if (!includeHunters && thisType.equals(Type.HUNTER)) {
                buildingsToRemove.add(building);
                
            } else if (!includeDiggers && (thisType.equals(Type.COAL_DIGGER)
                    || thisType.equals(Type.GOLD_DIGGER)
                    || thisType.equals(Type.IRON_DIGGER)
                    || thisType.equals(Type.MASONRY))) {
                buildingsToRemove.add(building);
                
            } else if (!includeMines && (thisType.equals(Type.COAL_MINE)
                    || thisType.equals(Type.GOLD_MINE)
                    || thisType.equals(Type.IRON_MINE)
                    || thisType.equals(Type.QUARRY))) {
                buildingsToRemove.add(building);
                
            }
        }
        
        tempBuildingsSet.removeAll(buildingsToRemove);
        
        allBuildings = Set.copyOf(tempBuildingsSet);
    }
    
    public static final String allToString() {
        String result = "";
        
        for (Building building : allBuildings) {
            result += building;
            result += "\n";
        }
        
        for (Building building : allBuildings) {
            if (building instanceof Producer) {
                Producer producer = (Producer) building;
                result += producer.getCompleteChain().toString();
                result += "\n";
            }
        }
        
        return result;
    }
    
    private static Building createBuilding(String specifications) {
        ArrayList<String> specs = new ArrayList<>(
                Arrays.asList(specifications.split(COLUMNS_DELIMITER)));
        
        while (specs.size() < TOTAL_NB_OF_COLUMNS) {
            specs.add("");
        }
        /*
         * The .split won't capture empty columns, so we have to add them. Also,
         * this means we can only leave empty values at the end of a record.
         */
        
        String classMarker = specs.get(CLASS_COLUMN);
        
        switch (classMarker) {
        case PROD_CLASS_MARKER:
            return new Producer(specs);
        default:
            return new OtherBuilding(specs);
        }
    }
    
    protected Type getType() {
        return type;
    }
    
    protected final void commonParser(List<String> specs) {
        this.name = specs.get(NAME_COLUMN);
        this.type = Type.valueOf(specs.get(TYPE_COLUMN));
        this.category = type.getCategory();
        
        cultures = EnumSet.noneOf(Culture.class);
        for (Culture culture : Culture.values()) {
            String  marker     = specs.get(CULTURE_COLUMN + culture.ordinal());
            boolean isIncluded = marker.equals("1");
            
            if (isIncluded) {
                cultures.add(culture);
            }
        }
        
        this.localStaff = Integer.valueOf(specs.get(STAFF_COLUMN));
        this.totalStaff = localStaff;
        
    }
    
    @Override
    public final String toString() {
        return commonToString() + particularToString();
    }
    
    private String commonToString() {
        String result = "";
        
        result += name;
        result += COLUMNS_DELIMITER;
        result += category.getPrettyName();
        result += COLUMNS_DELIMITER;
        result += type.getPrettyName();
        result += COLUMNS_DELIMITER;
        for (Culture culture : Culture.values()) {
            result += cultures.contains(culture) ? "VRAI" : "FAUX";
            result += COLUMNS_DELIMITER;
        }
        result += localStaff;
        result += COLUMNS_DELIMITER;
        result += getTotalStaff();
        result += COLUMNS_DELIMITER;
        
        return result;
    }
    
    public int getLocalStaff() {
        return localStaff;
    }
    
    public int getTotalStaff() {
        return totalStaff;
    }
    
    protected abstract String particularToString();
    
    protected abstract void particularParser(List<String> particularSpecs);
    
    protected class DependencyChain {
        private final Building owner;
        
        private Float            totalStaff;
        private ProducerMultiset chain;
        
        private DependencyChain(Building owner) {
            this.owner = owner;
        }
    }
    
}
