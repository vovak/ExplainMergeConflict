package utils;

import com.intellij.ui.treeStructure.Tree;

public class Utils {

  /**
   * Expands all the nodes of the tree.
   *
   * @param tree          to be expanded.
   * @param startingIndex the starting index for expansion.
   * @param rowCount      of the tree.
   */
  public static void expandAllNodes(Tree tree, int startingIndex, int rowCount) {
    for (int i = startingIndex; i < rowCount; ++i) {
      tree.expandRow(i);
    }
    if (tree.getRowCount() != rowCount) {
      expandAllNodes(tree, rowCount, tree.getRowCount());
    }
  }

  /**
   * Method used for a presentable displaying of class change.
   * @param nameBefore class name before.
   * @param nameAfter class name after.
   * @return the index where the string are different.
   */
  public static int indexOfDifference(String nameBefore, String nameAfter) {
    int minLen = Math.min(nameBefore.length(), nameAfter.length());
    int index = -1;
    for (int i = 0; i != minLen; i++) {
      char chA = nameBefore.charAt(i);
      char chB = nameAfter.charAt(i);
      if (chA != chB) {
        index = i;
        return index;
      }
    }
    return index;
  }

}
