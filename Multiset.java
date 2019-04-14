package hearthlandsoptimizer;

import java.util.Map;

/**
 * Implementation of a Multiset interface for {@link Map}.
 * 
 * @author Amélia @SlayZeKyriarchy
 * @param <K> the type of elements maintained by this multiset.
 *
 */
public interface Multiset<K> extends Map<K, Float> {
        
    /**
     * Adds the specified count of {@code element} to the multiset. 
     * 
     * @param element The key to add.
     * @param count The amount to add.
     */
    public default void add(K element, Float count) {
        Float previousCount = this.get(element);
        
        if(previousCount == null) {
            this.put(element, count);
        } else {
            this.put(element, count + previousCount);
        }
    }
    
    /**
     * Adds the specified count of {@code element} to the multiset. 
     * 
     * @param element The key to add.
     * @param count The amount to add.
     */
    public default void add(K element, Integer count) {
        Float addedCount = count.floatValue();
        Float previousCount = this.get(element);
        
        if(previousCount == null) {
            this.put(element, addedCount);
        } else {
            this.put(element, addedCount + previousCount);
        }
    }
    
    /**
     * Adds all members of the specified multiset to this multiset, with each
     * sourced value multiplied by the specified factor before their inclusion.
     * 
     * @param ms The multiset to add.
     * @param factor The factor by which to multiply each value from {@code ms}.
     */
    public default void addAll(Multiset<K> ms, Float factor) {
        for(Map.Entry<K, Float> entry : ms.entrySet()) {
            add(entry.getKey(), entry.getValue() * factor);
        }
    }
    
}
