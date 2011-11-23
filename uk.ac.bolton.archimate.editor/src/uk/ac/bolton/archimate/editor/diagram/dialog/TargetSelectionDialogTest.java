package uk.ac.bolton.archimate.editor.diagram.dialog;

import static org.junit.Assert.*;

import org.eclipse.emf.common.util.URI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.junit.Test;


public class TargetSelectionDialogTest  {

	@Test
	public void test() {
        TargetSelectionDialog dialog = new TargetSelectionDialog(Display.getCurrent().getActiveShell(),SWT.OPEN);
        URI path = dialog.open();
        System.out.println("path="+path);
		fail("Not yet implemented");
	}

}
