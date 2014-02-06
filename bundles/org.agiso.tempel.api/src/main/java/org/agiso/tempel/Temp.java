/* org.agiso.tempel.Temp (14-09-2012)
 * 
 * Temp.java
 * 
 * Copyright 2012 agiso.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.agiso.tempel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Klasa tymczasowa (do usunięcia). Zawiera statyczne metody narzędziowe do
 * przeniesienia do właściwych dla nich bibliotek i klas.
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
@Deprecated
public class Temp {
//	--------------------------------------------------------------------------
//	StringUtils
//	--------------------------------------------------------------------------
	/**
	 * @param string
	 * @return
	 */
	public static final boolean StringUtils_isEmpty(String string) {
		return string == null || string.length() == 0;
	}

	/**
	 * @param string
	 * @return
	 */
	public static final boolean StringUtils_isBlank(String string) {
		return string == null || string.trim().length() == 0;
	}

	/**
	 * @param string
	 * @return
	 */
	public static final String StringUtils_nullIfEmpty(String string) {
		return StringUtils_isEmpty(string)? null : string;
	}

	/**
	 * @param string
	 * @return
	 */
	public static final String StringUtils_nullIfBlank(String string) {
		return StringUtils_isBlank(string)? null : string;
	}

	/**
	 * @param string
	 * @return
	 */
	public static final String StringUtils_emptyIfNull(String string) {
		return string == null? "" : string;
	}

	/**
	 * @param string
	 * @return
	 */
	public static final String StringUtils_emptyIfBlank(String string) {
		return StringUtils_isBlank(string)? "" : string;
	}

