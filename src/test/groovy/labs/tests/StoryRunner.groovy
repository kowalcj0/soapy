package labs.tests

import org.junit.Test
import org.jbehave.core.embedder.Embedder
import org.jbehave.core.io.StoryFinder
import static org.jbehave.core.io.CodeLocations.*
import static java.util.Arrays.*


class StoryRunner {

    @Test
    public void runClasspathLoadedStoriesAsJUnit() {
        Embedder embedder = new SoapyEmbedder(); // Embedder defines the configuration and candidate steps
        List<String> storyPaths = storyPaths(); // use StoryFinder to look up paths
        embedder.runStoriesAsPaths(storyPaths);
    }

    protected List<String> storyPaths() {
        List<String> list = new StoryFinder().findPaths(codeLocationFromClass(this.getClass()).getFile(),
                asList("stories/*.story"), asList(""));
        return list;
    }


}
