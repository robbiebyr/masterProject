package suncertify.server;

import suncertify.db.DBCache;

/**
 * ServerClosingHook class is used to monitor when a virtual-machine shutdown
 * has been initiated.
 * 
 * @author Robbie Byrne
 * 
 */
public class ServerClosingHook {

	/**
	 * The hook is attached to monitor the current VM. When a shutdown is
	 * initiated, the run method of the hook is called and in this case it
	 * writes (dumps) the DBCache to the db file.
	 */
	public void attachHook() {

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				DBCache.dumpCacheToFile();
			}
		});
	}
}