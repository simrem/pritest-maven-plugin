package no.citrus.runner.junit;

import java.net.URL;
import java.net.URLClassLoader;

public class AppendableClassLoader extends URLClassLoader {

	public AppendableClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	@Override
	public void addURL(URL url) {
		super.addURL(url);
	}
}
