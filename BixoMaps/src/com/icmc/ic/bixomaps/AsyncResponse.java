package com.icmc.ic.bixomaps;

/**
 * This interface is implemented by the class that needs a response from 
 * processes running in concurrency. Whenever a thread finishes and needs to 
 * send the results of its calculations, it will send those through the
 *  processFinish method.
 *
 * @author Thais Santos
 * @version 1.0
 * @since January 27, 2015
 */

public interface AsyncResponse {

	Void processFinish(String[] result);
}
