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
public class Repo extends AbstractItem
{
	private static Logger log = Logger.getLogger(Repo.class);
	
	public Repo(String root, String name)
	{
		log.debug("Creating repo: " + root + " and name: " + name);
		this.setName(name);
		this.setParentPath(root);
		this.setFolder(true);
	}

	public List<AbstractItem> getChildren() throws IOException
	{
		log.debug("Getting repo children: " + this.toString());
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
}
