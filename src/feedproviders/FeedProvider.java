package feedproviders;

import org.apache.log4j.Logger;

/**
 * @author paco@hernandezgomez.com
 */
public abstract class FeedProvider {
	protected static Logger log = Logger.getLogger("FeedProvider");	
	
	public abstract void loadData();
	public abstract void simulate();
}
