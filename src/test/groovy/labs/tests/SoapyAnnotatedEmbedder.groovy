package labs.tests

import org.junit.runner.RunWith
import org.jbehave.core.annotations.Configure
import org.jbehave.core.annotations.UsingEmbedder
import org.jbehave.core.annotations.UsingSteps
import org.jbehave.core.Embeddable
import org.jbehave.core.embedder.Embedder
import org.junit.Test
import org.jbehave.core.io.StoryFinder
import labs.tests.json.JSONHandlerScenarioSteps
import org.jbehave.core.junit.AnnotatedEmbedderRunner
import static org.jbehave.core.io.CodeLocations.*
import static java.util.Arrays.asList
import java.text.SimpleDateFormat
import org.jbehave.core.steps.ParameterConverters
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser
import org.jbehave.core.reporters.StoryReporterBuilder
import org.jbehave.core.io.LoadFromClasspath

/**
 * Created with IntelliJ IDEA.
 * User: jk
 * Date: 12/04/12
 * Time: 14:35
 * To change this template use File | Settings | File Templates.
 */
@RunWith(AnnotatedEmbedderRunner.class)
@Configure(storyLoader = MyStoryLoader.class, storyReporterBuilder = MyReportBuilder.class, parameterConverters = [ MyDateConverter.class ])
@UsingEmbedder(embedder = Embedder.class, generateViewAfterStories = true, ignoreFailureInStories = true, ignoreFailureInView = true)
@UsingSteps(instances = [ JSONHandlerScenarioSteps.class ])
class SoapyAnnotatedEmbedder implements Embeddable {

    private Embedder embedder;

    public void useEmbedder(Embedder embedder) {
        this.embedder = embedder;
    }

    @Test
    public void run() {
        embedder.runStoriesAsPaths(storyPaths());
    }

    protected List<String> storyPaths() {
        // use StoryFinder to look up paths
        return new StoryFinder().findPaths(codeLocationFromClass(
                this.getClass()).getFile(),
                asList("stories/*.story"),
                asList(""));

    }


    public static class MyStoryLoader extends LoadFromClasspath {
        public MyStoryLoader() {
            super(SoapyAnnotatedEmbedder.class.getClassLoader());
        }
    }

    public static class MyReportBuilder extends StoryReporterBuilder {
        public MyReportBuilder() {
            this.withFormats(org.jbehave.core.reporters.Format.CONSOLE, org.jbehave.core.reporters.Format.TXT,
                    org.jbehave.core.reporters.Format.HTML, org.jbehave.core.reporters.Format.XML).withDefaultFormats();
        }
    }

    public static class MyRegexPrefixCapturingPatternParser extends RegexPrefixCapturingPatternParser {
        public MyRegexPrefixCapturingPatternParser() {
            super("%");
        }
    }

    public static class MyDateConverter extends ParameterConverters.DateConverter {
        public MyDateConverter() {
            super(new SimpleDateFormat("yyyy-MM-dd"));
        }
    }

}