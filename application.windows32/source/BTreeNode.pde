class BTreeNode {
  private BTreeNode left;
  private BTreeNode right;
  private int score;
  private String id;
  
  public BTreeNode(int score, String id) {
    this.left = this.right = null;
    this.score = score;
    this.id = id;
  }
  
  public int getData() {
    return score;
  }
  
  public String getId() {
    return id;
  }
  
}