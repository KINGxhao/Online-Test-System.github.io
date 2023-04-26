package com.boot.exam.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class FileUtil {
	
	/**
	 * 创建文件夹
	 * @param path
	 */
	public static void mkdirs(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	
	/**
	 * 创建文件夹/文件
	 * @param path
	 */
	public static void createFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			int idx = path.lastIndexOf(".");
			if (idx == -1) {
				mkdirs(path);
			} else {
				idx = path.lastIndexOf("/");
				if (idx == -1) {
					idx = path.lastIndexOf("\\");
				}
				mkdirs(path.substring(0, idx));
				createNewFile(path);
			}
		}
	}
	
	/**
	 * 创建文件
	 * @param path
	 */
	public static void createNewFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 删除文件夹/文件
	 * @param path
	 */
	public static void delDir(String path) {
		delDir(new File(path));
	}
	public static void delDir(File file) {
		if (file.isDirectory()) {
			File[] fs = file.listFiles();
			if (fs != null) {
				for (File f : fs) {
					delDir(f);
				}
			}
		}
		file.delete();
	}
	
	/**
	 * 获取文件的容量
	 * @param file
	 * @return
	 */
	public static long getDirLength(File file) {
		if (file.isDirectory()) {
			long size = 0;
			File[] fs = file.listFiles();
			if (fs != null) {
				for (File f : fs) {
					size += getDirLength(f);
				}
			}
			return size;
		} else {
			return file.length();
		}
	}
	public static long getDirLength(String path) {
		return getDirLength(new File(path));
	}
	
	/**
	 * 复制文件
	 * @param src
	 * @param target
	 */
	public static void copy(String src, String target) {
		copy(new File(src), target);
	}
	public static void copy(File srcFile, String target) {
		createFile(target);
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(srcFile);
			fos = new FileOutputStream(target);
			byte[] bytes = new byte[1024];
			int len = -1;
			while ((len = fis.read(bytes)) != -1) {
				fos.write(bytes, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 复制文件夹
	 * @param src
	 * @param target
	 */
	public static void copyFile(String src, String target) {
		if (src.contains(".") ^ target.contains(".")) {
			return;
		}
		createFile(target);
		if (!src.contains(".")) {
			File dir = new File(src);
			File[] fs = dir.listFiles();
			if (fs != null) {
				for (File f : fs) {
					copyFile(src + File.separator + f.getName(), 
							target + File.separator + f.getName());
				}
			}
		} else {
			copy(src, target);
		}
	}
	
	/**
	 * 剪切文件夹/文件
	 * @param src
	 * @param target
	 */
	public static void cutFile(String src, String target) {
		copyFile(src, target);
		delDir(src);
	}
	
	/**
	 * 文件写入...
	 * @param path
	 * @param text
	 */
	public static void write(String path, String text) {
		write(path, text, false);
	}
	public static void write(String path, String text, boolean append) {
		File file = new File(path);
		if (!file.exists()) {
			createFile(path);
			append = false;
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file, append);
			fos.write(text.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void println(String path, Object text) {
		println(path, text, true);
	}
	public static void println(String path, Object text, boolean append) {
		createFile(path);
		PrintStream ps = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(path, append);
			ps = new PrintStream(fos);
			if (text instanceof String) {
				ps.println((String) text);
			} else if (text instanceof Exception) {
				((Exception) text).printStackTrace(ps);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				ps.close();
			}
			try {
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 从文件中读...
	 * @param path
	 * @return
	 */
	public static String read(String path) {
		String ret = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
			byte[] bytes = new byte[1024];
			StringBuilder builder = new StringBuilder();
			int len = -1;
			while ((len = fis.read(bytes)) != -1) {
				builder.append(new String(bytes, 0, len));
			}
			ret = builder.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}
	
	
	/**
	 * 压缩文件
	 * @param src
	 */
	public static void zipFile(String src) {
		String target = null;
		if (src.contains(".")) {
			target = src.substring(0, src.lastIndexOf(".")) + ".zip";
		} else {
			target = src + ".zip";
		}
		delDir(target);
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			fos = new FileOutputStream(target);
			zos = new ZipOutputStream(fos);
			zip(new File(src), "", zos);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (zos != null) {
					zos.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void zip(File file, String baseDir, ZipOutputStream zos) {
		if (file.isDirectory()) {
			File[] fs = file.listFiles();
			if (fs != null) {
				for (File f : fs) {
					zip(f, baseDir + file.getName() + File.separator, zos);
				}
			}
		} else {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				zos.putNextEntry(new ZipEntry(baseDir + file.getName()));
				byte[] bytes = new byte[1024];
				int len = -1;
				while ((len = fis.read(bytes)) != -1) {
					zos.write(bytes, 0, len);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fis != null) {
						fis.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 解压缩
	 * @param src
	 */
	public static void unzipFile(String src) {
		ZipFile zf = null;
		InputStream is = null;
		FileOutputStream fos = null;
		boolean isLeft = true;
		int idx = src.lastIndexOf("/");
		if (idx == -1) {
			idx = src.lastIndexOf("\\");
			isLeft = false;
		}
		String root = src.substring(0, idx);
		try {
			zf = new ZipFile(src);
			@SuppressWarnings("rawtypes")
			Enumeration entries = zf.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				is = zf.getInputStream(entry);
				String name = File.separator + entry.getName();
				name = isLeft ? name.replace("\\", "/") : name.replace("/", "\\");
				String path = root + name;
				createFile(path);
				fos = new FileOutputStream(path);
				byte[] bytes = new byte[1024];
				int len = -1;
				while ((len = is.read(bytes)) != -1) {
					fos.write(bytes, 0, len);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (is != null) {
					is.close();
				}
				if (zf != null) {
					zf.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
