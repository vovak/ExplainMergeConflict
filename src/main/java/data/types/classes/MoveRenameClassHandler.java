package data.types.classes;

import data.RefactoringInfo;
import data.types.Handler;
import gr.uom.java.xmi.diff.MoveAndRenameClassRefactoring;
import org.refactoringminer.api.Refactoring;

public class MoveRenameClassHandler extends Handler {

  @Override
  public RefactoringInfo specify(Refactoring refactoring, RefactoringInfo info) {
    MoveAndRenameClassRefactoring ref = (MoveAndRenameClassRefactoring) refactoring;

    if (ref.getRenamedClass().isAbstract()) {
      info.setGroup(RefactoringInfo.Group.ABSTRACT);
    } else if (ref.getRenamedClass().isInterface()) {
      info.setGroup(RefactoringInfo.Group.INTERFACE);
    } else {
      info.setGroup(RefactoringInfo.Group.CLASS);
    }
    return info
        .addMarking(ref.getOriginalClass().codeRange(), ref.getRenamedClass().codeRange(), true)
        .setNameBefore(ref.getOriginalClassName())
        .setNameAfter(ref.getRenamedClassName())
        .setDetailsBefore(ref.getOriginalClass().getPackageName())
        .setDetailsAfter(ref.getRenamedClass().getPackageName());
  }
}
