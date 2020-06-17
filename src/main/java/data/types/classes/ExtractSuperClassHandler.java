package data.types.classes;

import data.Group;
import data.RefactoringInfo;
import data.RefactoringLine;
import data.types.Handler;
import gr.uom.java.xmi.diff.ExtractSuperclassRefactoring;
import org.refactoringminer.api.Refactoring;

public class ExtractSuperClassHandler extends Handler {

  @Override

  public RefactoringInfo specify(Refactoring refactoring, RefactoringInfo info) {

    ExtractSuperclassRefactoring ref = (ExtractSuperclassRefactoring) refactoring;

    if (ref.getExtractedClass().isInterface()) {
      info.setGroup(Group.INTERFACE);
    } else if (ref.getExtractedClass().isAbstract()) {
      info.setGroup(Group.ABSTRACT);
    } else {
      info.setGroup(Group.CLASS);
    }

    info.setDetailsBefore(ref.getExtractedClass().getPackageName())
        .setDetailsAfter(ref.getExtractedClass().getPackageName())
        .setNameBefore(ref.getExtractedClass().getName())
        .setNameAfter(ref.getExtractedClass().getName())
        .setMoreSided(true)
        .setRightPath(ref.getExtractedClass().codeRange().getFilePath());

    String[] nameSpace = ref.getExtractedClass().getName().split("\\.");
    String className = nameSpace[nameSpace.length - 1];

    ref.getUMLSubclassSet().forEach(subClass -> {
      info.addMarking(subClass.codeRange(), null, line ->
          line.setWord(new String[] {className, null, null}),
          RefactoringLine.MarkingOption.COLLAPSE, true);
    });


    return info;
  }
}
