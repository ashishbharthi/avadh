package com.ashish.avadh;

import java.io.IOException;
import java.util.Iterator;
import java.util.ListIterator;

import javax.servlet.http.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@SuppressWarnings("serial")
public class AvadhServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		Document doc = Jsoup.connect("http://avadhtimes.net/category/12/2.html").get();
		
		Elements submenu = doc.select("#sub_menu").select("a");
	
		Element e[] = new Element[2];
		e[0] = submenu.get(submenu.size()-1);
		e[1] = submenu.get(submenu.size()-2);
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("<html><head><title>Simple Avadh by Ashish Gauswami</title></head><body><h1>Simple Avadh ePaper </h1><p>");
		
		for (int i = 0; i < e.length; i++) {
			sb.append("<p><b>" + e[i].ownText() + "</b><ul>");
			sb.append(getDateStrings(e[i]));
			sb.append("</ul>");
		}
		
		sb.append("Created by Ashish Gauswami  </body></html>");
		
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().println(sb.toString());
	}
	
	private StringBuffer getDateStrings(Element e) throws IOException{
		StringBuffer result = new StringBuffer();
		
		Document doc = Jsoup.connect(e.attr("abs:href")).get();
		Element pagemenu = doc.select(".Page_Menu").first();
		
		Elements list = pagemenu.select("a");
		ListIterator<Element> elements = list.listIterator(list.size());
		
		while(elements.hasPrevious()){
			Element element = elements.previous();
			String url = getPDFurl(element);
			if(url.equals("")){
				result.append("<li>");
				result.append(element.ownText() + " - no PDF uploaded yet!");
				result.append("</li>");
			}else{
				result.append("<li><a target=_blanck href=\"");
				result.append(url);
				result.append("\">");
				result.append(element.ownText());
				result.append("</a></li>");
			}
		}
		
		return result;
	}

	private String getPDFurl(Element element) throws IOException {
		Document doc = Jsoup.connect(element.attr("abs:href")).get();
		String url = "";
		try {
			url = doc.select("iframe").first().attr("src");
		}catch (Exception e){}
		
		return url;
	}
}
