package org.jetbrains.research.refactorinsight.utils;

import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameterList;
import gr.uom.java.xmi.UMLOperation;
import org.jetbrains.research.refactorinsight.data.Group;
import org.jetbrains.research.refactorinsight.data.RefactoringInfo;

public class StringUtils {

  public static final String ESC = "#";
  public static final String ESC_REGEX = "(?<!" + ESC + ")";

  public static final int MAP = 0;
  public static final int MAP_ENTRY = 1;
  public static final int ENTRY = 2;
  public static final int INFO = 3;
  public static final int LIST = 4;
  public static final int FRAG = 5;
  public static final int RANGE = 6;
  public static final String[] delimiters = {"_", "=", "`", "-", "!", ",", ";"};

  public static String delimiter(int option, boolean escaped) {
    return (escaped ? ESC_REGEX : "") + delimiters[option];
  }

  public static String delimiter(int option) {
    return delimiter(option, false);
  }


  /**
   * Method used for a presentable displaying of class change.
   *
   * @param nameBefore class name before.
   * @param nameAfter  class name after.
   * @return the index where the string are different.
   */
  public static int indexOfDifference(String nameBefore, String nameAfter) {
    int minLen = Math.min(nameBefore.length(), nameAfter.length());
    int last = 0;
    for (int i = 0; i != minLen; i++) {
      char chA = nameBefore.charAt(i);
      char chB = nameAfter.charAt(i);
      if (nameBefore.charAt(i) == '.' && nameAfter.charAt(i) == '.') {
        last = i + 1;
      }
      if (chA != chB) {
        return last;
      }
    }
    return last;
  }

  /**
   * Calculates the signature of a PsiMethod such that it matches the once calculated
   * for a RefactoringMiner UMLOperation.
   *
   * @param method to calcukate signature for.
   * @return the signature.
   */
  public static String calculateSignature(PsiMethod method) {
    String signature = method.getName();
    signature = method.getContainingClass().getQualifiedName() + "." + signature + "(";
    PsiParameterList parameterList = method.getParameterList();
    int parametersCount = parameterList.getParametersCount();

    for (int i = 0; i < parametersCount; i++) {
      if (i != parametersCount - 1) {
        signature += parameterList.getParameter(i).getType().getPresentableText() + ", ";
      } else {
        signature += parameterList.getParameter(i).getType().getPresentableText();
      }
    }
    signature += ")";
    return signature;
  }

  /**
   * Builder for a method's signature.
   *
   * @param operation retrieved from RefactoringMiner
   * @return a String signature of the operation.
   */
  public static String calculateSignature(UMLOperation operation) {
    StringBuilder builder = new StringBuilder();
    builder.append(operation.getClassName())
        .append(".")
        .append(calculateSignatureWithoutClassName(operation));
    return builder.toString();
  }

  /**
   * Builder for a method's signature.
   *
   * @param operation retrieved from RefactoringMiner
   * @return a String signature of the operation.
   */
  public static String calculateSignatureWithoutClassName(UMLOperation operation) {
    StringBuilder builder = new StringBuilder();
    builder.append(operation.getName())
            .append("(");

    operation.getParameterTypeList().forEach(x -> builder.append(x).append(", "));

    if (operation.getParameterTypeList().size() > 0) {
      builder.deleteCharAt(builder.length() - 1);
      builder.deleteCharAt(builder.length() - 1);
    }

    builder.append(")");
    return builder.toString();
  }

  /**
   * Computes the signature of a PsiField in order to match a RefactoringMiner attribute.
   *
   * @param field to compute signature for.
   * @return signature of a field.
   */
  public static String getFieldSignature(PsiField field) {
    return field.getContainingClass().getQualifiedName()
        + "|" + field.getName() + " : " + field.getType().getPresentableText();
  }

  /**
   * Method for create a presentable String out of the
   * name refactoring.
   *
   * @return presentable String that shows the changes if existent, else shows a presentable name.
   */
  public static String getDisplayableName(RefactoringInfo info) {
    if (info.getGroup() == Group.PACKAGE) {
      if (info.getNameBefore().equals(info.getNameAfter())) {
        return info.getNameBefore();
      } else {
        return info.getNameBefore() + " -> " + info.getNameAfter();
      }
    }
    String before = info.getNameBefore();
    String after = info.getNameAfter();

    if (info.getGroup() == Group.METHOD || info.getGroup() == Group.VARIABLE) {
      String paramsBefore = before.substring(before.lastIndexOf("("));
      String paramsAfter = after.substring(after.lastIndexOf("("));
      before = before.substring(0, before.lastIndexOf("("));
      after = after.substring(0, after.lastIndexOf("("));
      before = before.substring(before.lastIndexOf(".") + 1);
      after = after.substring(after.lastIndexOf(".") + 1);
      before = before + paramsBefore;
      after = after + paramsAfter;
    } else if (info.getGroup() != Group.ATTRIBUTE) {
      if (before.contains(".")) {
        before = info.getNameBefore().substring(info.getNameBefore().lastIndexOf(".") + 1);
      }
      if (after.contains(".")) {
        after = info.getNameAfter().substring(info.getNameAfter().lastIndexOf(".") + 1);
      }
    }
    if (before.equals(after)) {
      return before;
    } else {
      return before + " -> " + after;
    }
  }

  /**
   * Escapes the delimiter chars with '#'.
   *
   * @param s string to sanitize
   * @return escaped s
   */
  public static String sanitize(String s) {
    s = s.replaceAll(ESC, ESC + ESC);
    for (String d : delimiters) {
      s = s.replaceAll(d, ESC + d);
    }
    return s;
  }

  /**
   * Remove escape chars.
   *
   * @param s string to desanitize.
   * @return clean s
   */
  public static String deSanitize(String s) {
    for (String d : delimiters) {
      s = s.replaceAll(ESC + d, d);
    }
    return s.replaceAll(ESC + ESC, ESC);
  }

  public static String pathToClassName(String name) {
    return name.substring(name.indexOf('/') + 1, name.lastIndexOf('.'))
        .replace('/', '.');
  }
}
