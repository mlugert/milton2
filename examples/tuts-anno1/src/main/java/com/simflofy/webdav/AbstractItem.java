package com.simflofy.webdav;

import java.io.File;

/**
 * @author mlugert
 *
 */
public abstract class AbstractItem
{
	private String name;
	private String parentPath;
	private boolean isFolder;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getParentPath()
	{
		return parentPath;
	}

	public void setParentPath(String parentPath)
	{
		this.parentPath = parentPath;
	}

	public boolean isFolder()
	{
		return isFolder;
	}

	public void setFolder(boolean isFolder)
	{
		this.isFolder = isFolder;
	}
	
	public String getPath()
	{
		File thisFolder = new File(this.getParentPath(), this.getName());
		return thisFolder.getAbsolutePath();
	}

	public String toString()
	{
		return "name: " + name + ", parentPath: " + parentPath + ", isFolder: " + isFolder;
	}
}