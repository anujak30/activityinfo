/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.util.xml;

import java.util.Map.Entry;
import java.util.Stack;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class XmlBuilder {

	private TransformerHandler hd;
	
	private static class State {
		private XmlElement element;
		private String qname;
		private String defaultNamespace;
		
		public State(XmlElement element, String qname, String defaultNamespace) {
			super();
			this.element = element;
			this.qname = qname;
			this.defaultNamespace = defaultNamespace;
		}
		
		public void setElement(XmlElement element) {
			this.element = element;
		}
		public XmlElement getElement() {
			return element;
		}

		public void setQname(String qname) {
			this.qname = qname;
		}

		public String getQname() {
			return qname;
		}

		public void setDefaultNamespace(String defaultNamespace) {
			this.defaultNamespace = defaultNamespace;
		}

		public String getDefaultNamespace() {
			return defaultNamespace;
		}
	}
	
	private XmlElement pendingTag = null;
	private Stack<State> openTags = new Stack<State>();
		
	public XmlBuilder(StreamResult result) throws TransformerConfigurationException, SAXException {
		SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
		
		hd = tf.newTransformerHandler();
		Transformer serializer = hd.getTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
		serializer.setOutputProperty(OutputKeys.INDENT,"yes");
		hd.setResult(result);
	}
	
	public void startDocument() throws SAXException {

		hd.startDocument();
	}
	
	public void endDocument() throws SAXException {
		hd.endDocument();
	}
	
	protected String getPrefix(XmlElement tag) {
		return getPrefix(tag, tag.getNamespace());
	}
	
	protected String getPrefix(XmlElement tag, String namespace) {
		
		String prefix = tag.getNamespacePrefixes().get(namespace);
		
		if(prefix != null) {
			return prefix;
		}
		
		for(int i=openTags.size()-1; i>=0; i--) {
			
			prefix = openTags.get(i).getElement().getNamespacePrefixes().get(namespace);
			
			if(prefix != null) {
                return prefix;
            }
		}
		return null;
	}

	
	protected void writeTag(XmlElement tag, boolean closed) throws SAXException {
		if(tag != null) {

			AttributesImpl atts = new AttributesImpl();
			
			String prefix = getPrefix(tag);
			String qname = tag.getName();
			
			if(prefix != null) {
				qname = prefix + ":" + qname;
			}
			
			String defaultNamespace = "";
			if(openTags.size() != 0) {
				defaultNamespace = openTags.peek().getDefaultNamespace();
			}
			
			if(prefix == null && !defaultNamespace.equals(tag.getNamespace()) ) {

				//atts.addAttribute("", "", "xmlns", "string", tag.getNamespace());
				defaultNamespace = tag.getNamespace();
			}

            for(Entry<String, String> entry : tag.getNamespacePrefixes().entrySet()) {
                hd.startPrefixMapping(entry.getValue(), entry.getKey());
            }
			
			for(XmlAttribute attr : tag.getAttributes()) {
				
				String attrQName = attr.getName();
				String attrNs = attr.getNamespace();
//				if(attr.getNamespace() == null) {
//					attrNs = tag.getNamespace();
//				}
				if(attrNs != null && !defaultNamespace.equals(attrNs)) {
					String attrPrefix = getPrefix(tag, attrNs);
					if(attrPrefix == null) {
						throw new SAXException();
					}
					attrQName = attrPrefix + ":" + attr.getName();
				}
					
				atts.addAttribute(attrNs, attr.getName(), attrQName, "string", attr.getValue());
			}
			
//			for(Entry<String, String> entry : tag.getNamespacePrefixes().entrySet()) {
//				atts.addAttribute("", "", "xmlns:" + entry.getValue(), "string", entry.getKey());
//			}
			
			hd.startElement(tag.getNamespace(),tag.getName(),qname,atts);
			
			if(closed) {
				
				if(tag.getInnerText() != null) {
					char[] text = pendingTag.getInnerText().toCharArray();
					hd.characters(text, 0, text.length);
				}
				
				hd.endElement(tag.getNamespace(), tag.getName(), qname);
				
			} else {
				openTags.push(new State(tag, qname, defaultNamespace));
			}
			
		}	
	}
	
	protected void writePendingTag() throws SAXException {
		if(pendingTag!=null) {
	
			writeTag(pendingTag, pendingTag.isClosed());
									
			pendingTag = null;
		}
	}
	
	
	public XmlBuilder text(String text) throws SAXException {
		writePendingTag();
		char[] chars = text.toCharArray();
		hd.characters(chars, 0, chars.length);
		return this;
	}

    public XmlBuilder cdata(String text) throws SAXException {
        writePendingTag();
        hd.startCDATA();
		char[] chars = text.toCharArray();
		hd.characters(chars, 0, chars.length);
        hd.endCDATA();

        return this;
    }
	
	public XmlBuilder close() throws SAXException {
		
		writePendingTag();
				
		State openTag = openTags.pop();
		
		hd.endElement(openTag.getElement().getNamespace(), openTag.getElement().getName(), openTag.getQname());
		
        for(Entry<String, String> entry : openTag.getElement().getNamespacePrefixes().entrySet()) {
            hd.endPrefixMapping(entry.getValue());
        }

		return this;
	}
	
	public <XmlElementT extends XmlElement> XmlElementT start(XmlElementT tag) throws SAXException {
		
		writePendingTag();
		
		pendingTag = tag;

		return tag;	
	}
	
	public <T extends XmlElement> T e(T element) throws SAXException {
		writePendingTag();
		start(element).close();	
		return element;
	}
	
	public XmlBuilder e(XmlElement tag, SimpleXmlElement... child) throws SAXException {
		start(tag);
		for(SimpleXmlElement c : child) {
			e(c);
		}
		close();
		return this;
	}
	
	public XmlBuilder e(SimpleXmlElement tag) throws SAXException {
		e(new XmlElement(tag.getNamespace(), tag.getName())).text(tag.getText().toString());
		return this;
	}

	
	public XmlBuilder closeVerify(String tagName) throws SAXException {
		
		
//		if(!openTags.peek().equals(tagName)) {
//			throw new MismatchedTagError(openTags.peek());
//		}
		return close();
	}
	

}
