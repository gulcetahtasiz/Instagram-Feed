public class MaxBinaryHeap {
    private static final int DEFAULT_CAPACITY = 30; // random number
    public int currentSize;
    public Post[] array;

    // Starting the heap array with given capacity.
    public MaxBinaryHeap(int capacity) {
        currentSize = 0;
        array = new Post[capacity + 1]; // One more space to start the array from 1. index not from the 0th.
    }

    // Find the maximum item
    public Post findMax() {
        if (isEmpty()) {
            System.out.println("Heap is empty.");
            return null;
        }
        return array[1];
    }

    // Delete the maximum item
    public Post deleteMax() {
        if (isEmpty()) {
            return null;
        }

        Post maxItem = findMax();
        array[1] = array[currentSize--];
        percolateDown(1);
        return maxItem;
    }

    // Check if the heap is empty
    public boolean isEmpty() {
        return currentSize == 0;
    }

    // Percolate down to maintain heap order
    private void percolateDown(int hole) {
        int child;
        Post tmp = array[hole];

        for (; hole * 2 <= currentSize; hole = child) {
            child = hole * 2;
            if (child != currentSize && array[child + 1].compareTo(array[child]) > 0) {
                child++;
            }
            if (array[child].compareTo(tmp) > 0) {
                array[hole] = array[child];
            } else {
                break;
            }
        }
        array[hole] = tmp;
    }

    // Build heap to restore heap order
    public void buildHeap() {
        for (int i = currentSize / 2; i > 0; i--) {
            percolateDown(i);
        }
    }

}
