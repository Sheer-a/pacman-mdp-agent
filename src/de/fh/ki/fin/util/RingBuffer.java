package de.fh.ki.fin.util;

import java.util.ArrayList;

public class RingBuffer<T> {
	
    private short head;	// index
    private final int size;
    private final ArrayList<T> buffer;

    public RingBuffer(int size) {
        buffer = new ArrayList<>();
        head = 0;
        this.size = size;
        for(int i=0; i<size; ++i)
            buffer.add(null);
    }

    public int countOccurrences(T obj) {
    	int result = 0;
    	for(T element : buffer)
    		if(element != null && obj instanceof T && element.equals(obj))
    			result++;
    	return result;
    }
    
    public void addToBuffer(T obj) {
        buffer.set(head, obj);
        moveHead();
    }

    private void moveHead() {
    	head = (short) ((head+1 == size) ? 0 : (head+1));
    }
}