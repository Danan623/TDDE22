package lab4;
/**
 * A simple queue class with the methods enqueue, dequeue, isEmpty and
 * empty. Should need no modifications (even though some things are,
 * intentionally, not perfect).
 */
class Queue {
    private ListNode front, back;

    public Queue() {
	front = null;
	back = null;
    }
    
    /** 
     * Inserts an element into the queue.
     * 
     * @param element - Any data we wish to keep in the queue (Object)
     */
    public void enqueue(Object element) {
	if (isEmpty()) {
	    back = front = new ListNode(); // back = front "create new queue" if empty => queue = {null}
	} else {
	    back = back.next = new ListNode(); //queue = {object} => queue = {object, null}
	}
	back.element = element; // queue = {object1, object2}
    }

    /**
     * Remove and return the first object in the queue. Moves all the
     * other objects in the queue "forward" one step.
     *
     * @return - the first object in the queue (Object)
     */
    public Object dequeue() throws Exception {
	if (isEmpty()) {
	    throw new Exception();
	}
	Object element = front.element;
	front = front.next;
	return element;
    }

    /** 
     * IsEmpty checks whether the queue is empty or not.
     *
     * @return true if the queue is empty, false otherwise (boolean)
     */ 
    public boolean isEmpty() {
	return front == null;
    }

    /**
     * Clears the queue (removes all contents).
     */ 
    public void empty() {
	front = back = null;
    }

    /**
     * Private internal class for linked list structure.
     */
    class ListNode {
	Object element;
	ListNode next = null;
    }
}
