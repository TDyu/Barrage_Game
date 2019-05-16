/* Use Singly Linkes List */
class Queue<T> {
  private SSL<T> queue;
    
  public Queue() {
    super();
    this.queue = new SSL<T>();
  }
  
  public void push(T data) { // form tail
    queue.pushInTail(data);
  }
  
  public void pop() { // from head
    queue.pop();
  }
  
  public T top() {
    return queue.top();
  }
  
  public int getSize() {
    return queue.getSize();
  }
  
  public boolean isEmpty() {
    return queue.isEmpty();
  }

  /*public void show() {
    queue.show();
  }*/
}