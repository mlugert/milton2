package com.simflofy.webdav;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author mlugert
 *
 */
public class SimflofyFile extends AbstractItem
{
	public SimflofyFile(String parentPath, String name) throws IOException
	{
		this.setName(name);
		this.setParentPath(parentPath);
		this.setFolder(false);
	}
	
	public SimflofyFile(String parentPath, String name, byte[] content) throws IOException
	{
		this.setName(name);
		this.setParentPath(parentPath);
		this.setFolder(false);

		File parent = new File(parentPath);
		if (!parent.exists())
		{
			parent.mkdirs();
		}

		File newFile = new File(parent, name);
		FileOutputStream myWriter = null;
		try
		{
			myWriter = new FileOutputStream(newFile);
			myWriter.write(content);
		} catch (IOException e)
		{
			throw e;
		} finally
		{
			if (null != myWriter)
				myWriter.close();
		}
	}

	public byte[] getBytes() throws IOException
	{
		File parent = new File(this.getParentPath());
		File file = new File(parent, this.getName());

		FileInputStream fin = null;
		try
		{
			// create FileInputStream object
			fin = new FileInputStream(file);

			byte fileContent[] = new byte[(int) file.length()];

			// Reads up to certain bytes of data from this input stream into an array of bytes.
			fin.read(fileContent);

			return fileContent;
		} catch (FileNotFoundException e)
		{
			throw e;
		} catch (IOException ioe)
		{
			throw ioe;
		} finally
		{
			if (null != fin)
			{
				fin.close();
			}
		}
	}
	
	public long getFileSize()
	{
		File parent = new File(this.getParentPath());
		File file = new File(parent, this.getName());
		
		return file.length();
	}
}