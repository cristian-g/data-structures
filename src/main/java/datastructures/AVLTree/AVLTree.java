package datastructures.AVLTree;

import datastructures.ElementWithIntegerKey;
import utils.IntegerUtilities;
import java.util.LinkedList;

public class AVLTree {

    public static String DATA_STRUCTURE_NAME = "AVL Tree";

    private AVLNode root;

    public AVLNode getRoot() {
        return root;
    }

    public void setRoot(AVLNode root) {
        this.root = root;
    }

    public void insert(ElementWithIntegerKey element) {
        this.root = this.insertImmersion(this.root, element);
    }

    public AVLNode insertImmersion(AVLNode node, ElementWithIntegerKey element) {

        int key = element.getKey();

        // Check if sub tree is empty
        if (node == null) {
            // Create the node
            AVLNode avlNode = new AVLNode(element);
            return avlNode;
        }

        // Sub tree is NOT empty so we have to perform the recursive descent
        if (key < node.getKey()) {
            node.setLeft(this.insertImmersion(node.getLeft(), element));
        } else if (key > node.getKey()) {
            node.setRight(this.insertImmersion(node.getRight(), element));
        } else {
            // Key already exists, so we cannot add a new element with the same key
            return node;
        }

        // After inserting the element, we must increase the height of the current node
        // so we can later compute the height of the whole tree.
        // Let's compute height below using child heights
        int heightBelow = IntegerUtilities.max(
                height(node.getLeft()),
                height(node.getRight())
        );
        // Increase the height of the current node
        node.setHeight(heightBelow + 1);

        // Compute the balance factor of this node
        int balance = this.getBalance(node);

        // Check if this node is unbalanced
        if (balance > 1 || balance < -1) {

            // This node is unbalanced
            // Let's check the case

            if (balance > 1) {
                // Case Left Left / case Left Right
                if (key < node.getLeft().getKey()) {
                    // Case Left Left
                    return this.rightRotate(node);
                } else if (key > node.getLeft().getKey()) {
                    // Case Left Right
                    node.setLeft(leftRotate(node.getLeft()));
                    return this.rightRotate(node);
                }
            } else {// balance < -1
                // Case Right Right / case Right Left
                if (key > node.getRight().getKey()) {
                    // Case Right Right
                    return this.leftRotate(node);
                } else if (key < node.getRight().getKey()) {
                    // Case Right Left
                    node.setRight(rightRotate(node.getRight()));
                    return this.leftRotate(node);
                }
            }
        }

        // No rotation is needed
        return node;
    }

    // Returns the node with the minimum key
    public AVLNode minValueNode(AVLNode node) {
        AVLNode current = node;

        // Loop to find the leaf at the left
        while (current.getLeft() != null) {
            current = current.getLeft();
        }

        return current;
    }

    public ElementWithIntegerKey findNodeWithKey(int key) {
        AVLNode current = this.root;

        // Loop down
        while (current != null && current.getKey() != key) {
            if (key < current.getKey()) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }

        if (current != null) {
            return current.getElement();
        }

        return null;
    }

    public void deleteNode(int key) {
        this.root = this.deleteNodeImmersion(this.root, key);
    }

    public AVLNode deleteNodeImmersion(AVLNode treeRoot, int key) {

        // Check if sub tree is empty
        if (treeRoot == null) {
            // Sub tree is empty
            // End operation
            return treeRoot;
        }

        // Sub tree is NOT empty so we have to perform the recursive descent
        if (key < treeRoot.getKey()) {
            // The key of the current node is less than the key we are looking for
            // Recursive descent through the left
            treeRoot.setLeft(deleteNodeImmersion(treeRoot.getLeft(), key));
        } else if (key > treeRoot.getKey()) {
            // The key of the current node is greater than the key we are looking for
            // Recursive descent through the right
            treeRoot.setRight(deleteNodeImmersion(treeRoot.getRight(), key));
        } else {
            // Key already exists, so we cannot add a new element with the same key
            treeRoot = this.handleDeletion(treeRoot);
        }

        // After applying the removal, let's see if rotations need to be applied

        // Case of tree with just one node: it is not necessary to apply rotations
        if (treeRoot == null) {
            return treeRoot;
        }

        // After inserting the element, we must increase the height of the current node
        // so we can later compute the height of the whole tree.
        // Let's compute height below using child heights
        int heightBelow = IntegerUtilities.max(height(treeRoot.getLeft()), height(treeRoot.getRight()));
        // Increase the height of the current node
        treeRoot.setHeight(heightBelow + 1);

        // Compute the balance factor of this node
        int balance = getBalance(treeRoot);

        // Check if this node is unbalanced
        if (balance > 1 || balance < -1) {

            // This node is unbalanced
            // Let's check the case

            if (balance > 1) {
                // Case Left Left / case Left Right
                if (getBalance(treeRoot.getLeft()) >= 0) {
                    // Case Left Left: Right Rotation
                    return rightRotate(treeRoot);// Right Rotation
                } else if (getBalance(treeRoot.getLeft()) < 0) {
                    // Case Left Right: Left Rotation and Right Rotation
                    treeRoot.setLeft(leftRotate(treeRoot.getLeft()));// Left Rotation
                    return rightRotate(treeRoot);// Right Rotation
                }
            } else {// balance < -1
                // Case Right Right / case Right Left
                if (getBalance(treeRoot.getRight()) <= 0) {
                    // Case Right Right: Left Rotation
                    return leftRotate(treeRoot);// Left Rotation
                } else if (getBalance(treeRoot.getRight()) > 0) {
                    // Case Right Left: Right Rotation and Left Rotation
                    treeRoot.setRight(rightRotate(treeRoot.getRight()));// Right Rotation
                    return leftRotate(treeRoot);// Left Rotation
                }
            }
        }

        return treeRoot;
    }

