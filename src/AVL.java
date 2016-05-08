/*Kalvin Suting
 *4/15/2016
 *CSE 373 
 *Assignment 3 Extra Credit. 
 *This class builds an AVL tree using keywords to sort the nodes, and records containing 
 *articles with those keywords associated with them. By definition the AVL is sorted from left
 *to right based on the keywords and also balanced by ensuring that no subtree differs in height
 *by a factor greater than 1. 
 */
class AVL {

	Node root;

	// constructs the AVL.
	public AVL() {
		this.root = null;
	}

	// Inserts keyword and record into the tree. If the tree does not
	// contain the record already, adds a new node into the tree. Else, adds
	// record to the pre-existing node. Also maintains balance of the tree
	// according
	// to AVL protocol (no subtree greater in height than another by more than
	// 1).
	public void insert(String keyword, FileData fd) {
		Record recordToAdd = new Record(fd.id, fd.title, fd.author, null);
		root = insert(keyword, recordToAdd, root);
	}

	// helper method to insert the given keyword and record to the tree.
	// Maintains
	// balance of the tree during insertion process.
	private Node insert(String keyword, Record recordToAdd, Node root) {
		if (root == null) {
			return new Node(keyword, recordToAdd);
		}
		int balance = 0;
		if (root.keyword.compareTo(keyword) > 0) {
			root.left = insert(keyword, recordToAdd, root.left);
			balance = getBalanceFactor(root);

		} else if (root.keyword.compareTo(keyword) < 0) {
			root.right = insert(keyword, recordToAdd, root.right);
			balance = getBalanceFactor(root);

		} else {
			root.update(recordToAdd);
		}
		return balance(root, balance);
	}

	// returns the height of a node.
	private int height(Node root) {
		if (root == null) {
			return -1;
		}
		return root.height;
	}

	// left of left rotation to balance tree.
	private Node leftLeft(Node root) {
		Node temp = root.left;
		root.left = temp.right;
		temp.right = root;
		root.height = Math.max(height(root.left), height(root.right)) + 1;
		temp.height = Math.max(height(temp.left), root.height) + 1;
		return temp;
	}

	// right of right rotation to balance tree.
	private Node rightRight(Node root) {
		Node temp = root.right;
		root.right = temp.left;
		temp.left = root;
		root.height = Math.max(height(root.left), height(root.right)) + 1;
		temp.height = Math.max(height(temp.right), root.height) + 1;
		return temp;
	}

	// left of right (double rotation) to balance tree.
	private Node leftRight(Node root) {
		root.left = rightRight(root.left);
		return leftLeft(root);
	}

	// right of left (double rotation) to balance tree.
	private Node rightLeft(Node root) {
		root.right = leftLeft(root.right);
		return rightRight(root);
	}

	// returns the balance factor of a subtree. This is used during insertion to
	// determine
	// what rotations need to occur in order to maintain balance.
	private int getBalanceFactor(Node root) {
		return height(root.left) - height(root.right);
	}

	// helper function to balance tree during insertion.
	private Node balance(Node root, int balanceFactor) {
		if (root == null) {
			return root;
		}
		if (balanceFactor > 1) { // left subtree unbalanced.
			if (height(root.left.left) >= height(root.left.right)) { // LL
																		// rotation
																		// needed.
				root = leftLeft(root);

			} else { // LR rotation.
				root = leftRight(root);
			}
		} else if (balanceFactor < -1) { // right subtree unbalanced.
			if (height(root.right.right) >= height(root.right.left)) { // RR
																		// rotation
																		// needed.
				root = rightRight(root);
			} else { // RL rotation needed.
				root = rightLeft(root);
			}
		}
		root.height = Math.max(height(root.left), height(root.right)) + 1; // updates
																			// height.
		return root;
	}

	// returns whether or not the tree contains a node with the given keyword.
	public boolean contains(String keyword) {
		return contains(keyword, root);
	}

	// helper function for finding whether a node containing the given keyword
	// exists in the tree.
	private boolean contains(String keyword, Node root) {
		if (root == null) {
			return false;
		}
		if (root.keyword.equalsIgnoreCase(keyword)) {
			return true;
		}
		return contains(keyword, root.left) || contains(keyword, root.right);
	}

	// gets the records of a given keyword from the node of the tree. If the
	// node does
	// not exist, returns null.
	public Record get_records(String keyword) {
		return get_records(keyword, root);
	}

	// helper method for getting the records.
	private Record get_records(String keyword, Node root) {
		if (root.keyword.equalsIgnoreCase(keyword)) {// keyword = node keyword
			return root.record;
		}
		if (root.keyword.compareTo(keyword) > 0) {
			return get_records(keyword, root.left); // keyword < node keyword
		}
		if (root.keyword.compareTo(keyword) < 0) {
			return get_records(keyword, root.right); // keyword > node keyword
		}
		return null;
	}

	// finds the node and deletes it from the tree. Adjusts the tree so that
	// subsequent nodes are not lost.
	public void delete(String keyword) {
		if (contains(keyword)) {
			root = delete(keyword, root);
		}
	}

	// helper method for deletion from the tree.
	private Node delete(String keyword, Node root) {
		if (root.keyword.equalsIgnoreCase(keyword)) { // found the correct node
														// to be deleted.
			if (root.isLeaf()) {
				return null;
			}
			Node temp = root.left;
			root = root.right;
			Node temp2 = findLeftLeaf(root); // multi
			temp2.left = temp;
			temp = root.left;
			int balance = getBalanceFactor(root);
			temp2.right = balance(root, balance);
			temp.left = null;
			root = temp2;
		} else if (root.keyword.compareTo(keyword) > 0) {// searches left
															// subtree.
			root.left = delete(keyword, root.left);

		} else { // searches right subtree.
			root.right = delete(keyword, root.right);
		}
		int balance = getBalanceFactor(root); // checks balance of tree.
		return balance(root, balance); // re-balances if necessary.
	}

	// finds the left most leaf for deletion and rearranging of the tree.
	private Node findLeftLeaf(Node root) {
		if (root.isLeaf() || root.left == null) {
			return root;
		}
		return findLeftLeaf(root.left);
	}

	// prints the nodes of the tree, as well as the record for each tree.
	public void print() {
		print(root);
	}

	// helper method to print tree.
	private void print(Node t) {
		if (t != null) {
			print(t.left);
			System.out.println(t.keyword);
			Record r = t.record;
			while (r != null) {
				System.out.println("\t" + r.title);
				r = r.next;
			}
			print(t.right);
		}
	}

	// the node class for the BST. Contains a keyword which is used for the
	// sorting of the tree.
	// Each node contains a record which is an implementation of a linked list.
	private class Node {
		String keyword;
		Record record;
		int height; // height of the tree
		int size; // size of the record list.
		Node left;
		Node right;

		// constructor for a single node.
		private Node(String k) {
			this.keyword = k;
			this.height = 0;
			this.size = 1;

		}

		// alternate constructor that constructs the record as well as the node.
		private Node(String k, Record r) {
			this.keyword = k;
			this.record = r;
			this.height = 0;
			this.size = 1;
		}

		// update the record of a node.
		private void update(Record r) {
			r.next = record;
			record = r;
			this.size++;
		}

		// returns true if a leaf node, false if not.
		private boolean isLeaf() {
			return this.left == null && this.right == null;
		}
	}
}
