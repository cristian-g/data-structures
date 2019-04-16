package datastructures.AVLTree;

import datastructures.ElementWithIntegerKey;
import utils.IntegerUtilities;

import java.util.LinkedList;

// Java program for insertion in AVL Tree
public class AVLTree {

    private AVLNode root;

    public AVLNode getRoot() {
        return root;
    }

    public void setRoot(AVLNode root) {
        this.root = root;
    }

    // A utility function to get the height of the tree
    int height(AVLNode N) {
        if (N == null)
            return 0;

        return N.getHeight();
    }

    // A utility function to right rotate subtree rooted with y
    // See the diagram given above.
    AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.getLeft();
        AVLNode T2 = x.getRight();

        // Perform rotation
        x.setRight(y);
        y.setLeft(T2);

        // Update heights
        y.setHeight(IntegerUtilities.max(height(y.getLeft()), height(y.getRight())) + 1);
        x.setHeight(IntegerUtilities.max(height(x.getLeft()), height(x.getRight())) + 1);

        // Return new root
        return x;
    }

    // A utility function to left rotate subtree rooted with x
    // See the diagram given above.
    AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.getRight();
        AVLNode T2 = y.getLeft();

        // Perform rotation
        y.setLeft(x);
        x.setRight(T2);

        //  Update heights
        x.setHeight(IntegerUtilities.max(height(x.getLeft()), height(x.getRight())) + 1);
        y.setHeight(IntegerUtilities.max(height(y.getLeft()), height(y.getRight())) + 1);

        // Return new root
        return y;
    }

    // Get Balance factor of node N
    int getBalance(AVLNode N) {
        if (N == null)
            return 0;

        return height(N.getLeft()) - height(N.getRight());
    }

    public AVLNode insert(AVLNode node, ElementWithIntegerKey element) {

        int key = element.getKey();

        // 1.  Perform the normal BST insertion
        if (node == null)
            return (new AVLNode(element));

        if (key < node.getKey())
            node.setLeft(insert(node.getLeft(), element));
        else if (key > node.getKey())
            node.setRight(insert(node.getRight(), element));
        else // Duplicate keys not allowed
            return node;

        // 2. Update height of this ancestor node
        node.setHeight(1 + IntegerUtilities.max(height(node.getLeft()),
                height(node.getRight())));

        // 3. Get the balance factor of this ancestor node to check whether this node became unbalanced
        int balance = getBalance(node);

        // If this node becomes unbalanced, then there are 4 cases
        // Left Left Case
        if (balance > 1 && key < node.getLeft().getKey())
            return rightRotate(node);

        // Left Right Case
        if (balance > 1 && key > node.getLeft().getKey()) {
            node.setLeft(leftRotate(node.getLeft()));
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && key < node.getRight().getKey()) {
            node.setRight(rightRotate(node.getRight()));
            return leftRotate(node);
        }

        // Right Right Case
        if (balance < -1 && key > node.getRight().getKey())
            return leftRotate(node);

        /* return the (unchanged) node pointer */
        return node;
    }

    // A utility function to print preorder traversal
    // of the tree.
    // The function also prints height of every node
    public void preOrder(AVLNode node) {
        if (node != null) {
            System.out.print(node.getKey() + " ");
            preOrder(node.getLeft());
            preOrder(node.getRight());
        }
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

    /**
     * Given a non-empty binary search tree, return the
     node with minimum key value found in that tree.
     Note that the entire tree does not need to be
     searched.
     * @param node
     * @return
     */
    public AVLNode minValueNode(AVLNode node)
    {
        AVLNode current = node;

        /* loop down to find the leftmost leaf */
        while (current.getLeft() != null)
            current = current.getLeft();

        return current;
    }

    public AVLNode deleteNode(AVLNode root, int key)
    {
        // Step 1: perform standard BST delete
        if (root == null)
            return root;

        // If the key to be deleted is smaller than
        // the root's key, then it lies in left subtree
        if (key < root.getKey())
            root.setLeft( deleteNode(root.getLeft(), key));

            // If the key to be deleted is greater than the
            // root's key, then it lies in right subtree
        else if (key > root.getKey())
            root.setRight(deleteNode(root.getRight(), key));

            // if key is same as root's key, then this is the node to be deleted
        else
        {

            // node with only one child or no child
            if ((root.getLeft() == null) || (root.getRight() == null))
            {
                AVLNode temp = null;
                if (temp == root.getLeft())
                    temp = root.getRight();
                else
                    temp = root.getLeft();

                // No child case
                if (temp == null)
                {
                    temp = root;
                    root = null;
                }
                else // One child case
                    root = temp; // Copy the contents of
                // the non-empty child
            }
            else
            {

                // node with two children: Get the inorder
                // successor (smallest in the right subtree)
                AVLNode temp = minValueNode(root.getRight());

                // Copy the inorder successor's data to this node
                root.setElement(temp.getElement());

                // Delete the inorder successor
                root.setRight(deleteNode(root.getRight(), temp.getKey()));
            }
        }

        // If the tree had only one node then return
        if (root == null)
            return root;

        // Step 2: update height of the current node
        root.setHeight(IntegerUtilities.max(height(root.getLeft()), height(root.getRight())) + 1);

        // Step 3: GET THE BALANCE FACTOR OF THIS NODE (to check whether this node became unbalanced)
        int balance = getBalance(root);

        // If this node becomes unbalanced, then there are 4 cases
        // Left Left Case
        if (balance > 1 && getBalance(root.getLeft()) >= 0)
            return rightRotate(root);

        // Left Right Case
        if (balance > 1 && getBalance(root.getLeft()) < 0)
        {
            root.setLeft(leftRotate(root.getLeft()));
            return rightRotate(root);
        }


        // Right Right Case
        // Right Right Case
        // Right Right Case
        if (balance < -1 && getBalance(root.getRight()) <= 0)
            return leftRotate(root);

        // Right Left Case
        if (balance < -1 && getBalance(root.getRight()) > 0)
        {
            root.setRight(rightRotate(root.getRight()));
            return leftRotate(root);
        }

        return root;
    }
}
