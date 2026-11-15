package estructuras;

public class ListaEnlazada<T> {
    private Node<T> head;
    private int size;

    public ListaEnlazada() {
        this.head = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public int getSize() {
        return size;
    }

    // Agregar al final de la lista
    public void agregar(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = newNode;
        } else {
            Node<T> actual = head;
            while (actual.getNext() != null) {
                actual = actual.getNext();
            }
            actual.setNext(newNode);
        }
        size++;
    }

    // Obtener elemento por índice
    public T obtener(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice fuera de límites: " + index);
        }
        Node<T> actual = head;
        for (int i = 0; i < index; i++) {
            actual = actual.getNext();
        }
        return actual.getData();
    }

    // Eliminar por valor
    public boolean eliminar(T data) {
        if (isEmpty()) return false;

        if (head.getData().equals(data)) {
            head = head.getNext();
            size--;
            return true;
        }

        Node<T> actual = head;
        while (actual.getNext() != null) {
            if (actual.getNext().getData().equals(data)) {
                actual.setNext(actual.getNext().getNext());
                size--;
                return true;
            }
            actual = actual.getNext();
        }
        return false;
    }

    // Limpiar lista
    public void limpiar() {
        head = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<T> actual = head;
        while (actual != null) {
            sb.append(actual.getData());
            if (actual.getNext() != null) {
                sb.append(", \n");
            }
            actual = actual.getNext();
        }
        sb.append("]");
        return sb.toString();
    }
}