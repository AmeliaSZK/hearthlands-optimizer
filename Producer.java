package hearthlandsoptimizer;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A building that produce {@link Resource}.
 * 
 * @author Amélia @SlayZeKyriarchy
 *
 */
public class Producer extends Building {
    
    private static final int   MAX_RECURSIVE_CALLS    = 1;
    private static final Float UNCOMPUTED_TOTAL_STAFF = 0f;
    
    private int              loadsPerYear;
    private ResourceMultiset consumption;
    private ResourceMultiset production;
    
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
            allChains.get(culture).setTotalStaff(UNCOMPUTED_TOTAL_STAFF);
            allChains.get(culture).getChain().clear();
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
     * @param culture TODO
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
                Resource          supplyNeeded    = supply.getKey();
                Float             countNeeded     = supply.getValue();
                HashSet<Producer> suppliers       = new HashSet<>();
                Producer          uniqueSupplier  = null;
                Float             suppliersNeeded = 0f;
                
                for (Building building : allBuildings) {
                    
                    if (building instanceof Producer
                            && building.isAvailableTo(culture)) {
                        
                        Producer producer = (Producer) building;
                        
                        if (producer.isProducing(supplyNeeded)) {
                            suppliers.add(producer);
                            uniqueSupplier = producer;
                            
                            Float suppliesProduced = producer.getProduction()
                                    .get(supplyNeeded);
                            
                            suppliersNeeded = countNeeded / suppliesProduced;
                        }
                    }
                }
                
                if (suppliers.size() == 1) {
                    thisChain.addAll(uniqueSupplier.getCompleteChain(culture),
                            suppliersNeeded);
                    
                } else if (suppliers.isEmpty()) {
                    thisChain.clear();
                    thisDependency.setTotalStaff(-1f);
                    thisDependency.makeUnavailable();
                    /*
                     * Not true anymore :
                     * 
                     * If no supplier was found, we put the Marketplace in the
                     * chain, but with a count of 0 so that it doesn't increase
                     * the staff needed. This is because only a few markets are
                     * needed in-game to supply the whole city, and their
                     * available supplies is variable.
                     */
                }
                
                if (suppliers.size() > 1) {
                    
                    suppliers.removeIf(e -> e.getTotalStaff(culture) == -1);
                    
                    if (suppliers.size() > 1) {
                        suppliers.removeIf(e -> e.getName().startsWith("Cotton"));
                        /*
                         * For the set of producers that excludes Hunters,
                         * Mines, and Woodburner, this condition should only
                         * come about for suppliers of Textile for the Eastern
                         * culture. Weavers can either get Cotton from a Grower,
                         * or Wool from a Sheep Sheperd.
                         * 
                         * I'm eliminating the Cotton Grower because I feel the
                         * Sheperds have a better space efficiency.
                         */
                        
                        for (Producer producer : suppliers) {
                            thisChain.addAll(producer.getCompleteChain(culture),
                                    suppliersNeeded);
                        }
                        
                    } else if (suppliers.isEmpty()) {
                        thisChain.clear();
                        thisDependency.setTotalStaff(-1f);
                        thisDependency.makeUnavailable();
                    } else {
                        for (Producer producer : suppliers) {
                            thisChain.addAll(producer.getCompleteChain(culture),
                                    suppliersNeeded);
                        }
                    }
                }
                
                if (suppliers.size() > 1) {
                    System.err.println(String.format(
                            "More than 1 available supplier of %s found for %s in culture %s!",
                            supplyNeeded, this.name, culture));
                }
                
            }
        }
        
        thisChain.add(this, 1);
        nbOfRecursiveCalls = 0;
        
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
        return allChains.get(culture).getTotalStaff() != UNCOMPUTED_TOTAL_STAFF;
    }
    
    @Override
    protected String particularToString() {
        String result = "";
        
        result += loadsPerYear;
        
        return result;
    }
}
