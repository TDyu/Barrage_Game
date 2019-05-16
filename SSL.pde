class SSL<T> {
  private Node<T> top;
  private Node<T> tail;
  private T data; 
  private int size;

  public SSL() {
    super();
    this.top = null;
    this.tail = null;
    this.size = 0;
  }

  public void pushInTop(T data) {
    if (isEmpty()) {
      top = new Node(data);
      tail = top;
      size++;
    } else {
      Node newNode = new Node(data);
      newNode.setNext(top);
      top = newNode;
      size++;
    }
  }

  public void pushInTail(T data) {
    if (isEmpty()) {
      tail = new Node(data);
      top = tail;
      size++;
    } else {
      Node newNode = new Node(data);
      tail.setNext(newNode);
      tail = newNode;
      size++;
    }
  }

  public void pop() { // from top/head
    if (isEmpty()) {
    } else if (top.getNext() == null) {
      top = null;
      size--;
    } else {
      Node deleteNode = top;
      top = top.getNext();
      deleteNode = null;
      /*Node temp = top.getNext();
       top.setNext(null);
       top = temp;*/
      size--;
    }
  }

  public int getSize() {
    return size;
  }

  public boolean isEmpty() {
    return (size == 0);
  }

  public T top() {
    if (isEmpty()) {
      return null;
    } else {
      return top.getData();
    }
  }

  /*public void show() { // from op/head
    Node current = top;
    if (!isEmpty()) {
      while (current != tail) {
        System.out.print(current.getData() + "   ");
        current = current.getNext();
      }
      System.out.println(current.getData());
    } else {
      System.out.println();
    }
  }*/
}