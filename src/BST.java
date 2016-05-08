/*Kalvin Suting
 *4/15/2016
 *CSE 373 
 *Assignment 3 
 *This class builds a binary search tree using keywords to sort the nodes, and records containing 
 *articles with those keywords associated with them. By definition the BST is sorted from left
 *to right based on the keywords. This implementation does not balance the tree. 
 */
class BST {

	Node root;

	//constructs the BST. 
	public BST() {
		this.root = null;
	}

	//Inserts keyword and record into the tree. If the tree does not 
	//contain the record already, adds a new node into the tree. Else, adds 
	//record to the pre-existing node. Case sensitive. 
	public void insert(String keyword, FileData fd) {
		Record recordToAdd = new Record(fd.id, fd.title, fd.author, null);
		root = insert(keyword, recordToAdd, root);
	}

	//helper method to insert the given keyword and record to the tree.
	private Node insert(String keyword, Record recordToAdd, Node root) {
		if (root == null) { //create new node.
			return new Node(keyword, recordToAdd);
		}
		if (root.keyword.compareTo(keyword) > 0) { //inserted key < key of the current node
			root.left = insert(keyword, recordToAdd, root.left);
		} else if (root.keyword.compareTo(keyword) < 0) {  //key > key of current node
			root.right = insert(keyword, recordToAdd, root.right);
		} else { //node exists already in the tree. 
			root.update(recordToAdd);
			return root;
		}
		return root;
	}

	//returns whether or not the tree contains a node with the given node.
	public boolean contains(String keyword) {
		return contains(keyword, root);
		}

	//helper method to determine if the tree contains node.
	private boolean contains(String keyword, Node root) {
		if (root == null) { // doesnt exist in tree
			return false;
		}
		if (root.keyword.equalsIgnoreCase(keyword)) {//exists in tree
			return true;
		}
		return contains(keyword, root.left) || contains(keyword, root.right); 
	}

	//gets the records of a given keyword from the node of the tree. If the node does
	//not exist returns null.
	public Record get_records(String keyword) {
		return get_records(keyword, root);	
	}
	
	//helper method for getting the records. 
	private Record get_records(String keyword, Node root) {
		if (root.keyword.equalsIgnoreCase(keyword)) { // keyword = node keyword
			return root.record;
		}
		if (root.keyword.compareTo(keyword) > 0) { //keyword < node keyword
			return get_records(keyword, root.left);
		}
		if (root.keyword.compareTo(keyword) < 0) { //keyword > node keyword
			return get_records(keyword, root.right);
		}
		return null;
	}

	//finds the node and deletes it from the tree. Adjusts the tree so that 
	//subsequent nodes are not lost. Case insensitive. 
	public void delete(String keyword) {
		if (contains(keyword)) {
			root = delete(keyword, root);
		} // does nothing if the node does not exist in the tree.
	}

	//helper method for deletion from the tree. 
	private Node delete(String keyword, Node root) {
		if (root.keyword.equalsIgnoreCase(keyword)) {
			if (root.isLeaf()) {
				return null;
			}
			Node temp = root.left;
			root = root.right;
			Node temp2 = findLeftLeaf(root);
			temp2.left = temp;
		} else if (root.keyword.compareTo(keyword) > 0) {
			root.left = delete(keyword, root.left);

		} else {
			root.right = delete(keyword, root.right);
		}
		return root;
	}

	//finds the left most leaf for deletion. As a side note, rightmost node
	//would work as well, I just didn't use that algorithm.
	private Node findLeftLeaf(Node root) {
		if (root.isLeaf() || root.left == null) {
			return root;
		}
		return findLeftLeaf(root.left);
	}
	
	//prints the nodes of the tree, as well as the record for each tree.
	public void print() {
		print(root);
	}

	//helper method to print tree.
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
	
	//the node class for the BST. Contains a keyword which is used for the sorting of the tree.
	//Each node contains a record which is an implementation of a linked list. 
	private class Node {
		String keyword;
		Record record;
		int size; // the size of the record list.
		Node left; 
		Node right;
		
		//constructor for a single node.
		private Node(String k) {
			this.keyword = k;
			this.size = 1;
		}

		//alternate constructor that constructs the record as well as the node. 
		private Node(String k, Record r) {
			this.keyword = k;
			this.record = r;
			this.size = 1;
		}

		//update the record of a node. 
		private void update(Record r) {
			if(r != null){
			r.next = record;
			record = r;
			this.size++;
			}
		}

		//returns true if a leaf node, false if not. 
		private boolean isLeaf() {
			return this.left == null && this.right == null;

		}
	}
}
