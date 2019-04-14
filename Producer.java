package hearthlandsoptimizer;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * A building that produce {@link Resource}.
 * 
 * @author Amélia @SlayZeKyriarchy
 *
 */
public class Producer extends Building {
    
    private static final String COTTON_MARKER       = "Cotton";
    private static final String MINES_BURNER_MARKER = "Mines & Burner";
    private static final String MINES_MARKER        = "Mines";
    private static final String DIGGERS_MARKER      = "Diggers";
    private static final String CHICKENS_MARKER     = "Chickens";
    private static final String MARKET_MARKER       = "Market";
    private static final String HUNT_MARKER         = "Hunt";
    private static final String PIGS_MARKER         = "Pigs";
    private static final int    MAX_RECURSIVE_CALLS = 1;
    private static final Float  UNCOMPUTED          = 0f;
    public static final Float   UNSUSTAINABLE       = -1f;
    
    private int              loadsPerYear;
    private ResourceMultiset consumption;
    private ResourceMultiset production;
    private EnumSet<Type>    excludedTypes;
    private boolean          marketAllowed;
    
    private int nbOfRecursiveCalls;
    
    protected Producer(List<String> specs) {
        commonParser(specs);
        particularParser(specs);
    }
    
    @Override
    protected void particularParser(List<String> particularSpecs) {
        this.loadsPerYear = Integer.valueOf(particularSpecs.get(LOADS_COLUMN));
        
        consumption = new ResourceMultiset();
        parseResources(consumption, particularSpecs.get(CONSUMPTION_COLUMN));
        production = new ResourceMultiset();
        parseResources(production, particularSpecs.get(PRODUCTION_COLUMN));
        
        for (Culture culture : cultures) {
            allChains.get(culture).setTotalStaff(UNCOMPUTED);
            allChains.get(culture).getChain().clear();
        }
        
        excludedTypes = EnumSet.noneOf(Type.class);
        fillExcludedTypesSet();
        
        marketAllowed = name.endsWith(MARKET_MARKER);
    }
    
    private void fillExcludedTypesSet() {
        // Default exclusions :
        excludedTypes.add(Type.HUNTER);
        excludedTypes.addAll(Type.MINES);
        excludedTypes.add(Type.WOODBURNER);
        /*
         * In this method, code commented out represents exclusions that were
         * included in the defaults right above.
         */
        
        if (type == Type.SMOKEHOUSE || type == Type.SHOEMAKER) {
            if (name.endsWith(PIGS_MARKER)) {
//                excludedTypes.add(Type.HUNTER);
            } else if (name.endsWith(HUNT_MARKER)) {
                excludedTypes.add(Type.PIGSTRY);
                excludedTypes.remove(Type.HUNTER);
            } else if (name.endsWith(MARKET_MARKER)) {
//                excludedTypes.add(Type.HUNTER);
                excludedTypes.add(Type.PIGSTRY);
            }
        }
        
        if (type == Type.PILLOW_MAKER) {
            if (name.endsWith(CHICKENS_MARKER)) {
//                excludedTypes.add(Type.HUNTER);
            } else if (name.endsWith(HUNT_MARKER)) {
                excludedTypes.add(Type.AVIARY);
                excludedTypes.remove(Type.HUNTER);
            } else if (name.endsWith(MARKET_MARKER)) {
//                excludedTypes.add(Type.HUNTER);
                excludedTypes.add(Type.AVIARY);
            }
        }
        
        if (type == Type.SMELTERY || type == Type.MINT) {
            if (name.endsWith(DIGGERS_MARKER)) {
//                excludedTypes.addAll(Type.MINES);
//                excludedTypes.add(Type.WOODBURNER);                
            } else if (name.endsWith(MINES_MARKER)) {
                excludedTypes.removeAll(Type.MINES);
                excludedTypes.addAll(Type.DIGGERS);
            } else if (name.endsWith(MINES_BURNER_MARKER)) {
                excludedTypes.removeAll(Type.MINES);
                excludedTypes.addAll(Type.DIGGERS);
                excludedTypes.remove(Type.WOODBURNER);
                excludedTypes.add(Type.COAL_MINE);
            }
        }
        
    }
    