    private AVLNode handleDeletion(AVLNode treeRoot) {

        // Two possible cases:
        // - Node with only one child or with no childs at all
        // - Node with two children

        // Let's check the case
        if ((treeRoot.getLeft() == null) || (treeRoot.getRight() == null)) {

            // Node with only one child or with no childs at all

            // Store the node which can be non-null
            AVLNode node = null;
            if (node == treeRoot.getLeft()) {
                node = treeRoot.getRight();
            } else {
                node = treeRoot.getLeft();
            }

            // Two possible cases:
            // - No child case
            // - One child case

            // No child case
            if (node == null) {
                treeRoot = null;
            }
            else { // One child case
                treeRoot = node; // Copy the contents of the non-empty child
            }
        } else {

            // Case of node with two children

            // Store the inorder successor (smallest node in the right subtree)
            AVLNode temp = minValueNode(treeRoot.getRight());

            // Copy the inorder successor's data to this node
            treeRoot.setElement(temp.getElement());

            // Delete the inorder successor
            treeRoot.setRight(deleteNodeImmersion(treeRoot.getRight(), temp.getKey()));
        }

        return treeRoot;
    }

    // Right rotation
    public AVLNode rightRotate(AVLNode node) {

        // Store childs
        AVLNode originalLeft = node.getLeft();
        AVLNode originalLeftRight = originalLeft.getRight();

        // Old root will be stored as right child of new root
        originalLeft.setRight(node);

        // Original right child of left child will be stored as left child of old root
        node.setLeft(originalLeftRight);

        // Update heights
        int currentHeightChilds = IntegerUtilities.max(height(node.getLeft()), height(node.getRight()));
        node.setHeight(currentHeightChilds + 1);
        int currentHeightChildsLeft = IntegerUtilities.max(height(originalLeft.getLeft()), height(originalLeft.getRight()));
        originalLeft.setHeight(currentHeightChildsLeft + 1);

        // New root is the original left
        return originalLeft;
    }

    // Left rotation
    public AVLNode leftRotate(AVLNode node) {

        // Store childs
        AVLNode originalRight = node.getRight();
        AVLNode originalRightLeft = originalRight.getLeft();

        // Old root will be stored as left child of new root
        originalRight.setLeft(node);

        // Original left child of right child will be stored as right child of old root
        node.setRight(originalRightLeft);

        //  Update heights
        int currentHeightChilds = IntegerUtilities.max(height(node.getLeft()), height(node.getRight()));
        node.setHeight(currentHeightChilds + 1);
        int currentHeightChildsRight = IntegerUtilities.max(height(originalRight.getLeft()), height(originalRight.getRight()));
        originalRight.setHeight(currentHeightChildsRight + 1);

        // New root is the original left
        return originalRight;
    }

    public ElementWithIntegerKey[] preOrder(AVLNode node) {
        LinkedList<ElementWithIntegerKey> list = new LinkedList<>();
        return preOrderImmersion(node, list).toArray(new ElementWithIntegerKey[list.size()]);
    }

    public LinkedList<ElementWithIntegerKey> preOrderImmersion(AVLNode node, LinkedList<ElementWithIntegerKey> list) {
        if (node != null) {
            list.add(node.getElement());
            preOrderImmersion(node.getLeft(), list);
            preOrderImmersion(node.getRight(), list);
        }
        return list;
    }

    public ElementWithIntegerKey[] inOrder(AVLNode node) {
        LinkedList<ElementWithIntegerKey> list = new LinkedList<>();
        return inOrderImmersion(node, list).toArray(new ElementWithIntegerKey[list.size()]);
    }

    public LinkedList<ElementWithIntegerKey> inOrderImmersion(AVLNode node, LinkedList<ElementWithIntegerKey> list) {
        if (node != null) {
            inOrderImmersion(node.getLeft(), list);
            list.add(node.getElement());
            inOrderImmersion(node.getRight(), list);
        }
        return list;
    }

    // Get the height of the tree
    int height(AVLNode N) {
        if (N == null)
            return 0;

        return N.getHeight();
    }

    // Get the balance factor of a node
    int getBalance(AVLNode node) {
        if (node == null)
            return 0;

        return this.height(node.getLeft()) - this.height(node.getRight());
    }
}
