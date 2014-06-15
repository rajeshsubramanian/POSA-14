package edu.vuum.mocca;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @class SimpleSemaphore
 * 
 * @brief This class provides a simple counting semaphore
 *        implementation using Java a ReentrantLock and a
 *        ConditionObject (which is accessed via a Condition). It must
 *        implement both "Fair" and "NonFair" semaphore semantics,
 *        just liked Java Semaphores.
 */
public class SimpleSemaphore {
    /**
     * Define a Lock to protect the critical section.
     */
    // TODO - you fill in here
	private final Lock lock;

    /**
     * Define a Condition that waits while the number of permits is 0.
     */
    // TODO - you fill in here
	private final Condition permitsAvailableCondition;

    /**
     * Define a count of the number of available permits.
     */
    // TODO - you fill in here.  Make sure that this data member will
    // ensure its values aren't cached by multiple Threads..
	private volatile int availablePermits;

    public SimpleSemaphore(int permits, boolean fair) {
        // TODO - you fill in here to initialize the SimpleSemaphore,
        // making sure to allow both fair and non-fair Semaphore
        // semantics.
		this.availablePermits = permits;
		this.lock = new ReentrantLock(fair);
		this.permitsAvailableCondition = lock.newCondition();
    }

    /**
     * Acquire one permit from the semaphore in a manner that can be
     * interrupted.
     */
    public void acquire() throws InterruptedException {
        // TODO - you fill in here.
		try {
			lock.lockInterruptibly();
			while (availablePermits == 0) {
				permitsAvailableCondition.await();
			}
			availablePermits--;
		} finally {
			lock.unlock();
		}

    }

    /**
	 * Acquire one permit from the semaphore in a manner that cannot be
	 * interrupted.
	 * 
	 * @throws InterruptedException
	 */
	public void acquireUninterruptibly() {
        // TODO - you fill in here.
		try {
			lock.lock();
			while (availablePermits == 0) {
				permitsAvailableCondition.awaitUninterruptibly();
			}
			availablePermits--;
		} finally {
			lock.unlock();
		}
    }

    /**
     * Return one permit to the semaphore.
     */
    public void release() {
        // TODO - you fill in here.
		try {
			lock.lock();
			availablePermits++;
			permitsAvailableCondition.signal();
		} finally {
			lock.unlock();
		}
    }

    /**
     * Return the number of permits available.
     */
    public int availablePermits() {
        // TODO - you fill in here by changing null to the appropriate
        // return value.
		return availablePermits;
    }
}