    private void parseResources(ResourceMultiset direction,
            String specifications) {
        if (!specifications.isBlank()) {
            String[] specs = specifications.split(" +");
            assert specs.length
                    % 2 == 0 : "Needs an even number of fields in Consumption and Production columns.";
            
            // Notice the i=i+2 instead of i++
            for (int i = 0; i < specs.length - 1; i = i + 2) {
                Float    count    = Float.valueOf(specs[i]) * loadsPerYear;
                Resource resource = Resource.valueOf(specs[i + 1]);
                direction.add(resource, count);
            } // End for all pairs of fields
        } // End if specifications aren't empty
    } // End parseResources
    
    /**
     * Not yet correct, as it doesn't take into account the required amount of
     * resources.
     * 
     * @param culture The culture for in which to choose the suppliers.
     * 
     * @throws AssertError if there is a loop in the production chain.
     */
    public void computeCompleteChain(Culture culture) {
        
        DependencyChain  thisDependency = allChains.get(culture);
        BuildingMultiset thisChain      = thisDependency.getChain();
        
        nbOfRecursiveCalls++;
        assert nbOfRecursiveCalls <= MAX_RECURSIVE_CALLS : String.format(
                "There was a loop when computing chain for %s!", this.name);
        
        if (!consumption.isEmpty() || !this.isAvailableTo(culture)) {
            /*
             * An empty consumption is the halt condition for this recursive
             * exercize. computeCompleteChain() calls getCompleteChain(), which
             * in turn will call computeCompleteChain() if the chain hasn't yet
             * been computed.
             * 
             * This recursive loop should finish as long as there is no cycle in
             * the production scheme. Ie, if A needs B, B needs C, and C needs
             * A, there is a cycle.
             * 
             * To guard against this, nbOfRecursiveCalls and MAX_RECURSIVE_CALLS
             * were introduced. nbOfRecursiveCalls will increment every time
             * this method is called, and checked against MAX_RECURSIVE_CALLS.
             * If the count is more than the max, then an AssertError will be
             * thrown. The count is set back to 0 at the end of this method.
             */
            for (Entry<Resource, Float> supply : consumption.entrySet()) {
                Resource      supplyNeeded    = supply.getKey();
                Float         countNeeded     = supply.getValue();
                Set<Producer> suppliers;
                Float         suppliersNeeded = 0f;
                
                suppliers = findAllAvailableSuppliers(supplyNeeded, culture);
                reduceToOnlyOneSupplier(suppliers, culture, supplyNeeded);
                assignTheUniqueSupplier(suppliers, thisChain, culture,
                        countNeeded, supplyNeeded, thisDependency);
            } // End for each supply.
        } // End if consumption isn't empty.
        
        thisChain.add(this, 1);
        nbOfRecursiveCalls = 0;
        
    }
    
    private void assignTheUniqueSupplier(Set<Producer> suppliers,
            BuildingMultiset thisChain, Culture culture, Float countNeeded,
            Resource supplyNeeded, DependencyChain thisDependency) {
        
        if (suppliers.isEmpty() == false) {
            for (Producer producer : suppliers) {
                
                Float suppliesProduced = producer.getProduction()
                        .get(supplyNeeded);
                
                Float suppliersNeeded = countNeeded / suppliesProduced;
                
                thisChain.addAll(producer.getCompleteChain(culture),
                        suppliersNeeded);
            }
            
        } else if (marketAllowed) {
            thisChain.add(getMarketplace(), 0f);
            /*
             * If no supplier was found, we put the Marketplace in the chain, if
             * allowed to, but with a count of 0 so that it doesn't increase the
             * staff needed. This is because only a few markets are needed
             * in-game to supply the whole city, and their available supplies is
             * variable.
             */
            
        } else {
            // If no supplier is available :
            thisChain.clear();
            thisDependency.setTotalStaff(UNSUSTAINABLE);
            thisDependency.makeUnavailable();
        }
        
    }
    
