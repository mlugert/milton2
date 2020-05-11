package com.simflofy.webdav;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import io.milton.annotations.Authenticate;
import io.milton.annotations.ChildrenOf;
import io.milton.annotations.ContentLength;
import io.milton.annotations.Copy;
import io.milton.annotations.CreatedDate;
import io.milton.annotations.Delete;
import io.milton.annotations.DisplayName;
import io.milton.annotations.Get;
import io.milton.annotations.MakeCollection;
import io.milton.annotations.MaxAge;
import io.milton.annotations.ModifiedDate;
import io.milton.annotations.Move;
import io.milton.annotations.Name;
import io.milton.annotations.PutChild;
import io.milton.annotations.ResourceController;
import io.milton.annotations.Root;
import io.milton.annotations.UniqueId;

@ResourceController
public class WebDavService
{
	private static Logger log = Logger.getLogger(WebDavService.class);

	private List<Repo> repos = new ArrayList<Repo>();

	public WebDavService()
	{
		repos.add(new Repo("C:\\test", "s3"));
		repos.add(new Repo("C:\\test", "output"));
	}

	@Authenticate
	public Boolean verifyPassword(SimflofyFolder item, String requestedPassword)
	{
		log.debug("Got request to verify password in Simflofy WebDav.");
		return true;
	}

	@Name
	public String getResourceName(Repo repo)
	{
		return repo.getName();
	}

	@Name
	public String getResourceName(WebDavService root)
	{
		log.debug("Getting WebDavService Root Name");
		return "Simflofy Webdav";
	}

	@Root
	public WebDavService getRoot()
	{
		return this;
	}

	@ChildrenOf
	public List<Repo> getRepos(WebDavService root)
	{
		log.debug("Getting list of repos");
		return repos;
	}

	@MakeCollection
	public SimflofyFolder createFolder(SimflofyFolder parent, String name)
	{
		log.debug("Creating folder with parent: " + parent.getPath() + ", and name: " + name);
		return parent.addFolder(name);
	}

	// public MyDatabase.FileContentItem createFile(MyDatabase.FolderContentItem parent, String name, InputStream in, Long contentLength, String contentType) {
	// public MyDatabase.FileContentItem createFile(MyDatabase.FolderContentItem parent, String name, byte[] bytes) {
	@PutChild
	public SimflofyFile createFile(SimflofyFolder parent, String name, byte[] content) throws IOException
	{
		log.debug("creating a file with path: " + parent.getPath() + ", and name: " + name);
		SimflofyFile pf = new SimflofyFile(parent.getPath(), name, content);
		return pf;
	}

	@ChildrenOf
	public List<AbstractItem> getRepoChildren(Repo repo) throws IOException
	{
		log.debug("getting repo children: " + repo);
		return repo.getChildren();
	}

	@ChildrenOf
	public List<AbstractItem> getFolderChildren(SimflofyFolder folder) throws IOException
	{
		log.debug("getting folder children: " + folder.getPath());
		return folder.getChildren();
	}

	@Get
	public void getFileContent(SimflofyFile item, OutputStream out) throws UnsupportedEncodingException, IOException
	{
		log.debug("getting file content: " + item.getPath());
		out.write(item.getBytes());
	}

	@ContentLength
	public Long getContentLength(AbstractItem item)
	{
		if (!item.isFolder())
		{
			long size = ((SimflofyFile) item).getFileSize();
			log.debug("File Size: " + item.getName() + "=" + size);
			return size;
		}

		return new Long(0);
	}

	@Name
	public String getResourceName(AbstractItem item)
	{
		//log.debug("resource name: " + item.getPath());
		return item.getName();
	}

	/*
	@MaxAge
	public int getMaxCacheInSeconds(AbstractItem item)
	{
		// Returns the max time to cache an item in seconds
		// Folders could have things changing more often than files, so cache those for less time
		// But you also don't want someone refreshing a million times and overloading a server
		if (item.isFolder())
		{
			return 0;
		} else
		{
			return 30;
		}
	}
	*/

	@Move
	public void move(AbstractItem source, AbstractItem newParent, String newName)
	{
		if (null == newName || newName.length() <= 0)
		{
			newName = newParent.getName();
		}
		File s = new File(source.getParentPath(), source.getName());
		File t = new File(newParent.getPath(), newName);

		log.debug("Move Called: " + s.getAbsolutePath() + "->" + t.getAbsolutePath());
		s.renameTo(t);
	}

	@Copy
	public void copy(AbstractItem source, AbstractItem newParent, String newName) throws IOException
	{
		if (null == newName || newName.length() <= 0)
		{
			newName = newParent.getName();
		}
		File s = new File(source.getParentPath(), source.getName());
		File t = new File(newParent.getParentPath(), newName);

		FileUtils.copyFile(s, t);
	}

	@Delete
	public void delete(AbstractItem source)
	{
		File s = new File(source.getParentPath(), source.getName());
		s.delete();
	}

	@DisplayName
	public String getDisplayName(AbstractItem source)
	{
		return source.getName();
	}

	@UniqueId
	public String getUniqueId(AbstractItem source)
	{
		File s = new File(source.getParentPath(), source.getName());
		return s.getAbsolutePath();
	}

	@ModifiedDate
	public Date getModifiedDate(AbstractItem source)
	{
		log.debug("getting modified date: " + source);
		File s = new File(source.getParentPath(), source.getName());
		return new Date(s.lastModified());
	}

	@CreatedDate
	public Date getCreatedDate(AbstractItem source) throws IOException
	{
		log.debug("getCreatedDate for source: " + source);
		File s = new File(source.getParentPath(), source.getName());
		FileTime creationTime = (FileTime) Files.getAttribute(Paths.get(s.getAbsolutePath()), "creationTime");

		return new Date(creationTime.toMillis());
	}

	public static void main(String[] args) throws IOException
	{
		WebDavService dav = new WebDavService();
		List<Repo> repos = dav.getRepos(dav);

		for (Repo repo : repos)
		{
			List<AbstractItem> children = dav.getRepoChildren(repo);

			for (AbstractItem child : children)
			{
				// createFolder
				// createFile
				// getFolderChildren
				// getFileContent
				// getResourceName
				// move
				// copy
				// delete
				// getDisplayName
				// getUniqueId
				// getModifiedDate
				// getCreatedDate
			}
		}

	}
}
