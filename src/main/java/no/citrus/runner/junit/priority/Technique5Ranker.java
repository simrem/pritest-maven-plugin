package no.citrus.runner.junit.priority;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;

public class Technique5Ranker {
	
	private File basedir;

	public Technique5Ranker(File basedir) {
		this.basedir = basedir;
	}
	
	public List<String> getTechnique5PriorityList() throws NoWorkTreeException, IOException {
		List<String> gitStatusList = new ArrayList<String>();
		gitStatusList = callGitStatus();
		return gitStatusList;
	}
	
	public List<String> callGitStatus() throws NoWorkTreeException, IOException {
		List<String> callGitList = new ArrayList<String>();
		
		// Maa faa tak i parent-project basedir her, og sette som repoPath.
		File repoPath = new File(this.basedir.getAbsolutePath() + "/.git");
		RepositoryBuilder repoBuilder = new RepositoryBuilder();
		Repository repo = repoBuilder.setGitDir(repoPath).build();
		
		Git git = new Git(repo);
		Status status = git.status().call();
			
		for (String untrackedFile : status.getUntracked()) {
			addIfJavaSuffix(untrackedFile, callGitList);
		}
		
		for (String modifiedFile : status.getModified()) {
			addIfJavaSuffix(modifiedFile, callGitList);
		}
		
		return callGitList;
	}
	
	private boolean addIfJavaSuffix(String fileName, List<String> listToAddStringTo) {
		if(fileName.endsWith(".java") && fileName.startsWith("src/main/java/")) {
			fileName = fileName.substring("src/main/java/".length(), fileName.length() - ".java".length());
			if (fileName.length() > 0 && !fileName.endsWith("Test")) {
				if (!listToAddStringTo.contains(fileName)) {
					fileName = fileName.replaceAll("/", ".");
					fileName = fileName + "Test";
					listToAddStringTo.add(fileName);
					return true;
				}
			}
		}
		
		return false;
	} 
}
