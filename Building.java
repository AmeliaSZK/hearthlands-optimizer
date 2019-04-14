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
    
    protected class DependencyChain {
        private final Building owner;
        private boolean        available;
        
        private Float            totalStaff;
        private BuildingMultiset chain;
        
        /**
         * Creates a new dependency chain. If it {@code isAvailable}, the chain
         * will be initialized with 1 count of its owner, and {@code totalStaff}
         * brought to its owner's {@code localStaff}. Otherwise,
         * {@code totalStaff} will be brought to -1 and the chain will stay
         * empty.
         * 
         * @param owner         the {@code Building} depending on this chain.
         * @param canBeComputed if the owner is allowed to compute this chain.
         */
        private DependencyChain(Building owner, Boolean canBeComputed) {
            this.owner = owner;
            chain = new BuildingMultiset();
            available = canBeComputed;
            
            if (canBeComputed) {
                chain.add(owner, 1);
                totalStaff = (float) owner.localStaff;
            } else {
                totalStaff = -1f;
            }
        }
        
        public Float getTotalStaff() {
            if (!available) {
                totalStaff = Producer.UNSUSTAINABLE;
            }
            
            return totalStaff;
        }
        
        public void setTotalStaff(Float totalStaff) {
            if (available) {
                this.totalStaff = totalStaff;
            } else {
//                throw new UnsupportedOperationException("Cannot set total staff"
//                        + "of a Dependency marked unavailable.");
            }
        }
        
        /**
         * Returns the chain, but if {@code isAvailable} is false, the chain
         * will first be emptied.
         * 
         * @return the chain, empty if unavailable.
         */
        public BuildingMultiset getChain() {
            if (!available) {
                chain.clear();
            }
            return chain;
        }
        
        public boolean isAvailable() {
            return available;
        }
        
        public void makeUnavailable() {
            available = false;
            chain.clear();
            totalStaff = -1f;
        }
        
    }
    
    private static final String PROD_CLASS_MARKER = "prod";
    private static final String HEADERS           = "name;category;type;west;east;south;north;local staff;west total staff;east total staff;south total staff;north total staff;west chain size;east chain size;south chain size;north chain size;west full chain;east full chain;south full chain;north full chain;loads";
    
    
    protected static final String COLUMNS_DELIMITER_INPUT = ";";
    /*
     * Excel decided to make its CSV export use semicolons...
     */
    
    protected static final String COLUMNS_DELIMITER_OUTPUT = "\t";
    /*
     * I need to free the semicolon as a delimiter if I want to trick Excel
     * into recieving formulas.
     */
    
    private static final int HEADERS_LINE         = 0;
    private static final int TOTAL_NB_OF_COLUMNS  = 11;
    private static final int NB_OF_COMMON_COLUMNS = 7;
    // Column indexes of the common specifications.
    private static final int NAME_COLUMN    = 0;
    private static final int TYPE_COLUMN    = 1;
    private static final int CULTURE_COLUMN = 2;
    
    private static final int CLASS_COLUMN = 6;
    // Column indexes of the particular specifications
    // For Producers :
    protected static final int STAFF_COLUMN       = 7;
    protected static final int LOADS_COLUMN       = 8;
    protected static final int CONSUMPTION_COLUMN = 9;
    
    protected static final int PRODUCTION_COLUMN = 10;
    // For Other buildings :
    // Nothing.
    
    protected static Set<Building> allBuildings;
    private static Building        marketplace;
    
    public static final String allToString() {
        String result = "";
        
        result += headersToString();
        result += "\n";
        
        for (Building building : allBuildings) {
            result += building;
            result += "\n";
        }
        
        return result;
    }
    
    private static String headersToString() {
        ArrayList<String> headers = new ArrayList<>();
        headers.add("name");
        headers.add("category");
        headers.add("type");
        headers.add("west");
        headers.add("east");
        headers.add("south");
        headers.add("north");
        headers.add("local staff");
        headers.add("west total staff");
        headers.add("east total staff");
        headers.add("south total staff");
        headers.add("north total staff");
        headers.add("west chain size");
        headers.add("east chain size");
        headers.add("south chain size");
        headers.add("north chain size");
        headers.add("west full chain");
        headers.add("east full chain");
        headers.add("south full chain");
        headers.add("north full chain");
        headers.add("loads");
        
        String result = "";
        
        for(String header : headers) {
            boolean isLast = headers.indexOf(header) == headers.size() - 1;
                    
            result += header;
            result += isLast ? "" : COLUMNS_DELIMITER_OUTPUT;
        }
                
        return result;
    }

    public static void buildAll(String allCsvRecords, boolean includeHunters,
            boolean includeDiggers, boolean includeMines,
            boolean includeWoodburner) {
        ArrayList<String> allSpecifications = new ArrayList<String>(
                Arrays.asList(allCsvRecords.split("\\R")));
        
        allSpecifications.remove(HEADERS_LINE);
        int size = allSpecifications.size();
        
        HashSet<Building> tempBuildingsSet = new HashSet<>(size + 1, 1);
        
        for (String specifications : allSpecifications) {
            tempBuildingsSet.add(createBuilding(specifications));
        }
        
        HashSet<Building> buildingsToRemove = new HashSet<>();
        
        for (Building building : tempBuildingsSet) {
            if (building.getType().equals(Type.MARKETPLACE)) {
                marketplace = building;
            }
        }
        
        allBuildings = Set.copyOf(tempBuildingsSet);
    }
    
    private static Building createBuilding(String specifications) {
        ArrayList<String> specs = new ArrayList<>(
                Arrays.asList(specifications.split(COLUMNS_DELIMITER_INPUT)));
        
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
    
    protected static Building getMarketplace() {
        return marketplace;
    }
    
    protected String           name;
    protected Category         category;
    protected Type             type;
    protected int              localStaff;
    /**
     * The cultures that can build {@code this} Building.
     */
    protected EnumSet<Culture> cultures;
    
    protected EnumMap<Culture, DependencyChain> allChains;
    
    protected Building() {}
    
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
        
        allChains = new EnumMap<>(Culture.class);
        
        for (Culture culture : Culture.values()) {
            DependencyChain thisChain = new DependencyChain(this,
                    cultures.contains(culture));
            
            allChains.put(culture, thisChain);
        }
        
    }
    
    private String commonToString() {
        String result = "";
        
        result += name;
        result += COLUMNS_DELIMITER_OUTPUT;
        result += category.getPrettyName();
        result += COLUMNS_DELIMITER_OUTPUT;
        result += type.getPrettyName();
        result += COLUMNS_DELIMITER_OUTPUT;
        for (Culture culture : Culture.values()) {
            result += cultures.contains(culture) ? "VRAI" : "FAUX";
            result += COLUMNS_DELIMITER_OUTPUT;
        }
        result += localStaff;
        result += COLUMNS_DELIMITER_OUTPUT;
        
        for (Culture culture : Culture.values()) {
            result += (int) Math.ceil(this.getTotalStaff(culture));
            result += COLUMNS_DELIMITER_OUTPUT;
        }
        
        for (Culture culture : Culture.values()) {
            result += allChains.get(culture).getChain().size();
            result += COLUMNS_DELIMITER_OUTPUT;
        }
        
        for (Culture culture : Culture.values()) {
            result += allChains.get(culture).getChain();
            result += COLUMNS_DELIMITER_OUTPUT;
        }
        
        return result;
    }
    
    public int getLocalStaff() {
        return localStaff;
    }
    
//    public int getTotalStaff() {
//        return totalStaff;
//    }
    
    public Float getTotalStaff(Culture culture) {
        return allChains.get(culture).totalStaff;
    }
    
    protected Type getType() {
        return type;
    }
    
    protected abstract void particularParser(List<String> particularSpecs);
    
    protected abstract String particularToString();
    
    @Override
    public final String toString() {
        return commonToString() + particularToString();
    }
    
    public boolean isAvailableTo(Culture culture) {
        return cultures.contains(culture);
    }
    
    public String getName() {
        return name;
    }
    
}
