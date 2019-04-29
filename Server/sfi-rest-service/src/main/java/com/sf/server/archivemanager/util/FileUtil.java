package com.sf.server.archivemanager.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/*
 * a file tools about file operation
 */
public class FileUtil {

	public static byte[] getFile4Bytes(String filepath) {
		byte[] buffer = null;
		try {
			FileInputStream fis = new FileInputStream(filepath);
			buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buffer;
	}

	public static InputStream getFile4InputStream(String filepath) {
		InputStream myfile = null;
		try {
			myfile = (new FileInputStream(new File(filepath)));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myfile;
	}
}
