package com.ethink.face.utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/* @类描述:查找文件
 * @date: 2017年11月6日
 * @author: dingfan
 *
 */
public class FileSearch {
	  	  	  
    /**   
     * 递归查找文件   
     * @param baseDirName  查找的文件夹路径   
     * @param targetFileName  需要查找的文件名   
     * @param fileList  查找到的文件集合   
     */ 		
	 public static void findFiles(String baseDirName, String targetFileName,  List<String> fileList,boolean flag,String atm) {     
		
        File baseDir = new File(baseDirName);       // 创建一个File对象  
        if (!baseDir.exists() || !baseDir.isDirectory()) {  // 判断目录是否存在  
            System.out.println("文件查找失败：" + baseDirName + "不是一个目录！");  
        }  
        String tempName = null;     
        //判断目录是否存在     
        File tempFiles;  
        File[] files = baseDir.listFiles();  
        for (int i = 0; i < files.length; i++) {  
            tempFiles = files[i]; 
          
           if (flag) {
        	   if(tempFiles.isDirectory()){  
                   findFiles(tempFiles.getAbsolutePath(), targetFileName, fileList,true,atm);  
               }else if(tempFiles.isFile()){  
                   tempName = tempFiles.getName();  
                   if(wildcardMatch(targetFileName, tempName)){  
                       // 匹配成功，将文件名添加到结果集  
                       fileList.add(tempFiles.getAbsoluteFile().toString());  
                   }  
               } 
		   } 
           else {  
        	  
                tempName = tempFiles.getName();  
                if(wildcardMatch(targetFileName, tempName)){  
                // 匹配成功，将文件名添加到结果集  
                if("".equals(atm))
                fileList.add(tempFiles.getAbsoluteFile().toString());  
                   }  
                else {
                	
                	if (!tempFiles.getAbsoluteFile().toString().contains(atm)) {			
                		fileList.add(tempFiles.getAbsoluteFile().toString()); 
					}
                }
           }                 
        }  
    }     
	         
	/**   
	 * 通配符匹配   
	 * @param pattern    通配符模式   
	 * @param str    待匹配的字符串   
	 * @return    匹配成功则返回true，否则返回false   
	 */    
	private static boolean wildcardMatch(String pattern, String str) {     
	    int patternLength = pattern.length();     
	    int strLength = str.length();     
	    int strIndex = 0;     
	    char ch;     
	    for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {     
	        ch = pattern.charAt(patternIndex);     
	        if (ch == '*') {     
	            //通配符星号*表示可以匹配任意多个字符     
	            while (strIndex < strLength) {     
	                if (wildcardMatch(pattern.substring(patternIndex + 1),     
	                        str.substring(strIndex))) {     
	                    return true;     
	                }     
	                strIndex++;     
	            }     
	        } else if (ch == '?') {     
	            //通配符问号?表示匹配任意一个字符     
	            strIndex++;     
	            if (strIndex > strLength) {     
	                //表示str中已经没有字符匹配?了。     
	                return false;     
	            }     
	        } else {     
	            if ((strIndex >= strLength) || (ch != str.charAt(strIndex))) {     
	                return false;     
	            }     
	            strIndex++;     
	        }     
	    }     
	    return (strIndex == strLength);     
	}   
	
	
		 /**  
		  * 读取目录文件  
		  * @param dirname 目录名称  
		  * @return list集合  
		  */  
		 public  List<String> getFiles(String dirname)throws Exception{  
		    List<String> file_names=new ArrayList<String>();  
		    File dir=new File(dirname);  
		    if(dir.exists()){  
		    
		     File []files=dir.listFiles();       
		     //排序  
		     Arrays.sort(files, new FileSearch.CompratorByLastModified());     
		     for(int i=0;i<files.length;i++){  
		        //获取当文件最后修改时间  		   
		        file_names.add(files[i].getName());  		    
		     }  
		    }else{  
		     System.out.println("该目录没有任何文件信息！");      
		    }     
		    return file_names;  
		 } 
		 
		 
		 /**  
		  * 格式化时间  
		  * @param format 格式化显示样式  
		  * @param date 时间  
		  * @return  
		  */  
		 private  String format(String format, Date date) {  
		  SimpleDateFormat dateFormat = new SimpleDateFormat(format);  
		  return dateFormat.format(date);  
		  }
		 
		 
		 /**  
		  * 进行文件排序时间  
		  * @author  
		  */  
		 private  class CompratorByLastModified implements Comparator<File>{     
		  public int compare(File f1, File f2) {     
		   long diff = f1.lastModified()-f2.lastModified();     
		       if(diff<0)     
		         return 1;     
		       else if(diff==0)     
		         return 0;     
		       else    
		         return -1;     
		       }     
		  public boolean equals(Object obj){     
		   return true;     
		  }     
		   }  
	
	
		 
