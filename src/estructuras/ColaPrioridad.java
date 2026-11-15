package estructuras;

public class ColaPrioridad<T extends Comparable<T>> {
    private NodeSimple<T> head;
    private int size;

    public ColaPrioridad() {
        this.head = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public int getSize() {
        return size;
    }

    // Encolar insertando de forma ordenada según la prioridad (menor fecha/valor primero)
    public void encolar(T data) {
        NodeSimple<T> newNode = new NodeSimple<>(data);
        
        if (isEmpty() || data.compareTo(head.getData()) < 0) {
            newNode.setNext(head);
            head = newNode;
        } else {
            NodeSimple<T> current = head;
            while (current.getNext() != null && current.getNext().getData().compareTo(data) <= 0) {
                current = current.getNext();
            }
            newNode.setNext(current.getNext());
            current.setNext(newNode);
        }
        size++;
    }

    // Desencolar el elemento con mayor prioridad (el primero de la lista ordenada)
    public T desencolar() {
        if (isEmpty()) {
            throw new RuntimeException("La cola de prioridad está vacía");
        }
        T data = head.getData();
        head = head.getNext();
        size--;
        return data;
    }

    // Ver el siguiente elemento a ejecutar sin sacarlo de la cola
    public T verSiguiente() {
        if (isEmpty()) {
            throw new RuntimeException("La cola de prioridad está vacía");
        }
        return head.getData();
    }

    public void limpiar() {
        head = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        NodeSimple<T> actual = head;
        while (actual != null) {
            sb.append(actual.getData());
            if (actual.getNext() != null) {
                sb.append(", \n");
            }
            actual = actual.getNext();
        }
        return sb.append("]").toString();
    }
}