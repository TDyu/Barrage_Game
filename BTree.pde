class BTree {
  private BTreeNode root;

  public BTree() {
    this.root = null;
  }

  public void push(int score, String id) {
    if (root == null) {
      root = new BTreeNode(score, id);
    } else {
      push(score, id, root);
    }
  }
  private void push(int score, String id, BTreeNode leaf) { // insert private version
    if (score < leaf.score) {
      if (leaf.left == null) {
        leaf.left = new BTreeNode(score, id);
      } else {
        push(score, id, leaf.left);
      }
    } else { // data >= leaf->data
      if (leaf.right == null) {
        leaf.right = new BTreeNode(score, id);
      } else {
        push(score, id, leaf.right);
      }
    }
  }
  //}}

  //{{ Ascending
  /*public void ascending() { // public version // Inorder Traversal
    ascending(root, 0);
  }
  private String ascending(BTreeNode ptr, int in) { // private version
    Table scoreTable = loadTable("scoreRanking.csv", "header");
    TableRow row = scoreTable.getRow(in);
    String info = null;
    if (ptr != null) {
      ascending(ptr.left);
      textSize(27);
      fill(255);
      return 
      ascending(ptr.right);
    } else {
      return;
    }
  }*/
  //}}
}