/*
 * Copyright  (c) 2017. By AsherLi0103
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.github.asherli0103.utils;

import com.github.asherli0103.utils.exception.UtilException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * XML工具类<br>
 * 此工具使用w3c dom工具，不需要依赖第三方包。<br>
 *
 * @author xiaoleilu
 */
public class XmlUtil {

    /**
     * 在XML中无效的字符 正则
     */
    public final static ThreadLocal<String> INVALID_REGEX = ThreadLocal.withInitial(() -> "[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]");

    /*
     * XML 文件文档
     */
    private Document doc = null;
    //字符编码
    private String charEncode = "utf-8";


    /**
     * 根据xml内容直接生成xml dom
     *
     * @param xmlContent xml内容
     * @throws ParserConfigurationException exception
     * @throws IOException                  exception
     * @throws SAXException                 exception
     */
    public XmlUtil(String xmlContent) throws ParserConfigurationException, SAXException, IOException {
        StringReader sr = new StringReader(xmlContent);
        InputSource is = new InputSource(sr);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        //不检查DTD
        db.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));
        //读取文件
        doc = db.parse(is);
    }

    /**
     * 根据xml文件路径生成xml dom
     *
     * @param xmlFile xml文件
     * @throws ParserConfigurationException exception
     * @throws IOException                  exception
     * @throws SAXException                 exception
     */
    public XmlUtil(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        //不检查DTD
        db.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));
        //读取文件
        doc = db.parse(xmlFile);
    }

    /**
     * 根据流生成xml dom
     *
     * @param is 流
     * @throws ParserConfigurationException exception
     * @throws IOException                  exception
     * @throws SAXException                 exception
     */
    public XmlUtil(InputSource is) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        //不检查DTD
        db.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));
        //读取文件
        doc = db.parse(is);
    }

    /**
     * 读取解析XML文件
     *
     * @param file XML文件
     * @return XML文档对象
     */
    public static Document readXML(File file) {
        if (file == null) {
            throw new NullPointerException("Xml file is null !");
        }
        if (!file.exists()) {
            throw new UtilException("File [" + file.getAbsolutePath() + "] not a exist!");
        }
        if (!file.isFile()) {
            throw new UtilException("[" + file.getAbsolutePath() + "] not a file!");
        }

        try {
            file = file.getCanonicalFile();
        } catch (IOException ignored) {
        }

        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            final DocumentBuilder builder = dbf.newDocumentBuilder();
            return builder.parse(file);
        } catch (Exception e) {
            throw new UtilException("Parse xml file [" + file.getAbsolutePath() + "] error!", e);
        }
    }

    /**
     * 读取解析XML文件
     *
     * @param absoluteFilePath XML文件绝对路径
     * @return XML文档对象
     */
    public static Document readXML(String absoluteFilePath) {
        return readXML(new File(absoluteFilePath));
    }

    /**
     * 将String类型的XML转换为XML文档
     *
     * @param xmlStr XML字符串
     * @return XML文档
     */
    public static Document parseXml(String xmlStr) {
        if (StringUtil.isBlank(xmlStr)) {
            throw new UtilException("Xml content string is empty !");
        }
        xmlStr = cleanInvalid(xmlStr);

        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            final DocumentBuilder builder = dbf.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xmlStr)));
        } catch (Exception e) {
            throw new UtilException("Parse xml file [" + xmlStr + "] error!", e);
        }
    }

    /**
     * 将XML文档转换为String
     *
     * @param doc XML文档
     * @return XML字符串
     */
    public static String toStr(Document doc) {
        return toStr(doc, "UTF-8");
    }

    /**
     * 将XML文档转换为String<br>
     * 此方法会修改Document中的字符集
     *
     * @param doc     XML文档
     * @param charset 自定义XML的字符集
     * @return XML字符串
     */
    public static String toStr(Document doc, String charset) {
        try {
            StringWriter writer = new StringWriter();

            final Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.setOutputProperty(OutputKeys.ENCODING, charset);
            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xformer.transform(new DOMSource(doc), new StreamResult(writer));

            return writer.toString();
        } catch (Exception e) {
            throw new UtilException("Trans xml document to string error!", e);
        }
    }

    /**
     * 将XML文档写入到文件<br>
     * 使用Document中的编码
     *
     * @param doc          XML文档
     * @param absolutePath 文件绝对路径，不存在会自动创建
     */
    public static void toFile(Document doc, String absolutePath) {
        toFile(doc, absolutePath, null);
    }

    /**
     * 将XML文档写入到文件<br>
     *
     * @param doc          XML文档
     * @param absolutePath 文件绝对路径，不存在会自动创建
     * @param charset      自定义XML文件的编码
     */
    public static void toFile(Document doc, String absolutePath, String charset) {
        if (StringUtil.isBlank(charset)) {
            charset = doc.getXmlEncoding();
        }
        if (StringUtil.isBlank(charset)) {
            charset = "UTF-8";
        }

        BufferedWriter writer = null;
        try {
            writer = FileUtil.getWriter(absolutePath, charset, false);
            Source source = new DOMSource(doc);
            final Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.setOutputProperty(OutputKeys.ENCODING, charset);
            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xformer.transform(source, new StreamResult(writer));
        } catch (Exception e) {
            throw new UtilException("Trans xml document to string error!", e);
        } finally {
            IOUtil.close(writer);
        }
    }

    /**
     * 创建XML文档<br>
     * 创建的XML默认是utf8编码，修改编码的过程是在toStr和toFile方法里，既XML在转为文本的时候才定义编码
     *
     * @param rootElementName 根节点名称
     * @return XML文档
     */
    public static Document createXml(String rootElementName) {
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = dbf.newDocumentBuilder();
        } catch (Exception e) {
            throw new UtilException("Create xml document error!", e);
        }
        Document doc = builder.newDocument();
        doc.appendChild(doc.createElement(rootElementName));

        return doc;
    }

    /**
     * 去除XML文本中的无效字符
     *
     * @param xmlContent XML文本
     * @return 当传入为null时返回null
     */
    public static String cleanInvalid(String xmlContent) {
        if (xmlContent == null) return null;
        return xmlContent.replaceAll(INVALID_REGEX.get(), "");
    }

    /**
     * 根据节点名获得子节点列表
     *
     * @param element 节点
     * @param tagName 节点名
     * @return 节点列表
     */
    public static List<Element> getElements(Element element, String tagName) {
        final NodeList nodeList = element.getElementsByTagName(tagName);
        return transElements(element, nodeList);
    }

    /**
     * 根据节点名获得第一个子节点
     *
     * @param element 节点
     * @param tagName 节点名
     * @return 节点
     */
    public static Element getElement(Element element, String tagName) {
        final NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList == null || nodeList.getLength() < 1) {
            return null;
        }
        int length = nodeList.getLength();
        for (int i = 0; i < length; i++) {
            Element childEle = (Element) nodeList.item(i);
            if (childEle == null || childEle.getParentNode() == element) {
                return childEle;
            }
        }
        return null;
    }

    /**
     * 根据节点名获得第一个子节点
     *
     * @param element 节点
     * @param tagName 节点名
     * @return 节点中的值
     */
    public static String elementText(Element element, String tagName) {
        Element child = getElement(element, tagName);
        return child == null ? null : child.getTextContent();
    }

    /**
     * 根据节点名获得第一个子节点
     *
     * @param element      节点
     * @param tagName      节点名
     * @param defaultValue d
     * @return 节点中的值
     */
    public static String elementText(Element element, String tagName, String defaultValue) {
        Element child = getElement(element, tagName);
        return child == null ? defaultValue : child.getTextContent();
    }

    /**
     * 将NodeList转换为Element列表
     *
     * @param nodeList NodeList
     * @return Element列表
     */
    public static List<Element> transElements(NodeList nodeList) {
        return transElements(null, nodeList);
    }

    /**
     * 将NodeList转换为Element列表
     *
     * @param parentEle 父节点，如果指定将返回此节点的所有直接子节点，nul返回所有就节点
     * @param nodeList  NodeList
     * @return Element列表
     */
    public static List<Element> transElements(Element parentEle, NodeList nodeList) {
        final ArrayList<Element> elements = new ArrayList<Element>();
        int length = nodeList.getLength();
        for (int i = 0; i < length; i++) {
            Element element = (Element) nodeList.item(i);
            if (parentEle == null || element.getParentNode() == parentEle) {
                elements.add(element);
            }
        }

        return elements;
    }

    /**
     * 将可序列化的对象转换为XML写入文件，已经存在的文件将被覆盖<br>
     * Writes serializable object to a XML file. Existing file will be overwritten
     *
     * @param <T>  d
     * @param dest 目标文件
     * @param t    对象
     * @throws IOException e
     */
    public static <T> void writeObjectAsXml(File dest, T t) throws IOException {
        FileOutputStream fos = null;
        XMLEncoder xmlenc = null;
        try {
            fos = new FileOutputStream(dest);
            xmlenc = new XMLEncoder(new BufferedOutputStream(fos));
            xmlenc.writeObject(t);
        } finally {
            IOUtil.close(fos);
            if (xmlenc != null) {
                xmlenc.close();
            }
        }
    }

    /**
     * 从XML中读取对象
     * Reads serialized object from the XML file.
     *
     * @param <T>    e
     * @param source XML文件
     * @return 对象
     * @throws IOException e
     */
    @SuppressWarnings("unchecked")
    public static <T> T readObjectFromXml(File source) throws IOException {
        Object result = null;
        FileInputStream fis = null;
        XMLDecoder xmldec = null;
        try {
            fis = new FileInputStream(source);
            xmldec = new XMLDecoder(new BufferedInputStream(fis));
            result = xmldec.readObject();
        } finally {
            IOUtil.close(fis);
            if (xmldec != null) {
                xmldec.close();
            }
        }
        return (T) result;
    }

    /**
     * 根据节点名称序号读取节点
     *
     * @param nodeName 节点名称
     * @param index    第几个，序号从0开始。
     * @return Node
     */
    public Node readNode(String nodeName, int index) {
        NodeList list = doc.getElementsByTagName(nodeName);
        return list.item(index);
    }

    /**
     * 根据节点名称读取节点列表
     *
     * @param nodeName 节点名称
     * @return NodeList
     */
    public NodeList readNodeList(String nodeName) {
        return doc.getElementsByTagName(nodeName);
    }

    /**
     * 得到某个节点的值
     *
     * @param node 某个节点
     * @return String
     */
    public String getNodeValue(Node node) {
        return node.getNodeValue();
    }

    /**
     * 得到某个元素属性的值
     *
     * @param element 元素
     * @param attr    属性名
     * @return String
     */
    public String getElementAttr(Element element, String attr) {
        return element.getAttribute(attr);
    }

    /**
     * 更新某个节点的内容
     *
     * @param node 节点
     * @param val  更新的值
     */
    public void setNodeValue(Node node, String val) {
        node.setNodeValue(val);
    }

    /**
     * 更新某个节点的属性
     *
     * @param element 节点
     * @param attr    属性名
     * @param attrVal 属性值
     */
    public void setElementAttr(Element element, String attr, String attrVal) {
        element.setAttribute(attr, attrVal);
    }

    /**
     * XML org.w3c.dom.Document 转 String
     *
     * @return string
     * @throws Exception exception
     */
    public String xmlToString() throws Exception {
        // XML转字符串
        String xmlStr = "";
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        t.setOutputProperty("encoding", this.charEncode);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        t.transform(new DOMSource(doc), new StreamResult(bos));
        xmlStr = bos.toString();
        return xmlStr;
    }

    /**
     * 设置编码。不设置采用Utf-8编码。
     *
     * @param charEncode 编码 如：utf-8
     */
    public void setCharEncode(String charEncode) {
        this.charEncode = charEncode;
    }

}
