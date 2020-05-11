package com.simflofy.webdav;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author mlugert
 *
 */
public class SimflofyFolder extends AbstractItem
{
	private static Logger log = Logger.getLogger(SimflofyFolder.class);
	
	public SimflofyFolder(String parent, String name)
	{
		this.setName(name);
		this.setParentPath(parent);
		this.setFolder(true);
	}

	public List<AbstractItem> getChildren() throws IOException
	{
		File f = new File(this.getParentPath(), this.getName());
		File[] files = f.listFiles();
		
		List<AbstractItem> items = new ArrayList<AbstractItem>();
		for(File file: files)
		{
			if(file.isDirectory())
			{
				SimflofyFolder folder = new SimflofyFolder(file.getParent(), file.getName());
				items.add(folder);
			}else
			{
				SimflofyFile newFile = new SimflofyFile(file.getParent(), file.getName());
				items.add(newFile);
			}
		}
		
		return items;
	}

	public SimflofyFolder addFolder(String name)
	{
		File thisFolder = new File(this.getParentPath(), this.getName());
		log.debug("Creating folder under parent: " + thisFolder.getAbsolutePath());
		File childFolder = new File(thisFolder, name);
		
		log.debug("Creating folder: " + childFolder.getAbsolutePath());
		if(!childFolder.exists())
			childFolder.mkdir();
		
		return new SimflofyFolder(childFolder.getParent(), childFolder.getName());
	}
}

