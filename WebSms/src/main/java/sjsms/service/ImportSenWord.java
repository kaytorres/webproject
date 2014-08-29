package sjsms.service;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import java_cup.runtime.lr_parser;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import sjsms.DBservice.SensitiveWordManager;
import sjsms.DBservice.SyslogManager;


public class ImportSenWord extends MultiActionController{
	private SensitiveWordManager sensitiveWordManager;
	public void setSensitiveWordManager(SensitiveWordManager sensitiveWordManager){
		this.sensitiveWordManager=sensitiveWordManager;
	}
	private SyslogManager syslogManager;
	public void setSyslogManager(SyslogManager syslogManager) {
		this.syslogManager = syslogManager;
	}
    public ModelAndView importTxt(HttpServletRequest request,     
            HttpServletResponse response) throws Exception {    
        // 转型为MultipartHttpRequest：     
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;     
        // 获得文件：     
        MultipartFile file = multipartRequest.getFile("file"); 
        File test=new File("/trance.txt");
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream("/trance.txt"));
                stream.write(bytes);
                stream.close();
            } catch (Exception e) {
            }
        }
        if(test.isFile() && test.exists()){ //判断文件是否存在
            InputStreamReader read = new InputStreamReader(
            new FileInputStream(test));//考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            CreateSyslog createSyslog=new CreateSyslog();
			createSyslog.setSyslogManager(syslogManager);
            while((lineTxt = bufferedReader.readLine()) != null){
            	if(!sensitiveWordManager.senWordExist(lineTxt)){
            		sensitiveWordManager.addSensitiveWord(lineTxt);
					createSyslog.createSyslog("01", "05", "增加："+lineTxt);  
            	}
            }
            read.close(); 
            } 
        return new ModelAndView("showsenword", "model",null);
     }
    
   
}

   /* @RequestMapping(value="/upload", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }
 
    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("name") String name,
            @RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(name + "-uploaded")));
                stream.write(bytes);
                stream.close();
                return "You successfully uploaded " + name + " into " + name + "-uploaded !";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }*/
 
//}

//public class ImportSenWord extends MultiActionController{
/*	private MultipartFile file;
	public MultipartFile getFile() {
		return file;
	}


	public void setFile(MultipartFile file) {
		this.file = file;
	}*/

//  @RequestParam("file") MultipartFile file
	//public ModelAndView importTxt(HttpServletRequest request,
	//		HttpServletResponse response) throws Exception {
//		InputStream s=null;
//		try {
//			s = new FileInputStream(file);
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//		}
//		// 关闭流
//		try {
//			s.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		/* String path = request.getSession().getServletContext().getRealPath("upload");  
	     String fileName = file.getOriginalFilename();   
	     File targetFile = new File(path, fileName); 
	     ReResult reResult=new ReResult();
	     if(targetFile.exists()){
	    	 reResult.setReturncode(targetFile.toString());
	     }*/
//		return new ModelAndView("showMT", "model", "reResult");
//	}

/*	
	    @RequestMapping(value = "/import.do")  
	    public String upload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, ModelMap model) {  
	  
	        System.out.println("开始");  
	        String path = request.getSession().getServletContext().getRealPath("import");  
	        String fileName = file.getOriginalFilename();  
//	        String fileName = new Date().getTime()+".jpg";  
	        System.out.println(path);  
	        File targetFile = new File(path, fileName);  
	        if(!targetFile.exists()){  
	            targetFile.mkdirs();  
	        }  
	  
	        //保存  
	        try {  
	            file.transferTo(targetFile);  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        model.addAttribute("fileUrl", request.getContextPath()+"/import/"+fileName);  
	        return "result";  
	    } */ 

//}
