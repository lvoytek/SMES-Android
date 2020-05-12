package com.example.smes_data;

public class SecureData
{
	protected String name;
	protected String fileName;
	protected String encryptionKey;


	public SecureData(String name, String fileName)
	{
		this.name = name;
		this.fileName = fileName;
	}

	public void writeData(String data)
	{

	}

	public String toString()
	{
		return this.name;
	}

}
