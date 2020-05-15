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

public class SecureData
{
	protected String name;
	protected String fileName;
	protected File dir;

	protected String encryptionKey;
	protected FileWriter writer;
	protected FileReader reader;
	protected ObjectOutputStream oStream;
	protected ObjectInputStream iStream;


	public SecureData(String name, String fileName, File externalFilesDirectory)
	{
		this.name = name;
		this.fileName = fileName;
		this.dir = externalFilesDirectory;
	}

	public void writeData(String data) throws IOException
	{
		writer = new FileWriter(new File(dir, fileName));
		writer.write(data);
		writer.close();
	}

	public void writeData(Serializable serObj) throws IOException
	{
		oStream = new ObjectOutputStream(new FileOutputStream(new File(dir, fileName)));
		oStream.writeObject(serObj);
		oStream.close();
	}

	public Serializable readObjectData() throws IOException, ClassNotFoundException
	{
		iStream = new ObjectInputStream(new FileInputStream(new File(dir, fileName)));
		Serializable obj = (Serializable) iStream.readObject();
		iStream.close();
		return obj;
	}

	public String readData() throws IOException
	{
		reader = new FileReader(new File(dir, fileName));
		char[] readData = new char[500];
		reader.read(readData);
		return readData.toString();
	}

	public String toString()
	{
		return this.name;
	}

	public String getFileName() { return this.fileName; }
}
