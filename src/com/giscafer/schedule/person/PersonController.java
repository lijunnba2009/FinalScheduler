package com.giscafer.schedule.person;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.giscafer.utils.DataUtils;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

public class PersonController extends Controller{
	public void index() {
		setAttr("personPage", Person.me.paginate(getParaToInt(0, 1), 10));
		render("person.html");
	}
	public void add(){
		
	}
	/**
	 * 删除
	 */
	@ActionKey("delPerson")
	public void delete(){
		boolean result=Person.me.deleteById(getParaToInt());
		renderJson(result);
	}
	/**
	 * 查询
	 * @throws IOException
	 */
	@ActionKey("/findPerson")
	public void findPerson() throws IOException{
		int rows=Integer.parseInt(getPara("rows"));
		int page=Integer.parseInt(getPara("page"));
		Page<Person> personPage=Person.me.paginate(page, rows);
		String result=DataUtils.dataTableToJson(personPage, Person.me);
		renderJson(result);
	}
	/**
	 * datagrid提交修改数据
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@ActionKey("commitPerson")
	public void commit() throws JsonParseException, JsonMappingException, IOException{
		boolean result=false;
		String insertedJson=getPara("inserted");
		String updatedJson=getPara("updated");
//		String deletedJson=getPara("deleted");
		if(insertedJson!=null && !insertedJson.equals("")){
			//字符串json数组转为json数组对象
			JSONArray jsonArray = JSONArray.fromObject(insertedJson);  
	        //json数组转List<Map>
	        List<Map<String,Object>> mapListJson = (List)jsonArray;  
	        //Map对象反序列化为Model
	        Person person=null;
	        for (int i = 0; i < mapListJson.size(); i++) {  
	            Map<String,Object> map=mapListJson.get(i);  
	            person=new Person();
	            result=person.setAttrs(map).save();
	        }  
		}
		if(updatedJson!=null && !updatedJson.equals("")){
			JSONArray updatedJsonArray = JSONArray.fromObject(updatedJson);  
	        List<Map<String,Object>> updatedListJson = (List)updatedJsonArray;  
	        Person updatePerson=null;
	        for (int i = 0; i < updatedListJson.size(); i++) {  
	            Map<String,Object> map=updatedListJson.get(i);  
	            updatePerson=new Person();
	            result=updatePerson.setAttrs(map).update();
	        }  
		}
		renderJson(result);
	}
}