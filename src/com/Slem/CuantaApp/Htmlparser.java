package com.Slem.CuantaApp;



import java.util.ArrayList;
import java.util.List;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.beans.FilterBean;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;


public class Htmlparser {
private static String url;
	public Htmlparser(String URL) {
	
		url = URL;
		
		
	}
	
	

	public List<Imagen> run(){
		
		
		List<Imagen> data = new ArrayList<Imagen>();

		System.out.println("url es: " + url);
		
		NodeList nodelist = getNodesFromDivClassBoxStory();
		NodeList list = new NodeList();
    	NodeFilter filter = new TagNameFilter ("IMG");

		try {
	    	for (NodeIterator e =nodelist.elements() ; e.hasMoreNodes ();)
					e.nextNode ().collectInto (list, filter);
			} catch (ParserException e1) {
				e1.printStackTrace();
			}
		

		


    	for(int m=0;m<list.size();m++){
    		Node temp =  list.elementAt(m); 		
    		ImageTag link = new ImageTag();
    		if( temp instanceof ImageTag){
    			link = (ImageTag) temp;
    			
    		   	   			
    			data.add(new Imagen(link.getAttribute("alt"),  link.extractImageLocn()));
			
    		    		
    	}
    	
    	}
    	return data;
	}
	public static NodeList getNodesFromDivClassBoxStory(){
		
		TagNameFilter filter0 = new TagNameFilter ();
	    filter0.setName ("DIV");
	    HasAttributeFilter filter1 = new HasAttributeFilter();
	    filter1.setAttributeName("class");
	    filter1.setAttributeValue("box story");
	    NodeFilter[] array0 = new NodeFilter[2];
	    array0[0] = filter0;
	    array0[1] = filter1;
	    AndFilter filter2 = new AndFilter ();
	    filter2.setPredicates (array0);
	    NodeFilter[] array1 = new NodeFilter[1];
	    array1[0] = filter2;
	    FilterBean bean = new FilterBean ();
	    bean.setFilters (array1);
	    
	    bean.setURL (url);
	    
    return bean.getNodes();
	}
	
	
	

}
