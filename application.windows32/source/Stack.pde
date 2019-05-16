/* Use Singly Linkes List */
class Stack<T> {
  private SSL<T> stack;
    
  public Stack() {
    super();
    this.stack = new SSL();
  }
    
  public void push(T data) { // form top
    stack.pushInTop(data);
  }
  
  public void pop() { // from top
    stack.pop();
  }
  
  public T top() {
    return stack.top();
  }
  
  public int getSize() {
    return stack.getSize();
  }
  
  public boolean isEmpty() {
    return stack.isEmpty();
  }
   
  /*public void show() {
    stack.show();
  }*/
}