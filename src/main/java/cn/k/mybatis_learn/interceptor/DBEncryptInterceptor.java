package cn.k.mybatis_learn.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.defaults.DefaultSqlSession.StrictMap;

import java.util.*;

/**
 * 数据库加密解密拦截器
 * 
 * @author denggq
 *
 */
@Intercepts({  
    @Signature(type=Executor.class,method="update",args={MappedStatement.class,Object.class}),  
    @Signature(type=Executor.class,method="query",args={MappedStatement.class,Object.class,RowBounds.class,ResultHandler.class})  
    })  
public class DBEncryptInterceptor implements Interceptor {

	/**
	 * 加密类型1：实例对象-字段名称
	 * key:需要加密对象及value:字段名称
	 */
	private Map<String, String> class_code_map;
	
	/**
	 * 入参加密类型2：mybatsSQLid-属性名称
	 */
	private Map<String, String> sqlid_code_map;
	/**
	 * 出参加密类型2：mybatsSQLid-属性名称
	 */
	private Map<String, String> sqlid_code_outmap;
	
	private Map<String, String> class_code_outmap;
	
	//aes_key
	private String AES_KEY="73af3a2fe4eb4737";
	
	private String IS_LIST="list";

	@Override  
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement statement = (MappedStatement) invocation.getArgs()[0];
        String methodName = invocation.getMethod().getName();  
        Object returnValue = null;
        Object parameter = invocation.getArgs()[1];  
        BoundSql sql = statement.getBoundSql(parameter);
        System.out.println(sql.getSql());
        
