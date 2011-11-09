package no.pritest.runner.junit.priority;

import no.pritest.vcs.VCSStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by IntelliJ IDEA.
 * User: sveinung dalatun
 * Date: 11/7/11
 * Time: 10:18 PM
 */
public class VCSStatusProviderTest {

    private VCSStatusProvider provider;

    @Mock
    private VCSStatus status;
    @Mock
    private File basedir;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        
        when(basedir.getPath()).thenReturn("basedir/a");
        when(basedir.getName()).thenReturn("a");
    }

    @Test
    public void should_return_list_of_tests_to_run_given_status() throws IOException {
        HashSet<String> modified = new HashSet<String>();
        modified.add("src/test/java/no/ATest.java");
        modified.add("src/test/java/no/b/B.java");

        when(status.getModified()).thenReturn(modified);
        when(status.getUntracked()).thenReturn(new HashSet<String>());

        String testSrcDir = "basedir/a/src/test/java";
        String srcDir = "basedir/a/src/main/java";

        provider = new VCSStatusProvider(basedir, srcDir, testSrcDir, status);

        assertThat(provider.getGitStatusPriorityList(),
                hasItems("no.ATest", "no.b.BTest"));
    }
}