//	--------------------------------------------------------------------------
//	FileUtils
//	--------------------------------------------------------------------------
	/**
	 * @param srcFile
	 * @param dstFile
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static final void FileUtils_copyFile(String srcFile, String dstFile) throws IOException {
		FileUtils_copyFile(new File(srcFile), new File(dstFile));
	}

	/**
	 * @param srcFile
	 * @param dstFile
	 * @throws IOException
	 */
	public static final void FileUtils_copyFile(File srcFile, File dstFile) throws IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(srcFile);

			FileUtils_copyFile(is, dstFile);
		} finally {
			if(is != null) {
				try {
					is.close();
				} catch(Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	/**
	 * @param is
	 * @param dstFile
	 * @throws IOException
	 */
	public static final void FileUtils_copyFile(InputStream is, File dstFile) throws IOException {
		OutputStream os = null;
		try {
			os = new FileOutputStream(dstFile);

			int len;
			byte[] buf = new byte[1024];
			while((len = is.read(buf)) > 0) {
				os.write(buf, 0, len);
			}
		} finally {
			if(os != null) {
				try {
					os.flush();
					os.close();
				} catch(Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public static final void FileUtils_copyDir(String source, String dest) {
		File srcFolder = new File(source);
		File destFolder = new File(dest);
		//make sure source exists
		if(!srcFolder.exists()){
			System.out.println("Directory does not exist: " + srcFolder);
			System.exit(0);
		}else{
			try{
				FileUtils_copyFolder(srcFolder,destFolder);
			}catch(IOException e){
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	public static final void FileUtils_copyFolder(File src, File dest) throws IOException{
		if(src.isDirectory()){
			//if directory not exists, create it
			if(!dest.exists()){
				dest.mkdir();
			}
			//list all the directory contents
			String files[] = src.list();
			for (String file : files) {
				//construct the src and dest file structure
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				//recursive copy
				FileUtils_copyFolder(srcFile,destFile);
			}
		}else{
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest); 
			byte[] buffer = new byte[1024];
			int length;
			//copy the file content in bytes 
			while ((length = in.read(buffer)) > 0){
				out.write(buffer, 0, length);
			}
			in.close();
			out.close();
		}
	}

//	--------------------------------------------------------------------------
//	DigestUtils
//	--------------------------------------------------------------------------
	/**
	 * @param algorithm
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static final String DigestUtils_countDigest(String algorithm, InputStream is) throws Exception {
		MessageDigest digest = MessageDigest.getInstance(algorithm);

		DigestUtils_updateDigest(digest, is);

		return HexUtils_toHexString(digest.digest());
	}

	/**
	 * @param md
	 * @param is
	 * @throws Exception
	 */
	public static final void DigestUtils_updateDigest(MessageDigest md, InputStream is) throws Exception {
		DigestInputStream dis = new DigestInputStream(new BufferedInputStream(is), md);

		while(dis.read() != -1);
	}

	/**
	 * @param algorithm
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static final String DigestUtils_countDigest(String algorithm, File file) throws Exception {
		if(!file.exists()) {
			throw new FileNotFoundException(file.getPath());
		}

		MessageDigest digest = MessageDigest.getInstance(algorithm);

		String basePath = file.getCanonicalPath();
		if(file.isDirectory()) {
			File[] content = file.listFiles();
			Arrays.sort(content, new Comparator<File>() {
				@Override
				public int compare(File f1, File f2) {
					return f1.getName().compareTo(f2.getName());
				}
			});

			for(File subFile : content) {
				DigestUtils_updateDirecotryDigest(basePath, subFile, digest);
			}
		} else {
			DigestUtils_updateDigest(digest, new FileInputStream(file));
		}

		return HexUtils_toHexString(digest.digest());
	}

//	--------------------------------------------------------------------------
	private static void DigestUtils_updateDirecotryDigest(String path, File file, MessageDigest md) throws Exception {
		String relativePath = file.getCanonicalPath().substring(path.length());
		relativePath = relativePath.replace('\\', '/');		// normalizacja ścieżki
		if(file.isDirectory()) {
			md.update(relativePath.getBytes(Charset.forName("UTF8")));

			File[] content = file.listFiles();
			Arrays.sort(content, new Comparator<File>() {
				@Override
				public int compare(File f1, File f2) {
					return f1.getName().compareTo(f2.getName());
				}
			});

			for(File subFile : content) {
				DigestUtils_updateDirecotryDigest(path, subFile, md);
			}
		} else {
			md.update(relativePath.getBytes(Charset.forName("UTF8")));

			DigestUtils_updateDigest(md, new FileInputStream(file));
		}
	}

//	--------------------------------------------------------------------------
//	HextUtils
//	--------------------------------------------------------------------------
	/**
	 * @param array
	 * @return
	 */
	public static final String HexUtils_toHexString(byte[] array) {
		int length = array.length;
		StringBuilder hexString = new StringBuilder();
		for(int i = 0; i < length; i++) {
			hexString.append(HexUtils_hexDigit(array[i]));
		}

		return hexString.toString();
	}

	/**
	 * @param b
	 * @return
	 */
	public static final String HexUtils_hexDigit(byte b) {
		StringBuilder sb = new StringBuilder();
		char c;
		// First nibble
		c = (char)((b >> 4) & 0xf);
		if(c > 9) {
			c = (char)((c - 10) + 'a');
		} else {
			c = (char)(c + '0');
		}
		sb.append(c);
		// Second nibble
		c = (char)(b & 0xf);
		if(c > 9) {
			c = (char)((c - 10) + 'a');
		} else {
			c = (char)(c + '0');
		}
		sb.append(c);
		return sb.toString();
	}

//	--------------------------------------------------------------------------
//	ConvertUtils
//	--------------------------------------------------------------------------
	public static final String ConvertUtils_convertStreamToString(InputStream is) {
		Scanner s = new Scanner(is, "UTF-8").useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

//	--------------------------------------------------------------------------
//	AnsiUtils
//	--------------------------------------------------------------------------
	public static class AnsiUtils {
		private static IAnsiProcessor processor = new DefaultAnsiProcessor();
		private static IAnsiElementWrapper wrapper = new AnsiElementWrapper();

		private AnsiUtils() {
		}

		public static void setAnsiProcessor(IAnsiProcessor processor) {
			AnsiUtils.processor = processor;
		}
		public static IAnsiElementWrapper setAnsiProcessor(IWrappingAnsiProcessor processor) {
			AnsiUtils.processor = processor;
			return wrapper;
		}


		public static String ansiString(Object... elements) {
			if(processor instanceof IWrappingAnsiProcessor) {
				elements = Arrays.copyOf(elements, elements.length);
				for(int index = 0; index < elements.length; index++) {
					if(elements[index] instanceof WrappingAnsiElement) {
						elements[index] = ((WrappingAnsiElement)elements[index]).getValue();
					}
				}
			}
			return processor.ansiString(elements);
		}

		public interface IAnsiProcessor {
			public String ansiString(Object... elements);
		}
		public interface IWrappingAnsiProcessor extends IAnsiProcessor {
		}
		private static class DefaultAnsiProcessor implements IAnsiProcessor {
			@Override
			public String ansiString(Object... elements) {
				StringBuilder sb = new StringBuilder();
				for(Object element : elements) {
					if(element instanceof IAnsiElement) {
						sb.append(((IAnsiElement)element).getValue());
					} else {
						sb.append(element);
					}
				}
				return sb.toString();
			}
		}

		public static class AnsiElement {
			public static final IAnsiElement NORMAL = new WrappingAnsiElement("");
			public static final IAnsiElement BOLD = new WrappingAnsiElement("");
			public static final IAnsiElement FAINT = new WrappingAnsiElement("");
			public static final IAnsiElement ITALIC = new WrappingAnsiElement("");
			public static final IAnsiElement UNDERLINE = new WrappingAnsiElement("");

			public static final IAnsiElement BLACK = new WrappingAnsiElement("");
			public static final IAnsiElement RED = new WrappingAnsiElement("");
			public static final IAnsiElement GREEN = new WrappingAnsiElement("");
			public static final IAnsiElement YELLOW = new WrappingAnsiElement("");
			public static final IAnsiElement BLUE = new WrappingAnsiElement("");
			public static final IAnsiElement MAGENTA = new WrappingAnsiElement("");
			public static final IAnsiElement CYAN = new WrappingAnsiElement("");
			public static final IAnsiElement WHITE = new WrappingAnsiElement("");
			public static final IAnsiElement DEFAULT = new WrappingAnsiElement("");

		}
		private interface IAnsiElement {
			public Object getValue();
		}
		private static class WrappingAnsiElement implements IAnsiElement {
			private Object value;

			public WrappingAnsiElement(Object value) {
				this.value = value;
			}

			public Object getValue() {
				return value;
			}
			public void setValue(Object value) {
				this.value = value;
			}
		}

		public interface IAnsiElementWrapper {
			public IAnsiElementWrapper withAnsiNormal(Object value);
			public IAnsiElementWrapper withAnsiBold(Object value);
			public IAnsiElementWrapper withAnsiFaint(Object value);
			public IAnsiElementWrapper withAnsiItalic(Object value);
			public IAnsiElementWrapper withAnsiUnderline(Object value);
			public IAnsiElementWrapper withAnsiBlack(Object value);
			public IAnsiElementWrapper withAnsiRed(Object value);
			public IAnsiElementWrapper withAnsiGreen(Object value);
			public IAnsiElementWrapper withAnsiYellow(Object value);
			public IAnsiElementWrapper withAnsiBlue(Object value);
			public IAnsiElementWrapper withAnsiMagenta(Object value);
			public IAnsiElementWrapper withAnsiCyan(Object value);
			public IAnsiElementWrapper withAnsiWhite(Object value);
			public IAnsiElementWrapper withAnsiDefault(Object value);
		}
		private static class AnsiElementWrapper implements IAnsiElementWrapper {
			@Override
			public IAnsiElementWrapper withAnsiNormal(Object value) {
				((WrappingAnsiElement)AnsiElement.NORMAL).setValue(value);
				return this;
			}
			@Override
			public IAnsiElementWrapper withAnsiBold(Object value) {
				((WrappingAnsiElement)AnsiElement.BOLD).setValue(value);
				return this;
			}
			@Override
			public IAnsiElementWrapper withAnsiFaint(Object value) {
				((WrappingAnsiElement)AnsiElement.FAINT).setValue(value);
				return this;
			}
			@Override
			public IAnsiElementWrapper withAnsiItalic(Object value) {
				((WrappingAnsiElement)AnsiElement.ITALIC).setValue(value);
				return this;
			}
			@Override
			public IAnsiElementWrapper withAnsiUnderline(Object value) {
				((WrappingAnsiElement)AnsiElement.UNDERLINE).setValue(value);
				return this;
			}
			@Override
			public IAnsiElementWrapper withAnsiBlack(Object value) {
				((WrappingAnsiElement)AnsiElement.BLACK).setValue(value);
				return this;
			}
			@Override
			public IAnsiElementWrapper withAnsiRed(Object value) {
				((WrappingAnsiElement)AnsiElement.RED).setValue(value);
				return this;
			}
			@Override
			public IAnsiElementWrapper withAnsiGreen(Object value) {
				((WrappingAnsiElement)AnsiElement.GREEN).setValue(value);
				return this;
			}
			@Override
			public IAnsiElementWrapper withAnsiYellow(Object value) {
				((WrappingAnsiElement)AnsiElement.YELLOW).setValue(value);
				return this;
			}
			@Override
			public IAnsiElementWrapper withAnsiBlue(Object value) {
				((WrappingAnsiElement)AnsiElement.BLUE).setValue(value);
				return this;
			}
			@Override
			public IAnsiElementWrapper withAnsiMagenta(Object value) {
				((WrappingAnsiElement)AnsiElement.MAGENTA).setValue(value);
				return this;
			}
			@Override
			public IAnsiElementWrapper withAnsiCyan(Object value) {
				((WrappingAnsiElement)AnsiElement.CYAN).setValue(value);
				return this;
			}
			@Override
			public IAnsiElementWrapper withAnsiWhite(Object value) {
				((WrappingAnsiElement)AnsiElement.WHITE).setValue(value);
				return this;
			}
			@Override
			public IAnsiElementWrapper withAnsiDefault(Object value) {
				((WrappingAnsiElement)AnsiElement.DEFAULT).setValue(value);
				return this;
			}
		}
	}
}