        String sqlid=statement.getId();
        System.out.println("sqlid is "+sqlid);
        String parametername="";
        if(parameter!=null){
	        parametername=parameter.getClass().getName();
	        //第一步参数加密
	        //1以sqlid方式查找需要加密的字段
	        if(sqlid_code_map!=null&&!sqlid_code_map.isEmpty()){
	        	String values=sqlid_code_map.get(sqlid);
	        	if(values!=null){
	        		List<String> fieldNames=Arrays.asList(values.split(",")) ;
	        		List<ParameterMapping> parameterMappings=sql.getParameterMappings();
	        		if(parameterMappings!=null&&parameterMappings.size()>0){
	        			if(IS_LIST.equals(values)){
	        				System.out.println(parameter.getClass().getName());
	        				try{
		        				StrictMap map =(StrictMap) parameter;
		        				ArrayList<String> vals=(ArrayList<String>) map.get(IS_LIST);
		        				for(int i=0;i<vals.size();i++){
		        					vals.set(i, AESUtil.encryptToBase64(vals.get(i), AES_KEY));
		        				}
	        				}catch(Exception e){
	        					Map map =(Map) parameter;
		        				ArrayList<String> vals=(ArrayList<String>) map.get(IS_LIST);
		        				for(int i=0;i<vals.size();i++){
		        					vals.set(i, AESUtil.encryptToBase64(vals.get(i), AES_KEY));
		        				}
	        				}
	        				
	        		//		map.put(IS_LIST, vals);
	        			}else{
		        			for(ParameterMapping mapping:parameterMappings){
		        				if(fieldNames.contains(mapping.getProperty())){
									if(StringUtils.isNotEmpty(parameter.toString())){
										if(parameter instanceof Map){
											Map map =(Map) parameter;
											map.put(mapping.getProperty(), AESUtil.encryptToBase64(map.get(mapping.getProperty()).toString(), AES_KEY));
										}else {
											invocation.getArgs()[1]=AESUtil.encryptToBase64(parameter.toString(), AES_KEY);
										}
										//invocation.getArgs()[1]=AESUtil.encryptToBase64(parameter.toString(), AES_KEY);
		        					}
		        				}
		        			}
	        			}
	        		}
	        	}
	        }
        
	        if(class_code_map!=null&&!class_code_map.isEmpty()){
	        	//2/以参数类型加密
	        	parameter= beanSetFiledValue(parametername, sqlid, parameter, methodName, false);
	        }
        }
        //执行脚本
        returnValue = invocation.proceed();
    	if(class_code_map!=null&&!class_code_map.isEmpty()){
        	//执行完脚本进行参数解密，解决参数重用后的还是加密后bug
    		parameter=beanSetFiledValue(parametername, sqlid, parameter, methodName, true);
        	//返回值解密
	        if(returnValue instanceof ArrayList<?>){
	            List<?> list = (ArrayList<?>)returnValue;
	            for(Object val:list){
	            	if(val!=null){
	            		if(sqlid_code_outmap!=null&&!sqlid_code_outmap.isEmpty()){
	            			val=setField(val,sqlid);
	            		}
	            		val=setField(val);
	            	}
//	            	String valclass=val.getClass().getName();
//	            	//以返回类型解密字段
//	            	if(class_code_map.containsKey(valclass)){
//	            		String fieldNamestr=class_code_map.get(valclass);
//	    	        	if(StringUtils.isNotEmpty(fieldNamestr)){
//	    	        		String []fieldNames=fieldNamestr.split(",");
//			            	for(String fieldName:fieldNames){
//			            		String original=(String) BeanUtil.getFieldValue(val, fieldName);
//			            		if(original!=null){
//			            			String decrypt=AESUtil.decryptFromBase64(original, AES_KEY);
//			            			BeanUtil.setFiledValue(val, fieldName, decrypt);
//			            		}
//			            	}
//	    	        	}
//	    	        }
//	            	
	            }
	        }
	        
    	}
        return returnValue;
    }
	
	private Object setField (Object val,String sqlid){
	if(sqlid_code_outmap!=null&&!sqlid_code_outmap.isEmpty()){
    	String values=sqlid_code_outmap.get(sqlid);
    	if(values!=null){
    		List<String> fieldNames=Arrays.asList(values.split(",")) ;
        	for(String fieldName:fieldNames){
        		Object val2=BeanUtil.getFieldValue(val, fieldName);
        		if(val2!=null){
        		String val2class=val2.getClass().getName();
        		if(class_code_map.containsKey(val2class)||(class_code_outmap!=null&&class_code_outmap.containsKey(val2class))){
        			val2=setField(val2);
        			BeanUtil.setFiledValue(val, fieldName, val2);
        		}else{
            		String original=(String) BeanUtil.getFieldValue(val, fieldName);
            		System.out.println("sqlid："+sqlid+"获取fieldName:"+fieldName+",值original:"+original);
            		if(StringUtils.isNotEmpty(original)){
            			try{
	            			String decrypt=AESUtil.decryptFromBase64(original, AES_KEY);
	            			BeanUtil.setFiledValue(val, fieldName, decrypt);
            			}catch (Exception e) {
							System.out.println(e);
						}
            		}
        		}
        	}
    	}
	}
	}
	return val;
	}
	private Object setField (Object val){
		String valclass=val.getClass().getName();
		if(class_code_map.containsKey(valclass)||(class_code_outmap!=null&&class_code_outmap.containsKey(valclass))){
    		String fieldNamestr=class_code_map.get(valclass);
    		if(class_code_outmap!=null&&StringUtils.isEmpty(fieldNamestr)){
    			fieldNamestr=class_code_outmap.get(valclass);
    		}
        	if(StringUtils.isNotEmpty(fieldNamestr)){
        		String []fieldNames=fieldNamestr.split(",");
            	for(String fieldName:fieldNames){
            		Object val2=BeanUtil.getFieldValue(val, fieldName);
            		if(val2!=null){
            		String val2class=val2.getClass().getName();
            		if(class_code_map.containsKey(val2class)||(class_code_outmap!=null&&class_code_outmap.containsKey(val2class))){
            			val2=setField(val2);
            			BeanUtil.setFiledValue(val, fieldName, val2);
            		}else{
	            		String original=(String) BeanUtil.getFieldValue(val, fieldName);
	            		System.out.println("valclass："+valclass+"获取fieldName:"+fieldName+",值original:"+original);
	            		if(StringUtils.isNotEmpty(original)){
	            			try{
		            			String decrypt=AESUtil.decryptFromBase64(original, AES_KEY);
		            			BeanUtil.setFiledValue(val, fieldName, decrypt);
	            			}catch (Exception e) {
								System.out.println(e);
							}
	            		}
            		}
            	}
            	}
        	}
        }
		
		return val;
	}
    
    @Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub

	}

	public Map<String, String> getClass_code_map() {
		return class_code_map;
	}

	public void setClass_code_map(Map<String, String> class_code_map) {
		this.class_code_map = class_code_map;
	}
	
	public Map<String, String> getSqlid_code_map() {
		return sqlid_code_map;
	}

	public void setSqlid_code_map(Map<String, String> sqlid_code_map) {
		this.sqlid_code_map = sqlid_code_map;
	}

	public Map<String, String> getClass_code_outmap() {
		return class_code_outmap;
	}

	public void setClass_code_outmap(Map<String, String> class_code_outmap) {
		this.class_code_outmap = class_code_outmap;
	}

	public String getAES_KEY() {
		return AES_KEY;
	}

	public void setAES_KEY(String aES_KEY) {
		AES_KEY = aES_KEY;
	}
	
	public Map<String, String> getSqlid_code_outmap() {
		return sqlid_code_outmap;
	}

	public void setSqlid_code_outmap(Map<String, String> sqlid_code_outmap) {
		this.sqlid_code_outmap = sqlid_code_outmap;
	}

	private Object sqlidSetFiledValue(BoundSql sql ,String sqlid,Object parameter,boolean isdec){
		if(sqlid_code_map!=null&&!sqlid_code_map.isEmpty()){
        	String values=sqlid_code_map.get(sqlid);
        	if(values!=null){
        		List<String> fieldNames=Arrays.asList(values.split(",")) ;
        		List<ParameterMapping> parameterMappings=sql.getParameterMappings();
        		if(parameterMappings!=null&&parameterMappings.size()>0){
        			for(ParameterMapping mapping:parameterMappings){
        				if(fieldNames.contains(mapping.getProperty())){
        					if(isdec)
        						parameter=AESUtil.decryptFromBase64(parameter.toString(), AES_KEY);
        					else
        						parameter=AESUtil.encryptToBase64(parameter.toString(), AES_KEY);
        				}
        			}
        		}
        	}
        }
		return parameter;
	}
	
	/**
	 * bean字段加密解密
	 * @param parametername
	 * @param sqlid
	 * @param parameter
	 * @param methodName
	 * @param isdec
	 */
	private Object beanSetFiledValue(String parametername ,String sqlid,Object parameter,String methodName,boolean isdec){
		if(class_code_map.containsKey(parametername)&& !sqlid.contains("!selectKey")){
        	String fieldNamestr=class_code_map.get(parametername);
        	if(StringUtils.isNotEmpty(fieldNamestr)){
	        	String []fieldNames=fieldNamestr.split(",");
	        	System.out.println(methodName);
	       //     if(methodName.equals("query")){
	            	
	        //    }else if(methodName.equals("update")){
	            	for(String fieldName:fieldNames){
	            		Object val2=BeanUtil.getFieldValue(parameter, fieldName);
	            		if(val2!=null){
	                		String val2class=val2.getClass().getName();
	                		if(class_code_map.containsKey(val2class)||(class_code_outmap!=null&&class_code_outmap.containsKey(val2class))){
	                			val2=beanSetFiledValue(val2class,sqlid,val2,methodName,isdec);
	                			BeanUtil.setFiledValue(parameter, fieldName, val2);
	                		}else{
			            		String original=(String) BeanUtil.getFieldValue(parameter, fieldName);
			            		System.out.println("字段名称："+parametername+"-"+fieldName+"，原值："+original);
			            		if(StringUtils.isNotEmpty(original)){
			            			String encrypt="";
			            			if(isdec)
			            				encrypt=AESUtil.decryptFromBase64(original, AES_KEY);
			            			else
			            				encrypt=AESUtil.encryptToBase64(original, AES_KEY);
			            			System.out.println("解密后值："+encrypt);
			            			BeanUtil.setFiledValue(parameter, fieldName, encrypt);
			            		}
	                		}
	            		}
	            	}
        		}
		}
		return parameter;
	}

	public static void main(String[] args) {
		String encrypt=AESUtil.encryptToBase64("888888881283747223", "73af3a2fe4eb4737");
		String decrypt=AESUtil.decryptFromBase64("xfmBVx0tJZR+mo0WkP3pFw==", "73af3a2fe4eb4737");
		System.out.println(encrypt+"***"+decrypt);
	}
}