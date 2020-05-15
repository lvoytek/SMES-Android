package com.example.smes_data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SecureData implements Serializable
{
	protected String name;
	protected String fileName;
	protected File dir;
	protected String encryptionKey;



	public SecureData(String name, String fileName, File externalFilesDirectory)
	{
		this.name = name;
		this.fileName = fileName;
		this.dir = externalFilesDirectory;
	}

	public void writeStringData(String data) throws IOException
	{
		FileWriter writer = new FileWriter(new File(dir, fileName));
		writer.write(data);
		writer.close();
	}

	public void writeData(Serializable serObj) throws IOException
	{
		ObjectOutputStream oStream = new ObjectOutputStream(new FileOutputStream(new File(dir, fileName)));
		oStream.writeObject(serObj);
		oStream.close();
	}

	public Serializable readObjectData() throws IOException, ClassNotFoundException
	{
		ObjectInputStream iStream = new ObjectInputStream(new FileInputStream(new File(dir, fileName)));
		Serializable obj = (Serializable) iStream.readObject();
		iStream.close();
		return obj;
	}

	public String readStringData() throws IOException
	{
		FileReader reader = new FileReader(new File(dir, fileName));
		char[] readData = new char[500];
		reader.read(readData);
		return readData.toString();
	}

	public String toString()
	{
		return this.name;
	}

	public String getFileName() { return this.fileName; }

	public void setName(String name)
	{
		this.name = name;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
}
