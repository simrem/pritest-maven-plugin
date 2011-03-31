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
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class Technique4Ranker {
	
	private List<String> localTestClasses = new ArrayList<String>();

	public Technique4Ranker(List<String> localTestClasses) {
		this.localTestClasses = localTestClasses;
	}
	
	public List<String> getTechnique4PriorityList() {
		List<String> gitStatusList = new ArrayList<String>();
		try {
			gitStatusList = callGitStatus();
		} catch (NoWorkTreeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		
		// Maa faa tak i parent-project basedir her, og sette som repoPath.
		File repoPath = new File("/Users/oyvindvol/dev/citrus-junit-runner/.git");
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
		if(fileName.endsWith(".java")) {
			
			if (!(fileName.substring(fileName.length()-9, fileName.length()-5).equals("Test"))) {
				
				if (!listToAddStringTo.contains(fileName)) {
					fileName = fileName.replaceAll("/", ".");
					listToAddStringTo.add(fileName.substring(0, fileName.length()-5) + "Test");
					return true;
				}
			}
		}
		
		return false;
	} 
	
	public List<String> getLocalTestClasses() {
		return localTestClasses;
	}
}
