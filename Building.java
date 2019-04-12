package hearthlandsoptimizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * A building in Hearthlands. Currently a placeholder for later.
 * 
 * @author Amélia @SlayZeKyriarchy
 *
 */
public abstract class Building {
    private static final String PROD_CLASS_MARKER = "prod";
    private static final String COLUMNS_DELIMITER = ";";
    /*
     * Excel decided to make its CSV export use semicolons...
     */
    
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
    
    protected Building() {}
    
    public static void buildAll(String allCsvRecords) {
        ArrayList<String> allSpecifications = new ArrayList<>(
                Arrays.asList(allCsvRecords.split("\\R")));
        
        int size = allSpecifications.size();
        
        HashSet<Building> tempBuildingsSet = new HashSet<>(size + 1, 1);
        
        for(String specifications : allSpecifications) {
            tempBuildingsSet.add(createBuilding(specifications));
        }
        
        allBuildings = Set.copyOf(tempBuildingsSet);
    }
    
    private static Building createBuilding(String specifications) {
        String[] specs       = specifications.split(COLUMNS_DELIMITER);
        String   classMarker = specs[CLASS_COLUMN];
        
        switch (classMarker) {
        case PROD_CLASS_MARKER:
            return new Producer(specs);
        default:
            return new OtherBuilding(specs);
        }
    }
    
    protected final void commonParser(String[] commonSpecs) {
        this.name = commonSpecs[NAME_COLUMN];
        this.type = Type.valueOf(commonSpecs[TYPE_COLUMN]);
        this.category = type.getCategory();
        
        for (Culture culture : Culture.values()) {
            String  marker     = commonSpecs[CULTURE_COLUMN
                    + culture.ordinal()];
            boolean isIncluded = marker.equals("1");
            
            if (isIncluded) {
                cultures.add(culture);
            }
        }
        
    }
    
    protected abstract void particularParser(String[] particularSpecs);
    
}
