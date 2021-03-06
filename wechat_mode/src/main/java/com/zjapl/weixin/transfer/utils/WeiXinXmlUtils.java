package com.zjapl.weixin.transfer.utils;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.zjapl.common.util.StringUtil;
import com.zjapl.weixin.transfer.bean.BaseOrderInfo;
import com.zjapl.weixin.transfer.bean.EventInfo;
import com.zjapl.weixin.transfer.bean.NewsEventInfo;
import com.zjapl.weixin.transfer.bean.NewsEventInfo.Item;
import com.zjapl.weixin.transfer.bean.QueryOrderResult;
import com.zjapl.weixin.transfer.bean.UnifiedOrderInfo;
import com.zjapl.weixin.transfer.bean.UnifiedOrderResult;



/**
 * 微信xml工具
 * @author yangb
 *
 */
public class WeiXinXmlUtils {


	static String str = "<xml><URL><![CDATA[http://itapl.com/weixin-web/public/global_event]]></URL><ToUserName><![CDATA[zjapl@zjapl.com]]></ToUserName><FromUserName><![CDATA[odbuCwiROygVr6zYfzLUGO9tFuHA]]></FromUserName><CreateTime>1492589427104</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[哈哈哈哈]]></Content><MsgId>1234567890123456</MsgId></xml>";
	
	/**
	 * xml 转 交易单结果
	 * @param xml
	 * @return
	 */
	public static QueryOrderResult xml2QueryOrderResult(String xml) {
		if(StringUtil.isEmpty(xml)){
			return null;
		}
		XStream xStream = new XStream();
		xStream.alias("xml", QueryOrderResult.class); 
		QueryOrderResult order = (QueryOrderResult) xStream.fromXML(xml);
		return order;
	}
	
    /**
	 * 下单对象转 xml
	 * @param event
	 * @return
	 */
	public static String UnifiedOrderInfo2Xml(BaseOrderInfo orderInfo) {
		if(orderInfo == null){
			return "";
		}
		xStream.alias("xml", UnifiedOrderInfo.class); 
		return xStream.toXML(orderInfo);
	}
    
	/**
	 *  xml转下单对象
	 * @param event
	 * @return
	 */
	public static UnifiedOrderResult xml2UnifiedOrderResult(String xml) {
		if(xml != null){
			XStream xStream = new XStream();
			xStream.alias("xml", UnifiedOrderResult.class); 
			
			UnifiedOrderResult result = (UnifiedOrderResult) xStream.fromXML(xml);
			return result;
		}
		return null;
	}
	
	/**
	 * xml 转对象
	 * @param xml
	 * @return
	 */
	public static EventInfo xml2EventInfo(String xml) {
		XStream xStream = new XStream();
		
		xStream.alias("xml", EventInfo.class); 
		
		
		EventInfo event = (EventInfo) xStream.fromXML(xml);
		return event;
	}
	
	/**
	 * 对象转 xml
	 * @param event
	 * @return
	 */
	public static String EventInfo2Xml(EventInfo event) {
		if(event == null){
			return "";
		}
		xStream.alias("xml", EventInfo.class); 
		xStream.alias("xml", NewsEventInfo.class); 
		xStream.alias("Articles", List.class);
		xStream.alias("item", Item.class);
		
		return xStream.toXML(event);
	}
	
	
	
	/**
	 * xml的对所有内容加 <[!CDATA[  ]]>标记.
	 */
	private static XStream xStream = new XStream(new XppDriver(new NoNameCoder()) {

         @Override
         public HierarchicalStreamWriter createWriter(Writer out) {
             return new PrettyPrintWriter(out) {
                 // 对所有xml节点的转换都增加CDATA标记
                 boolean cdata = true;

                 @Override
                 @SuppressWarnings("rawtypes")
                 public void startNode(String name, Class clazz) {
                     super.startNode(name, clazz);
                 }
                 
                 /* 
                  * 不需要将标签里面包含的 _ 转成 __ 重写此方法
                  * (non-Javadoc)
                 * @see com.thoughtworks.xstream.io.AbstractWriter#encodeNode(java.lang.String)
                 */
                @Override
                 public String encodeNode(String name) {
                     return name;
                 }
                 
                 @Override
                 protected void writeText(QuickWriter writer, String text) {
                     if (cdata) {
                         writer.write("<![CDATA[");
                         writer.write(text);
                         writer.write("]]>");
                     } else {
                         writer.write(text);
                     }
                 }
             };
         }
     });
	
	

	public static void main(String[] args) {
		NewsEventInfo eventInfo = new NewsEventInfo();
		eventInfo.setFromUserName("FROM");
		eventInfo.setToUserName("TO");
		eventInfo.setMsgType("news");
		eventInfo.setCreateTime(new Date().getTime()+"");
		
		
		List<Item> articles = new ArrayList<>();
		Item item1 = new Item();
		item1.setTitle("重大好消息");
		item1.setDescription("好消息,好消息,大后天就是周末啦");
		item1.setPicUrl("http://mmbiz.qpic.cn/mmbiz_jpg/aUNODSoHW7pQ7falpafENyPPZl4zN4xafKVK4RcqbWsgYibuN8NDotYtvxRBQziaVbLZ1TruibDVy1VibbZd3odaDg/0");
		item1.setUrl("https://wap.sogou.com");
		articles.add(item1);
		
		Item item2 = new Item();
		item2.setTitle("滴答滴答滴答");
		item2.setDescription("好消息,好消息,大后天就是周末啦");
		item2.setPicUrl("http://mmbiz.qpic.cn/mmbiz_jpg/aUNODSoHW7pQ7falpafENyPPZl4zN4xafKVK4RcqbWsgYibuN8NDotYtvxRBQziaVbLZ1TruibDVy1VibbZd3odaDg/0");
		item2.setUrl("https://wap.sogou.com");
		articles.add(item2);
		
		eventInfo.setArticleCount(articles.size());
		eventInfo.setArticles(articles);
		
		XStream xStream = new XStream();
		xStream.alias("xml", EventInfo.class); 
		xStream.alias("xml", NewsEventInfo.class);
		xStream.alias("Articles", List.class);
		xStream.alias("item", Item.class);
		
//		Object obj = event;
		
		EventInfo ev2 = eventInfo;
		
		String xml = xStream.toXML(ev2);
		
		
		
//		EventInfo object = (EventInfo) xStream.fromXML(str);
		System.out.println(xml);
	}
}
