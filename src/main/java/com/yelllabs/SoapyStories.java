package com.yelllabs;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.steps.pico.PicoStepsFactory;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser;
import org.jbehave.core.reporters.CrossReference;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.SilentStepMonitor;
import org.picocontainer.Characteristics;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;
import org.picocontainer.behaviors.Storing;
import org.picocontainer.behaviors.ThreadCaching;
import org.picocontainer.classname.ClassLoadingPicoContainer;
import org.picocontainer.classname.ClassName;
import org.picocontainer.classname.DefaultClassLoadingPicoContainer;
import org.picocontainer.injectors.CompositeInjection;
import org.picocontainer.injectors.ConstructorInjection;
import org.picocontainer.injectors.SetterInjection;

import java.text.SimpleDateFormat;
import java.util.List;

import static java.util.Arrays.asList;
import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;
import static org.jbehave.core.reporters.Format.*;

/**
 * Created with IntelliJ IDEA.
 * User: jk
 * Date: 17/04/12
 * Time: 10:14
 * To change this template use File | Settings | File Templates.
 */
public class SoapyStories extends JUnitStories {


    public SoapyStories() {

        Configuration configuration = configuration();
        useConfiguration(configuration);

        final ThreadCaching primordialCaching = new ThreadCaching();
        MutablePicoContainer primordial = new PicoBuilder().withBehaviors(primordialCaching).build();

        final Storing store = (Storing) new Storing().wrap(new CompositeInjection(new ConstructorInjection(),
                new SetterInjection("set", "setMetaClass")));
        ClassLoader currentClassLoader = this.getClass().getClassLoader();


        final DefaultClassLoadingPicoContainer pageObjects = new DefaultClassLoadingPicoContainer(currentClassLoader, store, primordial);
        pageObjects.change(Characteristics.USE_NAMES);


        ClassLoadingPicoContainer steps = pageObjects.makeChildContainer("steps");
        steps.addComponent(new ClassName("com.yelllabs.tests.steps.json.JSONHandlerScenarioSteps"));
        useStepsFactory(new PicoStepsFactory(configuration, steps));

    }

    @Override
    protected List<String> storyPaths() {
        return new StoryFinder().findPaths(codeLocationFromClass(this.getClass()).getFile(), asList("**/" + System.getProperty("storyFilter", "*")
                + ".story"), null);
    }

    @Override
    public Configuration configuration() {
        Class<? extends SoapyStories> embedderClass = this.getClass();
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
}
