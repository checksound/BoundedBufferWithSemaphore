package bounded_buffer_sem;
/**
 * BoundedBuffer.java
 *
 * This program implements the bounded buffer with semaphores
 * of the java.util.concurrent.
 * Note that the use of count only serves to output whether
 * the buffer is empty of full.
 *
 *
 * @author Patrizia Scandurra
 */

import java.util.concurrent.Semaphore;

public class BoundedBuffer implements Buffer
{

	private static final int  BUFFER_SIZE = 5;

	private Semaphore mutex; // semaforo binario per l'accesso al buffer
	private Semaphore notFull; // semaforo per evitare scrittura se buffer pieno
	private Semaphore notEmpty; // semaforo per evitare lettura se buffer vuoto

	private int count; // numero di oggetti nel buffer
	private int in;   // index della prossima posizione libera per l'inserimento
	private int out;  // index della prossima posizione piena per il prelevamento
	private Object[] buffer;

	public BoundedBuffer(){

		// Il buffer e' inizialmente vuoto
		count = 0;
		in = 0;
		out = 0;

		buffer = new Object[BUFFER_SIZE];

		// inizializiamo i semafori
		mutex = new Semaphore(1);
		// inizializzato a BUFFER_SIZE: ho BUFFER_SIZE permessi disponibili
		notFull = new Semaphore(BUFFER_SIZE);
		// inizializzato a 0: ho 0 permessi disponibili
		notEmpty = new Semaphore(0);
	}

	// metodo chiamato dai produttori
	public void insert(Object item) {

		try {
			
			notFull.acquire();
			mutex.acquire();
			
			// aggiungo un oggetto al buffer
			++count;
			buffer[in] = item;
			in = (in + 1) % BUFFER_SIZE;

			if (count == BUFFER_SIZE)
				System.out.println("Producer Entered " + item + " Buffer FULL");
			else
				System.out.println("Producer Entered " + item + " Buffer Size = " +  count);
			
			// notare che si chiama
			// acquire su notFull
			// e release su notEmpty
			notEmpty.release();
			
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			mutex.release();
		}

	}

	// metodo chiamato dai consumatori 
	public Object remove() {
		
		try {
			
			notEmpty.acquire();
			mutex.acquire();
			
			// rimuovo un ogggetto dal buffer
			--count;
			Object item = buffer[out];
			out = (out + 1) % BUFFER_SIZE;

			if (count == 0)
				System.out.println("Consumer Consumed " + item + " Buffer EMPTY");
			else
				System.out.println("Consumer Consumed " + item + " Buffer Size = " + count);

			// notare che si chiama
			// acquire su notEmpty
			// e release su notFull
			notFull.release();
			return item;
			
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}finally{
			mutex.release();
		}

		
	}

}
