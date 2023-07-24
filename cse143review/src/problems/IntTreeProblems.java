package problems;

import datastructures.IntTree;
// Checkstyle will complain that this is an unused import until you use it in your code.
import datastructures.IntTree.IntTreeNode;

/**
 * See the spec on the website for tips and example behavior.
 *
 * Also note: you may want to use private helper methods to help you solve these problems.
 * YOU MUST MAKE THE PRIVATE HELPER METHODS STATIC, or else your code will not compile.
 * This happens for reasons that aren't the focus of this assignment and are mostly skimmed over in
 * 142 and 143. If you want to know more, you can ask on the discussion board or at office hours.
 *
 * REMEMBER THE FOLLOWING RESTRICTIONS:
 * - do not call any methods on the `IntTree` objects
 * - do not construct new `IntTreeNode` objects (though you may have as many `IntTreeNode` variables
 *      as you like).
 * - do not construct any external data structures such as arrays, queues, lists, etc.
 * - do not mutate the `data` field of any node; instead, change the tree only by modifying
 *      links between nodes.
 */

public class IntTreeProblems {

    /**
     * Computes and returns the sum of the values multiplied by their depths in the given tree.
     * (The root node is treated as having depth 1.)
     */
    public static int depthSum(IntTree tree) {
        return levelAddition(tree.overallRoot, 1);
    }

    // public-private pair method with depthSum, used to compute the
    // calculated 'depthSum of a IntTree.
    private static int levelAddition(IntTreeNode node, int level) {
        int sum = 0;
        if (node != null) {
            sum += node.data * level;
            sum += levelAddition(node.left, level + 1);
            sum += levelAddition(node.right, level + 1);
        }
        return sum;
    }

    /**
     * Removes all leaf nodes from the given tree.
     */
    public static void removeLeaves(IntTree tree) {
        tree.overallRoot = leafDelete(tree.overallRoot);
    }

    // public-private pair method with removeLeaves, used to remove any nodes
    // with null in both left and right from a tree.
    private static IntTreeNode leafDelete(IntTreeNode node) {
        if (node != null) {
            if (node.left == null && node.right == null) {
                return null;
            }
            node.left = leafDelete(node.left);
            node.right = leafDelete(node.right);
        }
        return node;
    }


    /**
     * Removes from the given BST all values less than `min` or greater than `max`.
     * (The resulting tree is still a BST.)
     */
    public static void trim(IntTree tree, int min, int max) {
        tree.overallRoot = trimNode(tree.overallRoot, min, max);
    }

    // public-private pair method with trim, used to remove any nodes
    // that are not in the given range designated by `min` and `max`.
    private static IntTreeNode trimNode(IntTreeNode node, int min, int max) {
        if (node != null) {
            if (node.data > max) {
                node = trimNode(node.left, min, max);
            } else if (node.data < min) {
                node = trimNode(node.right, min, max);
            } else {
                node.left = trimNode(node.left, min, max);
                node.right = trimNode(node.right, min, max);
            }
        }
        return node;
    }
}