		 /**  
		  * 文件夹创建
		  * @author  
		  */  
		 public  boolean mkDirectory(String path) {  
		       File file = null;  
		       try {  
		            file = new File(path);  
		            if (!file.exists()) {  
		                return file.mkdirs();  
		            }  		           
		        } catch (Exception e) {
		        	return false;  
		        } finally {  
		            file = null;  		           
		        }  
		        return true;  
		    }  
		    	
			  /*		  
			  * 文件指定目录复制
			  * @author  
			  */  
		    public  boolean copy(File file, File toFile) throws Exception { 
    		    byte[] b = new byte[1024]; 
    		    int a; 
    		    FileInputStream fis = null; 
    		    FileOutputStream fos = null; 
    		    try {
					if (file.isDirectory()) { 
						  String filepath = file.getAbsolutePath(); 
						  filepath=filepath.replaceAll("\\\\", "/"); 
						  String toFilepath = toFile.getAbsolutePath(); 
						  toFilepath=toFilepath.replaceAll("\\\\", "/"); 
						  int lastIndexOf = filepath.lastIndexOf("/"); 
						  toFilepath = toFilepath + filepath.substring(lastIndexOf ,filepath.length()); 
						  File copy=new File(toFilepath); 
						  //复制文件夹 
						  if (!copy.exists()) { 
						    copy.mkdir(); 
						  } 
						  //遍历文件夹 
						  for (File f : file.listFiles()) { 
						    copy(f, copy); 
						  } 
					 } else { 
						  if (toFile.isDirectory()) { 
							    String filepath = file.getAbsolutePath(); 
							    filepath=filepath.replaceAll("\\\\", "/"); 
							    String toFilepath = toFile.getAbsolutePath(); 
							    toFilepath=toFilepath.replaceAll("\\\\", "/"); 
							    int lastIndexOf = filepath.lastIndexOf("/"); 
							    toFilepath = toFilepath + filepath.substring(lastIndexOf ,filepath.length()); 
							      
							    //写文件 
							    File newFile = new File(toFilepath); 
							    fis = new FileInputStream(file); 
							    fos = new FileOutputStream(newFile); 
							    while ((a = fis.read(b)) != -1) { 
							      fos.write(b, 0, a); 
						        } 
					       } else { 
							    //写文件 
							    fis = new FileInputStream(file); 
							    fos = new FileOutputStream(toFile); 
							    while ((a = fis.read(b)) != -1) { 
							    fos.write(b, 0, a); 
					            } 
					       } 
  
					}
				} catch (Exception e) {
					return false; 
				}
    		    finally {  
		    	    fis.close();
				    fos.close(); 
    		  } 
    		    return true; 
		    }
		    
		    
		    

			 /**  
			  * 更改文件名
			  * @param path 文件路径 
			  *   
			  * @return  
			  */  
		    public boolean changeNane(String path ){		          
				List<String> list = new ArrayList<String>();		
				FileSearch.findFiles(path, "*3.jpg",list,true,""); 				
				try {
					for (int i = 0; i < list.size(); i++) {						
						File file=new File(list.get(i)); //指定文件名及路径
						String newName=list.get(i).substring(0, list.get(i).lastIndexOf("\\")+1)+"base_"+list.get(i)
						               .substring(list.get(i).lastIndexOf("\\")+1, list.get(i).lastIndexOf("."))+".jpg";					   
						File newFile = new File(newName);  
						
						file.renameTo(newFile); //改名
					 }
				} catch (Exception e) {
					
					return false;
				}
				
				return true;
				
			}
}


