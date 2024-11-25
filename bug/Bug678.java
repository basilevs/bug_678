import static java.lang.System.currentTimeMillis;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Bug678 {
    public static void main(String[] args) {
		Shell shell = new Shell();
		try {
			Image image = new Image(shell.getDisplay(), 20, 20);
			try {
				shell.setSize(200, 200);
				shell.setLayout(new FillLayout());
				shell.open();
				waitUntilIdle();
				for (int i = 0; i < 200; i++) {
					Tree tree = new Tree(shell, SWT.VIRTUAL);
					tree.addListener(SWT.SetData, e -> {
						TreeItem item = (TreeItem) e.item;
						item.setText(0, "A");
						item.setImage(image); // <-- this is the critical line!
					});
					waitUntilIdle(); // slightly increase crash probability by preventing unrelated background processing on the next line
					tree.setItemCount(1);

					waitUntilIdle(); // may crash while processing asynchronous events

					tree.dispose();
				}		
			} finally {
				image.dispose();
			}
		} finally {
			shell.dispose();
		}
	}

	private static void waitUntilIdle() {
		long lastActive = currentTimeMillis();
		while (true) {
			if (Thread.interrupted()) {
				throw new AssertionError();
			}
			if (Display.getCurrent().readAndDispatch()) {
				lastActive = currentTimeMillis();
			} else {
				if (lastActive + 10 < currentTimeMillis()) {
					return;
				}
				Thread.yield();
			}
		}
	}

}
