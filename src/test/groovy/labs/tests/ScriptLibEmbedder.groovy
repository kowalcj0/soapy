package labs.tests

import org.jbehave.core.embedder.Embedder
import org.jbehave.core.embedder.EmbedderControls
import org.jbehave.core.configuration.Configuration
import org.jbehave.core.configuration.MostUsefulConfiguration
import org.jbehave.core.io.LoadFromClasspath
import org.jbehave.core.reporters.StoryReporterBuilder
import org.jbehave.core.reporters.CrossReference
import org.jbehave.core.steps.ParameterConverters
import java.text.SimpleDateFormat
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser
import org.jbehave.core.steps.SilentStepMonitor
import org.jbehave.core.steps.InjectableStepsFactory
import org.jbehave.core.steps.InstanceStepsFactory
import static org.jbehave.core.reporters.Format.*

import labs.tests.json.JSONHandlerScenarioSteps

/**
 * Created with IntelliJ IDEA.
 * User: jk
 * Date: 11/04/12
 * Time: 17:22
 * To change this template use File | Settings | File Templates.
 */
class ScriptLibEmbedder extends Embedder {

    @Override
    public EmbedderControls embedderControls() {
        return new EmbedderControls().doIgnoreFailureInStories(true).doIgnoreFailureInView(true);
    }

    @Override
    public Configuration configuration() {
        Class<? extends ScriptLibEmbedder> embedderClass = this.getClass();
        return new MostUsefulConfiguration()
                .useStoryLoader(new LoadFromClasspath(embedderClass.getClassLoader()))
                .useStoryReporterBuilder(new StoryReporterBuilder()
                .withCodeLocation( org.jbehave.core.io.CodeLocations.codeLocationFromClass(embedderClass))
                .withDefaultFormats()
                .withFormats(CONSOLE, TXT, HTML, XML)
                .withCrossReference(new CrossReference()))
                .useParameterConverters(new ParameterConverters()
                .addConverters(new ParameterConverters.DateConverter(new SimpleDateFormat("yyyy-MM-dd")))) // use custom date pattern
                .useStepPatternParser(new RegexPrefixCapturingPatternParser("%")) // use '%' instead of '$' to identify parameters
                .useStepMonitor(new SilentStepMonitor());
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), new JSONHandlerScenarioSteps());
    }

}