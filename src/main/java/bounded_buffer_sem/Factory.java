package bounded_buffer_sem;
/**
 * This creates the buffer and the producer and consumer threads.
 *
 * Figure 7.16
 *
 * @author Gagne, Galvin, Silberschatz
 * Operating System Concepts with Java - Sixth Edition
 * Copyright John Wiley & Sons - 2003.
 */
public class Factory
{
	public static void main(String args[]) {

		Buffer server = new BoundedBuffer();

		// now create the producer and consumer threads
		Thread producerThread = new Thread(new Producer(server));
		Thread consumerThread = new Thread(new Consumer(server));

		producerThread.start();
		consumerThread.start();
	
	}
	
}
