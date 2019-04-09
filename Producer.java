package hearthlandsoptimizer;
//import com.google.common.collect.*;

import java.util.Set;

/**
 * Describe a production building.
 * 
 * @author Amélia
 *
 */
@SuppressWarnings("unused")
public abstract class Producer extends AbstractBuilding {
	/*
	 * Name "Producer" instead of "AbstractProducer" because I thought the later
	 * was too long to write. Decision not final and opened to discussion.
	 */
	protected Process consumption;
	protected Process production;
	protected int loadsPerYear;
	// protected TreeMultiset<Producer> dependencies;
	/*
	 * Commented because I cannot install the Google API right now. Also, I'm
	 * not sure if TreeMultiset is really what I need.
	 */
	
	/**
	 * Amount of workers needed only for this building.
	 */
	protected int staff;
	/**
	 * Amount of worker needed for this building and all other required
	 * buildings to supply this producer.
	 */
	protected int totalStaffNeeded;
	protected CultureSet cultures = CultureSet.ALL;

	/**
	 * Contains the multiset of either all required items to produce a good, or
	 * all produced items from a single multiset of inputs, for both yearly and
	 * loadly.
	 * 
	 * Definitions are written loadly, and calculations made yearly.
	 * 
	 * @author Amélia
	 *
	 */
	private class Process {
		protected Set<Resource> loadly;
		protected Set<Resource> yearly;
		/*
		 * Set<E> is used as a placeholder until I can install the Google API
		 * for Multiset.
		 */

		public void add(int count, Resource type) {
			// TODO fill the rest.
			System.out.println("Add method accessible.");
		}

		/**
		 * Returns yearly count.
		 * 
		 * @param type
		 *            Resource to get the count of.
		 * @return Yearly count of the specified resource.
		 */
		public int getCountOf(Resource type) {
			// Return yearly count. TODO
			return 0;
		}

	}

	public Producer(int loadsPerYear, int staff){
		this.loadsPerYear = loadsPerYear;
		this.staff = staff;
	}
	
	public void consumes(int quantity, Resource type){
		consumption.add(quantity, type);
	}
	
	public void produces(int quantity, Resource type){
		production.add(quantity, type);
	}
	
	
	
	
	
}