    private void reduceToOnlyOneSupplier(Set<Producer> suppliers,
            Culture culture, Resource supplyNeeded) {
        
        suppliers.removeIf(thisSupplier -> culture == Culture.EASTERNERS
                && supplyNeeded == Resource.TEXTILE
                && thisSupplier.getName().startsWith(COTTON_MARKER)
                && thisSupplier.getType() == Type.WEAVER);
        /*
         * Easterners can get Textile from either Cotton Growers, or from Sheep
         * Sheperd, and I feel the sheperds have a better space efficiency.
         * 
         * The Type filtered is Weaver instead of Grower, because the Textile
         * searcher will look for a Cotton Weaver, not a Cotton Grower.
         */
        
        suppliers.removeIf(thisSupplier -> supplyNeeded == Resource.MEAT
                && thisSupplier.getType() == Type.HUNTER
                && !thisSupplier.getName().startsWith("Any"));
        
        if (suppliers.size() > 1) {
            System.err.println(String.format(
                    "More than 1 available supplier of %s found for %s in culture %s!",
                    supplyNeeded, this.name, culture));
            /*
             * Print an error message if there is still more than 1 supplier.
             * I'll just keep adding tests here on an ad hoc basis until there's
             * no no more error messages.
             */
        }
    }
    
    private Set<Producer> findAllAvailableSuppliers(Resource supplyNeeded,
            Culture culture) {
        
        HashSet<Producer> suppliers = new HashSet<>();
        
        for (Building building : allBuildings) {
            if (building instanceof Producer
                    && building.isAvailableTo(culture)) {
                
                Producer producer = (Producer) building;
                if (producer.isProducing(supplyNeeded)) {
                    suppliers.add(producer);
                }
            }
        }
        
        suppliers.removeIf(e -> e.getTotalStaff(culture) == UNSUSTAINABLE);
        suppliers.removeIf(e -> excludedTypes.contains(e.getType()));
        
        return suppliers;
    }
    
    public void computeTotalStaff(Culture culture) {
        if (isCompleteChainComputed(culture) == false) {
            computeCompleteChain(culture);
        }
        
        DependencyChain thisDependency = allChains.get(culture);
        
        Float cumulatedStaff = 0f;
        /*
         * This instance is included in completeChain, so we initialize to 0.
         */
        
        for (Entry<Building, Float> supplierEntry : thisDependency.getChain()
                .entrySet()) {
            /*
             * Here, we sum the localStaff values of each supplier instead of
             * their totalStaff because we are going through every supplier in
             * the chain.
             * 
             * We would need the totalStaff if we were only going through this
             * Producer's direct suppliers, not this suppliers' suppliers and so
             * on.
             */
            
            Building supplier        = supplierEntry.getKey();
            Float    suppliersNeeded = supplierEntry.getValue();
            
            cumulatedStaff += supplier.getLocalStaff() * suppliersNeeded;
        }
        
        thisDependency.setTotalStaff(cumulatedStaff);
        
    }
    
    public BuildingMultiset getCompleteChain(Culture culture) {
        if (isCompleteChainComputed(culture) == false) {
            this.computeCompleteChain(culture);
        }
        
        return allChains.get(culture).getChain();
    }
    
    private Map<Resource, Float> getProduction() {
        return Map.copyOf(production);
    }
    
    @Override
    public Float getTotalStaff(Culture culture) {
        if (isTotalStaffComputed(culture) == false) {
            this.computeTotalStaff(culture);
        }
        
        return allChains.get(culture).getTotalStaff();
    }
    
    public boolean isCompleteChainComputed(Culture culture) {
        DependencyChain thisDependency = allChains.get(culture);
        
        return !thisDependency.getChain().isEmpty()
                || !thisDependency.isAvailable();
        /*
         * A complete chain must include this instance, so an empty chain cannot
         * have been computed yet.
         * 
         * Also, if the chain isn't available, it is expected say it is
         * computed.
         * 
         * @formatter:off
         * T = True
         * F = False
         * 
         *                   Returned
         * Avail? | Empty? | Computed?
         *   F        F        T
         *   F        T        T
         *   T        F        T
         *   T        T        F
         * @formatter:on
         */
    }
    
    public boolean isProducing(Resource resource) {
        return production.containsKey(resource);
    }
    
    public boolean isTotalStaffComputed(Culture culture) {
        return allChains.get(culture).getTotalStaff() != UNCOMPUTED;
    }
    
    @Override
    protected String particularToString() {
        String result = "";
        
        result += loadsPerYear;
        result += Building.COLUMNS_DELIMITER;
        
        for (Culture culture : Culture.values()) {
            result += allChains.get(culture).getChain().size();
            result += culture != Culture.NORTHERNERS
                    ? Building.COLUMNS_DELIMITER
                    : "";
        }
        
        return result;
    }
}
