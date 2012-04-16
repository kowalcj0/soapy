package labs.tests

import org.junit.runner.RunWith
import org.jbehave.core.annotations.Configure
import org.jbehave.core.annotations.UsingEmbedder
import org.jbehave.core.annotations.UsingSteps
import org.jbehave.core.junit.AnnotatedEmbedderRunner
import org.jbehave.core.embedder.Embedder
import org.jbehave.core.InjectableEmbedder
import labs.tests.json.JSONHandlerScenarioSteps
import org.junit.Test
import org.jbehave.core.io.StoryFinder
import static org.jbehave.core.io.CodeLocations.*
import  static org.jbehave.core.reporters.Format.*
import org.jbehave.core.embedder.StoryControls
import org.jbehave.core.io.LoadFromClasspath
import org.jbehave.core.reporters.StoryReporterBuilder
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser
import org.jbehave.core.steps.ParameterConverters
import java.text.SimpleDateFormat

/**
 * Created with IntelliJ IDEA.
 * User: jk
 * Date: 13/04/12
 * Time: 16:57
 * To change this template use File | Settings | File Templates.
 */
@RunWith(AnnotatedEmbedderRunner.class)
@Configure(storyControls = MyStoryControls.class, storyLoader = MyStoryLoader.class, storyReporterBuilder = MyReportBuilder.class)
@UsingEmbedder(embedder = Embedder.class, generateViewAfterStories = true, ignoreFailureInStories = true,
ignoreFailureInView = true, verboseFailures = true, storyTimeoutInSecs = 100L, threads = 1, metaFilters = "-skip")
@UsingSteps(instances = [JSONHandlerScenarioSteps.class ])
class crap extends InjectableEmbedder {

    @Test
    public void run() {
        List<String> storyPaths = new StoryFinder().findPaths(codeLocationFromClass(this.getClass()), "**stories/*.story", "");
        injectedEmbedder().runStoriesAsPaths(storyPaths);
    }

    public static class MyStoryControls extends StoryControls {
        public MyStoryControls() {
            doDryRun(false);
            doSkipScenariosAfterFailure(false);
        }
    }

    public static class MyStoryLoader extends LoadFromClasspath {
        public MyStoryLoader() {
            super();
        }
    }

    public static class MyReportBuilder extends StoryReporterBuilder {
        public MyReportBuilder() {
            this.withFormats(CONSOLE, TXT, HTML, XML).withDefaultFormats();
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