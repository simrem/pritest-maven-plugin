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


public class Technique4Ranker {
	
	private List<String> localTestClasses = new ArrayList<String>();
	private File basedir;

	public Technique4Ranker(List<String> localTestClasses, File basedir) {
		this.localTestClasses = localTestClasses;
		this.basedir = basedir;
	}
	
	public List<String> getTechnique4PriorityList() throws NoWorkTreeException, IOException {
		List<String> gitStatusList = new ArrayList<String>();
		gitStatusList = callGitStatus();
		List<String> finalList = new ArrayList<String>();
		
		finalList.addAll(gitStatusList);
		
		for (String localTestClass : localTestClasses) {
			if (!finalList.contains(localTestClass)) {
					finalList.add(localTestClass);
			}
		}
		
		return finalList;
	}
	
	public List<String> callGitStatus() throws NoWorkTreeException, IOException {
		List<String> gitStatusList = new ArrayList<String>();
		
		File repoPath = new File(basedir.getAbsolutePath() + "/.git");
		RepositoryBuilder repoBuilder = new RepositoryBuilder();
		Repository repo = repoBuilder.setGitDir(repoPath).build();
		
		Git git = new Git(repo);
		Status status = git.status().call();
			
		for (String untrackedFile : status.getUntracked()) {
			addIfJavaSuffix(untrackedFile, gitStatusList);
		}
		
		for (String modifiedFile : status.getModified()) {
			addIfJavaSuffix(modifiedFile, gitStatusList);
		}
		
		return gitStatusList;
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
