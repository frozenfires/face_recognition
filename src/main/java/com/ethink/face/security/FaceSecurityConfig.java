/**
 * Ethink 2017 copyright
 * 
 */
package com.ethink.face.security;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * 描述:  安全功能集成
 * @author wangjing.dc@qq.com
 */
@Configuration
public class FaceSecurityConfig implements WebMvcConfigurer{	
	
	private static final Logger log = LoggerFactory.getLogger(FaceSecurityConfig.class);
	
	@Autowired
	private FaceSecurity faceSecurity;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(jwtSecurity())
			.addPathPatterns("/api/**")
			.excludePathPatterns("/api/login", "/api/logout","/api/regimg/**","/api/showImage/**","/api/getImage/**");
		
	}
	
	/**
	 * 
	 * @return
	 */
	@Bean
	public HandlerInterceptor jwtSecurity() {
		
		return new HandlerInterceptor() {

		    //拦截每个请求
		    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
		            Object handler) throws Exception {
		        response.setCharacterEncoding("utf-8");
		        String token = request.getParameter("token");
//		        log.debug("当前token=" + token);
		        
		        //token不存在
		        if(null == token || "".equals(token.trim())) {
			            responseMessage(response, response.getWriter(), ResponseData.forbidden());
			            return false;
		        }
		        else {
		        	try {
		        		boolean authed = faceSecurity.auth(token);
		        		 if(! authed){
				             responseMessage(response, response.getWriter(), ResponseData.outtime());
				        }
				        else {
				        	return true;
				        }
		        	}catch(Exception e) {
		        		e.printStackTrace();
		        	}
		        	
		        	return false;
		        }
		        
		    }

		    //请求不通过，返回错误信息给客户端
		    private void responseMessage(HttpServletResponse response, PrintWriter out, ResponseData responseData) {
		        response.setContentType("application/json; charset=utf-8");  
		        
		        ObjectMapper mapper = new ObjectMapper();
		        String json;
				try {
					json = mapper.writeValueAsString(responseData);
					 out.print(json);
				        out.flush();
				        out.close();
				} catch (JsonProcessingException e) {
					log.error(null, e);
				}		       
		    }
		};
	}
	
}
