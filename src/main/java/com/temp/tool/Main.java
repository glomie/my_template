package com.temp.tool;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;
import java.util.zip.ZipFile;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;

public class Main {

	public static void main(String[] args) throws IOException {
		Path parent = Paths.get("E:\\webapps");
		Path[] files = getPaths(parent, Files::isRegularFile);
		for (Path path2 : files) {
			if (path2.toString().toLowerCase().endsWith("web.war"))
				continue;
			Path resolve = parent.resolve(getPrefixName(path2.getFileName().toString()));
			try (ZipFile zipFile = new ZipFile(path2.toFile())) {
				zipFile.stream().filter(entry -> !entry.isDirectory()).forEach(entry -> {
					try {
						Path resolve2 = resolve.resolve(entry.getName());
						Files.createDirectories(resolve2.getParent());
						try (InputStream is = zipFile.getInputStream(entry)) {
							Files.copy(is, resolve2, StandardCopyOption.REPLACE_EXISTING);
						}
						Files.setLastModifiedTime(resolve2, entry.getLastModifiedTime());
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
			}
		}

		Path[] paths = getPaths(parent, Files::isDirectory);
		System.out.println(
				"insert into access_api_tb(id,access_api_project,access_api_path,access_api_method,access_api_alias,access_api_description,create_time,update_time) values");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		boolean first = true;
		for (Path path2 : paths) {
			List<Model> list = new ArrayList<>();
			Path classes = path2.resolve("WEB-INF/classes");
			URL[] urls = java.util.stream.Stream
					.concat(java.util.stream.Stream.of(classes), Files.list(path2.resolve("WEB-INF/lib"))).map(path -> {
						try {
							return path.toUri().toURL();
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}).toArray(URL[]::new);
			URLClassLoader classloader = URLClassLoader.newInstance(urls);
			Files.walk(classes).filter(Files::isRegularFile).forEach(file -> {
				String name = classes.relativize(file).toString().replaceAll("[\\\\/]", ".");
				if (!name.endsWith(".class"))
					return;
				name = name.substring(0, name.length() - 6);

				try {
					Class<?> clazz = Class.forName(name, false, classloader);
					list.addAll(resolveClass(path2.getFileName().toString(), clazz));
				} catch (ClassNotFoundException e) {
					System.err.println(e);
				}
			});

			for (Model model : list) {
				String id = UUID.randomUUID().toString().replace("-", "").toUpperCase();
				String projectName = model.getProjectName();
				String path = model.getPath();
				String now = sdf.format(new Date());
				if (!first)
					System.out.println(",");
				System.out.printf("('%s','%s','%s','%s','%s','%s','%s','%s')", id, projectName, path,
						model.getRequestMethod(), "", "", now, now);
				first = false;
			}
		}
		System.out.println(";");
	}

	private static List<Model> resolveClass(String projectName, Class<?> clazz) {
		boolean hasPathOnClass = clazz.isAnnotationPresent(javax.ws.rs.Path.class);
		if (!hasPathOnClass)
			return Collections.emptyList();
		javax.ws.rs.Path resourcePath = clazz.getAnnotation(javax.ws.rs.Path.class);
		String resourcePathName = resourcePath.value();
		Method[] methods = clazz.getMethods();
		ArrayList<Model> list = new ArrayList<>();
		for (Method method : methods) {
			String reqMethod = null;
			if (method.isAnnotationPresent(GET.class))
				reqMethod = "GET";
			if (method.isAnnotationPresent(POST.class))
				reqMethod = "POST";
			if (method.isAnnotationPresent(PUT.class))
				reqMethod = "PUT";
			if (method.isAnnotationPresent(DELETE.class))
				reqMethod = "DELETE";
			if (reqMethod == null)
				continue;
			boolean hasPathOnMethod = method.isAnnotationPresent(javax.ws.rs.Path.class);
			String detailPath = "";
			if (hasPathOnMethod)
				detailPath = method.getAnnotation(javax.ws.rs.Path.class).value();
			Model model = new Model();
			model.setProjectName(projectName);
			model.setResourcePathName(resourcePathName);
			model.setDetailPath(detailPath);
			model.setRequestMethod(reqMethod);
			list.add(model);
		}
		return list;
	}

	private static String getPrefixName(String fileName) {
		int indexOf = fileName.lastIndexOf('.');
		if (indexOf > 0)
			return fileName.substring(0, indexOf);
		return fileName;
	}

	private static Path[] getPaths(Path parent, Predicate<Path> predicate) throws IOException {
		try (DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream(parent)) {
			return StreamSupport.stream(newDirectoryStream.spliterator(), false).filter(predicate).toArray(Path[]::new);
		}
	}
}

class Model {

	private String projectName;

	private String resourcePathName;

	private String detailPath;

	private String requestMethod;

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setResourcePathName(String resourcePathName) {
		this.resourcePathName = resourcePathName;
	}

	public void setDetailPath(String detailPath) {
		this.detailPath = detailPath;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public String getPath() {
		return ("/" + projectName + "/" + resourcePathName + "/" + detailPath).replaceAll("//++", "/");
	}

}
