package dbg.util;

import junit.framework.TestCase;

public class ProgressStateTest extends TestCase {

	public void testNewState() throws Exception {
		ActivityMonitor monitor = new ActivityMonitor() {
			@Override
			public void setProgress(String message, int progress) {
				System.out.println("["+progress+"%]" + (message == null ? "" : message));
			}
		};

		ProgressState progressState = new ProgressState(monitor);

		ActivityMonitor monitor1 = progressState.newState("State A", 25);
		{
			ProgressState progressState1 = new ProgressState(monitor1);
			ActivityMonitor monitor2 = progressState1.newState("sub-state A-1", 25);
			monitor2.setProgress(null, 25);
			monitor2.setProgress(null, 50);
			monitor2.setProgress(null, 100);

			monitor2 = progressState1.newState("sub-state A-2", 50);
			monitor2.setProgress(null, 25);
			monitor2.setProgress(null, 50);
			monitor2.setProgress(null, 100);

			monitor2 = progressState1.newState("sub-state A-2", 25);
			monitor2.setProgress(null, 25);
			monitor2.setProgress(null, 50);
			monitor2.setProgress(null, 100);
		}

		monitor1 = progressState.newState("State B", 75);
		monitor1.setProgress(null, 25);
		monitor1.setProgress(null, 50);
		monitor1.setProgress(null, 100);
	}
}