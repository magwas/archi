package uk.ac.bolton.archimate.editor.diagram.dialog;

import org.eclipse.emf.common.util.URI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class TargetSelectionDialog {
	URI location;
	Shell parent;
	int openOrSave;
	private FileDialog dialog;
	public TargetSelectionDialog(Shell parentShell, int openOrSave) {
        super();
        parent = parentShell;
        this.openOrSave = openOrSave;
    }
	public URI open() {
		Display display = parent.getDisplay ();

		final Shell shell = new Shell(parent, SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
		shell.setText("Choose target");
	    shell.setLayout(new GridLayout(2, false));

	    Label nameLabel = new Label(shell, SWT.NULL);
	    nameLabel.setText("CDO url:");
	    final Text nameText = new Text(shell, SWT.SINGLE | SWT.BORDER);
	    nameText.setText("cdo://127.0.0.1/path/to/resource");

	    final Button buttonCDO = new Button(shell, SWT.PUSH);
	    buttonCDO.setText("CDO");
	    final Button buttonFile = new Button(shell, SWT.PUSH);
	    buttonFile.setText("File...");

	    Button buttonCancel = new Button(shell, SWT.PUSH);
	    buttonCancel.setText("Cancel");

        buttonCDO.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	location = URI.createURI(nameText.getText());
            	shell.dispose();
            }
        });
        buttonCancel.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	location = null;
            	shell.dispose();
            }
        });
        buttonFile.addSelectionListener(new SelectionAdapter() {
			@Override
            public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog (shell, openOrSave);
				setDialog(dialog);
        		String [] filterNames = new String [] {"Archi files", "All Files (*)"};
        		String [] filterExtensions = new String [] {"*.archimate", "*"};
        		String platform = SWT.getPlatform();
        		if (platform.equals("win32") || platform.equals("wpf")) {
        			filterExtensions = new String [] {"*.archimate", "*"};
        		}
        		dialog.setFilterNames (filterNames);
        		dialog.setFilterExtensions (filterExtensions);
            	location = URI.createFileURI(dialog.open());
            	shell.dispose();
            }
        });
	    shell.pack();
	    shell.open();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		return location;
		
	}
	public void setDialog(FileDialog dialog) {
		this.dialog = dialog;
	}
	public Integer getFilterIndex() {
		if(dialog == null) {
			return null;
		}
		return dialog.getFilterIndex();
	}
}
