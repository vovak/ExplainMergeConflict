package data.types.methods;

import static data.RefactoringLine.MarkingOption.ADD;

import data.RefactoringInfo;
import data.types.Handler;
import gr.uom.java.xmi.UMLAnnotation;
import gr.uom.java.xmi.diff.AddMethodAnnotationRefactoring;
import org.refactoringminer.api.Refactoring;
import utils.Utils;

public class AddMethodAnnotationHandler extends Handler {

  @Override
  public RefactoringInfo specify(Refactoring refactoring, RefactoringInfo info) {
    AddMethodAnnotationRefactoring ref = (AddMethodAnnotationRefactoring) refactoring;
    UMLAnnotation annotation = ref.getAnnotation();

    String classNameBefore = ref.getOperationBefore().getClassName();
    String classNameAfter = ref.getOperationAfter().getClassName();

    return info.setGroup(RefactoringInfo.Group.METHOD)
        .setDetailsBefore(classNameBefore)
        .setDetailsAfter(classNameAfter)
        .setElementBefore(annotation.toString())
        .setElementAfter(null)
        .addMarking(
            ref.getOperationBefore().codeRange(),
            annotation.codeRange(),
            line -> line.addOffset(
                annotation.getLocationInfo(),
                ADD),
            ADD,
            false)
        .setNameBefore(Utils.calculateSignature(ref.getOperationBefore()))
        .setNameAfter(Utils.calculateSignature(ref.getOperationBefore()));
  }
}
