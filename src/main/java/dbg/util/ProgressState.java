package dbg.util;

/**
 * Keeps track of progress during assembly
 */
public class ProgressState {
	private final ActivityMonitor monitor;
	private int progress = 0;
	private State currentState;

	public ProgressState(ActivityMonitor monitor) {
		this.monitor = monitor;
	}

	public ActivityMonitor newState(String description, int weight) {
		if (monitor != null) {
			if (currentState != null)
				progress = currentState.weight;
			currentState = new State(description, weight);
			monitor.setProgress(currentState.description, progress);
		}

		return new ActivityMonitor() {
			@Override
			public void setProgress(String message, int newProgress) {
				if (monitor != null) {
					currentState.progress = newProgress;
					monitor.setProgress(message, progress + (currentState.progress * currentState.weight / 100));
				}
			}
		};
	}

	private static class State {
		int progress = 0;
		String description;
		int weight;

		State(String description, int weight) {
			this.description = description;
			this.weight = weight;
		}
	}
}
