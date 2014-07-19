package dbg.data.media;

import dbg.data.media.element.MediaElement;
import dbg.data.media.element.generated.GeneratedMediaElement;
import dbg.data.media.element.imported.ImageElement;
import dbg.data.media.element.imported.ImportedMediaElement;
import dbg.ui.LocalizedText;
import dbg.util.ActivityMonitor;
import dbg.util.Logger;
import dbg.util.ProgressState;
import dbg.util.TemporaryFileHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Process a MediaSequence to generate all image from GeneratedMediaElement. The result is a MediaSequence containing
 * only ImportedMediaElement (VideoElement and ImageElement).
 */
public class MediaSequenceImagesGenerator {

	private final TemporaryFileHandler temporaryFileHandler;
	private final MediaSequence sequence;
	private final Logger logger;

	public MediaSequenceImagesGenerator(TemporaryFileHandler temporaryFileHandler, MediaSequence sequence, Logger logger) {
		this.temporaryFileHandler = temporaryFileHandler;
		this.sequence = sequence;
		this.logger = logger;
	}

	public MediaSequence generate(ProgressState state) {
		MediaSequence outputMediaSequence = new MediaSequence();

		final ActivityMonitor monitor = state.newState(LocalizedText.get("generating_elements"), 100);

		final List<Future<Result>> futureFiles = new ArrayList<>();
		final CompletionService<Result> completionService = new ExecutorCompletionService<>(
						Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1)
		);

		List<GeneratedMediaElement> generatedMediaElements = sequence.getElementsByClass(GeneratedMediaElement.class);
		for (final GeneratedMediaElement element : generatedMediaElements) {
			futureFiles.add(completionService.submit(new Callable<Result>() {
				@Override
				public Result call() throws Exception {
					try {
						return new Result(
								element,
								new ImageElement(
										element.getTitle(),
										element.generateImage(temporaryFileHandler),
										element.getDuration()
								),
								null
						);
					} catch (Throwable t) {
						return new Result(element, null, t);
					}
				}
			}));
		}

		final Map<Integer, ImportedMediaElement> generatedElements = new HashMap<>();
		for (int i1 = 0, size = futureFiles.size(); i1 < size; i1++) {
			try {
				Future<Result> futureFile = completionService.take();
				Result res = futureFile.get();
				if (res != null) {
					if (res.error != null) {
						logger.error("Error while generating originalElement : " + res.originalElement.getTitle() + " [" + res.originalElement.getSubTitle() + "] ");
						logger.error(res.error);
					} else if (res.generatedElement != null) {
						logger.info(res.originalElement.getClass().getSimpleName() + " generated to '" + res.generatedElement.getFile().getAbsolutePath() + "'");
						generatedElements.put(sequence.indexOf(res.originalElement), res.generatedElement);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			monitor.setProgress(null, i1 * 100 / size);
		}

		for (int i = 0, sequenceSize = sequence.size(); i < sequenceSize; i++) {
			MediaElement element = sequence.get(i);

			if (generatedElements.containsKey(i))
				outputMediaSequence.add(generatedElements.get(i));
			else if (element instanceof ImportedMediaElement)
				outputMediaSequence.add(element);
		}

		return outputMediaSequence;
	}

	private static class Result {
		final GeneratedMediaElement originalElement;
		final ImportedMediaElement generatedElement;
		final Throwable error;

		private Result(GeneratedMediaElement originalElement, ImportedMediaElement generatedElement, Throwable error) {
			this.originalElement = originalElement;
			this.generatedElement = generatedElement;
			this.error = error;
		}
	}

}
