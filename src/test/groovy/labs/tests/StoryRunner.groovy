package labs.tests

import org.junit.Test
import org.jbehave.core.embedder.Embedder
import org.jbehave.core.io.StoryFinder
import static org.jbehave.core.io.CodeLocations.*

/**
 * Created with IntelliJ IDEA.
 * User: jk
 * Date: 11/04/12
 * Time: 17:21
 * To change this template use File | Settings | File Templates.
 */
class StoryRunner {

    @Test
    public void runClasspathLoadedStoriesAsJUnit() {
        // Embedder defines the configuration and candidate steps
        Embedder embedder = new ScriptLibEmbedder();
        // use StoryFinder to look up paths
        List<String> storyPaths = storyPaths();
        embedder.runStoriesAsPaths(storyPaths);
    }


    protected List<String> storyPaths() {
        return new StoryFinder().findPaths(codeLocationFromClass(this.getClass()), "**/stories/*.story", "");
    }

/*    @Test
    public void runURLLoadedStoriesAsJUnit() {
        // Embedder defines the configuration and candidate steps
        Embedder embedder = new URLTraderEmbedder();
        List<String> storyPaths = ... // use StoryFinder to look up paths
        embedder.runStoriesAsPaths(storyPaths);
    }*/

}
