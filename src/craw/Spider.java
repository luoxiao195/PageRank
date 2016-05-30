package craw;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
/*
 * code by 邦柳
 * 爬虫主体类
 * */
public class Spider {
	/*
	 * 用于匹配的正则
	 * */
	public static String getUrl_question="question_link.+?href=\"(.+?)\"";
	public static String getUrl_content="content.+?href=\"(.+?)\"";
	public static String getUrl_visible="visible-expanded.+?href=\"(.+?)\"";
	public static String getUrl_author="author-link.+?href=\"(.+?)\"";
	public static String getUrl_editable="zm-editable-content.+?href=\"(.+?)\"";
	//get请求
	public String SendGet_client(String url)
	{
		//System.out.println("spider(20) get info from:"+url);
		
		String ret="";
		
		try {
			HttpClient client=new HttpClient();
			HttpMethod method=new GetMethod(url);
			client.executeMethod(method);
			int code=method.getStatusCode();
			if(code==200)
				ret=method.getResponseBodyAsString();
			else
				ret="400";
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("function:SendGet_client error in class Spider");
			e.printStackTrace();
		}
		return ret;
	}
	//执行队列，并获取web中的内容
	public  ArrayList<WebEntity> getWebList(String url){
		int times=0;
		
		ArrayList<WebEntity> retList=new ArrayList<WebEntity>();
		ArrayList<String> webUrlList=new ArrayList<String>();
		//init
		SpiderQueue queue=new SpiderQueue();
		
		queue.addUnvisitedUrl(url);
		//timse次数为所要访问的页面数量
		while(!queue.unVisitedUrlsEmpty()&&times<=1000){
			
			url=(String)queue.unVisitedUrlDequeue();
			
			String content=SendGet_client(url);
			if(content.equals("400"))
				continue;
			
			queue.addVisiteUrl(url);
			
			webUrlList.addAll(getWebUrl(content,getUrl_question));
			webUrlList.addAll(getWebUrl(content,getUrl_author));
			webUrlList.addAll(getWebUrl(content,getUrl_content));
			webUrlList.addAll(getWebUrl(content,getUrl_editable));
			webUrlList.addAll(getWebUrl(content,getUrl_visible));
			WebEntity tmp=new WebEntity(url);

			for(String urlTmp:webUrlList){
				queue.addUnvisitedUrl(urlTmp);
				tmp.addOutUrl(urlTmp); 
			}
			retList.add(tmp);
			System.out.println("times is :"+times++);
			webUrlList.clear();
		}
		webUrlList.clear();
		return retList;
	}
	//获取web中的url
	public ArrayList<String> getWebUrl(String content,String reg){
		ArrayList<String> urlList=new ArrayList<String>();
		
		Pattern p=Pattern.compile(reg);
		Matcher m=p.matcher(content);
		while(m.find())
		{
			if(m.group(1).indexOf("http")>=0){
				urlList.add(m.group(1));
				//System.out.println("spider(97) get:"+m.group(1));
			}
			else{
				urlList.add("http://www.zhihu.com"+m.group(1));
				//System.out.println("spider(97) get:http://www.zhihu.com"+m.group(1));
			}	
				
		}

		return urlList;
	}
	//传入的url是否存在于retLIst中
	public int webUrlContain(ArrayList<WebEntity> retList,String url){
		int count=-1,len=retList.size();
		for(int i=0;i<len;i++){
			if(retList.get(i).getUrl().equals(url)){
				count=i;
				break;
			}
		}	
		
		return count;
		
	}
}
