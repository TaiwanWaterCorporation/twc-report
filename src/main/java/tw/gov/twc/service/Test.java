package tw.gov.twc.service;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.ws.xml.Marshaller;
import com.jaspersoft.jasperserver.ws.xml.Unmarshaller;

/*
 * 此類別為測試備忘用途
 */
public class Test {
	
	static final String REQUEST_PARAMENTER_RD = "ResourceDescriptor";
    static final String REQUEST_PARAMENTER_MOVE_TO = "moveTo";
    static final String REQUEST_PARAMENTER_COPY_TO = "copyTo";
    static final String SWITCH_PARAM_GET_LOCAL_RESOURCE = "GET_LOCAL_RESOURCE"; //indicates that the first call was made to retrieve a local resource
    static final String REQUEST_PARAMENTER_ROLES = "roles";
    static final String REQUEST_PARAMENTER_USERS = "users";
    static final String REQUEST_PARAMENTER_SEPARATOR = ",";
    static final String REQUEST_PARAMENTER_ATTRIBUTES = "attributeKeys";
    static final String REQUEST_PARAMENTER_LIST_SUB_ORGS = "listSubOrgs";

    static final String SUPERUSER = "SUPERUSER";
    static final String ADMINISTRATOR = "ADMINISTRATOR";

    static final String FILE_DATA = "fileData";
    
    static final byte TYPE_BOOLEAN = 1;
	static final byte TYPE_SINGLE_VALUE = 2;
	static final byte TYPE_SINGLE_SELECT_LIST_OF_VALUES = 3;
	static final byte TYPE_SINGLE_SELECT_QUERY = 4;
	static final byte TYPE_MULTI_VALUE = 5;
	static final byte TYPE_MULTI_SELECT_LIST_OF_VALUES = 6;
	static final byte TYPE_MULTI_SELECT_QUERY = 7;
	static final byte TYPE_SINGLE_SELECT_LIST_OF_VALUES_RADIO = 8;
	static final byte TYPE_SINGLE_SELECT_QUERY_RADIO = 9;
	static final byte TYPE_MULTI_SELECT_LIST_OF_VALUES_CHECKBOX = 10;
	static final byte TYPE_MULTI_SELECT_QUERY_CHECKBOX = 11;

	private final DefaultHttpClient client;
	private final String serverUrl;

	Test() {
		PoolingClientConnectionManager cm = new PoolingClientConnectionManager();
		cm.setMaxTotal(10);

		client = new DefaultHttpClient(cm);
		client.getCredentialsProvider().setCredentials(AuthScope.ANY,
				new UsernamePasswordCredentials("jasperadmin", "jasperadmin"));
		serverUrl = "http://192.168.22.107:8600/jasperserver";
	}

	void run(String targetUri, Map<String, ?> params, OutputStream output) throws Exception {
		if (StringUtils.isBlank(targetUri)) {
			throw new RuntimeException("uri error");
		}
		URIBuilder uriBuilder = new URIBuilder()
			.setScheme("http").setHost("192.168.22.107").setPort(8600).setUserInfo("jasperadmin", "jasperadmin")
			.setPath("/jasperserver/rest_v2/reports" + targetUri + ".pdf");
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, ?> entry : params.entrySet()) {
				uriBuilder.addParameter(entry.getKey(), ObjectUtils.toString(entry.getValue()));
			}
		}
		HttpGet get = new HttpGet(uriBuilder.build());

		try {
			HttpResponse rsp = client.execute(get);
			int code = rsp.getStatusLine().getStatusCode();
			if (code != 200) {
				throw new RuntimeException("運行報表失敗 error code : " + code);
			}
			System.out.println("****************************");
			System.out.println("*********運行報表成功*************");
			System.out.println("****************************");
			IOUtils.copy(rsp.getEntity().getContent(), output);
		} finally {
			get.releaseConnection();
		}
	}

	List<ResourceDescriptor> search(String targetUri) throws Exception {
		URIBuilder uriBuilder = new URIBuilder()
		.setScheme("http").setHost("192.168.22.107").setPort(8600).setUserInfo("jasperadmin", "jasperadmin")
		.setPath("/jasperserver/rest/resources" + StringUtils.defaultIfEmpty(targetUri, "/"));
		HttpGet get = new HttpGet(uriBuilder.build()/*serverUrl + "/rest/resources" + StringUtils.defaultIfEmpty(targetUri, "/")*/);
		List<ResourceDescriptor> list = new ArrayList<ResourceDescriptor>();
		try {
			HttpResponse rsp = client.execute(get);
			int code = rsp.getStatusLine().getStatusCode();
			if (code != 200) {
				throw new RuntimeException("查詢失敗 error code : " + code);
			}
			System.out.println("****************************");
			System.out.println("*********查詢成功*************");
			System.out.println("****************************");
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    		docBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(
					rsp.getEntity().getContent());
			Element root = doc.getDocumentElement();
			NodeList rdxmls = root.getChildNodes();
			for (int i = 0, l = rdxmls.getLength(); i < l; i++) {
				Node rpNode = rdxmls.item(i);
				if (rpNode.getNodeType() == Node.ELEMENT_NODE
						&& rpNode.getNodeName().equalsIgnoreCase(
								"resourceDescriptor")) {
					list.add(Unmarshaller
							.readResourceDescriptor((Element) rpNode));
				}
			}
		} finally {
			get.releaseConnection();
		}
		return list;
	}

	ResourceDescriptor detail(String targetUri) throws Exception {
		URIBuilder uriBuilder = new URIBuilder(serverUrl);
		String rest = "/rest/resource";
		uriBuilder.addParameter("rest", rest);
		uriBuilder.addParameter("target", targetUri);
		HttpGet get = new HttpGet(uriBuilder.build().toString());
//		HttpGet get = new HttpGet(serverUrl + "/rest/resource" + targetUri);
		try {
			HttpResponse rsp = client.execute(get);
			int code = rsp.getStatusLine().getStatusCode();
			if (code != 200) {
				throw new RuntimeException("查詢失敗 error code : " + code);
			}
			System.out.println("****************************");
			System.out.println("*********查詢成功*************");
			System.out.println("****************************");
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    		docBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(
					rsp.getEntity().getContent());
			return Unmarshaller
					.readResourceDescriptor(doc.getDocumentElement());
		} finally {
			get.releaseConnection();
		}
	}

	void create(String targetUri, String lable, String description)
			throws Exception {
		ResourceDescriptor rd = build(targetUri, Type.Folder, lable,
				description);

		String reqXml = new Marshaller().writeResourceDescriptor(rd);
		HttpPut put = new HttpPut(serverUrl + "/rest/resource");
		StringEntity entity = new StringEntity(reqXml, ContentType.create(
				"text/plain", "UTF-8"));
		put.setEntity(entity);
		try {
			HttpResponse rsp = client.execute(put);
			int code = rsp.getStatusLine().getStatusCode();
			if (code != 201) {
				throw new RuntimeException("建立失敗 error code : " + code);
			}
			System.out.println("****************************");
			System.out.println("*********建立成功*************");
			System.out.println("****************************");
		} finally {
			put.releaseConnection();
		}

	}

	void download(String targetUri) throws Exception {
		if (StringUtils.isBlank(targetUri)) {
			throw new RuntimeException("uri error");
		}
		URIBuilder uriBuilder = new URIBuilder(serverUrl);
		String rest = "/rest/resource";
		String fileData = "?fileData=true";
		uriBuilder.addParameter("rest", rest);
		uriBuilder.addParameter("target", targetUri);
		uriBuilder.addParameter("fileData", fileData);
		HttpGet get = new HttpGet(uriBuilder.build().toString());
//		HttpGet get = new HttpGet(serverUrl + "/rest/resource" + targetUri
//				+ "?fileData=true");
		try {
			HttpResponse rsp = client.execute(get);
			int code = rsp.getStatusLine().getStatusCode();
			if (code != 200) {
				throw new RuntimeException("下載失敗 error code : " + code);
			}
			System.out.println("****************************");
			System.out.println("*********下載成功*************");
			System.out.println("****************************");
			System.out.println(EntityUtils.toString(rsp.getEntity(), "UTF-8"));
		} finally {
			get.releaseConnection();
		}
	}

	void upload(String targetUri, String lable, String description,
			String dataSourceUri, FileUnit fileUnit) throws Exception {
		String jrxmlUri = targetUri + "/files/" + fileUnit.getName();
		ResourceDescriptor rd = build(targetUri, Type.Report, lable,
				description, dataSourceUri, jrxmlUri, fileUnit);

		String reqXml = new Marshaller().writeResourceDescriptor(rd);
		HttpPut put = new HttpPut(serverUrl + "/rest/resource");
		MultipartEntity entity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		entity.addPart(REQUEST_PARAMENTER_RD, new StringBody(reqXml,
				"text/plain", Charset.forName("UTF-8")));
		entity.addPart(jrxmlUri,
				new ByteArrayBody(fileUnit.getData(), jrxmlUri));
		put.setEntity(entity);
		try {
			HttpResponse rsp = client.execute(put);
			int code = rsp.getStatusLine().getStatusCode();
			if (code != 201) {
				throw new RuntimeException("上傳失敗 error code : " + code);
			}
			System.out.println("****************************");
			System.out.println("*********上傳成功*************");
			System.out.println("****************************");
		} finally {
			put.releaseConnection();
		}
	}

	void update(String targetUri, String lable, String description,
			String dataSourceUri, FileUnit fileUnit) throws Exception {
		String jrxmlUri = fileUnit != null ? targetUri + "/files/"
				+ fileUnit.getName() : null;
		ResourceDescriptor rd = build(targetUri, Type.Report, lable,
				description, dataSourceUri, jrxmlUri, fileUnit);

		String reqXml = new Marshaller().writeResourceDescriptor(rd);
		HttpPost post = new HttpPost(serverUrl + "/rest/resource" + targetUri);

		if (jrxmlUri != null) {
			MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			entity.addPart(REQUEST_PARAMENTER_RD, new StringBody(reqXml,
					"text/plain", Charset.forName("UTF-8")));
			entity.addPart(jrxmlUri, new ByteArrayBody(fileUnit.getData(),
					jrxmlUri));
			post.setEntity(entity);
		} else {
			StringEntity entity = new StringEntity(reqXml, ContentType.create(
					"text/plain", "UTF-8"));
			post.setEntity(entity);
		}

		try {
			HttpResponse rsp = client.execute(post);
			int code = rsp.getStatusLine().getStatusCode();
			if (code != 200) {
				throw new RuntimeException("更新失敗 error code : " + code);
			}
			System.out.println("****************************");
			System.out.println("*********更新成功*************");
			System.out.println("****************************");
		} finally {
			post.releaseConnection();
		}
	}

	void update(String targetUri, String lable, String description)
			throws Exception {
		ResourceDescriptor rd = build(targetUri, Type.Folder, lable,
				description);

		String reqXml = new Marshaller().writeResourceDescriptor(rd);
		HttpPost post = new HttpPost(serverUrl + "/rest/resource" + targetUri);
		StringEntity entity = new StringEntity(reqXml, ContentType.create(
				"text/plain", "UTF-8"));
		post.setEntity(entity);
		try {
			HttpResponse rsp = client.execute(post);
			int code = rsp.getStatusLine().getStatusCode();
			if (code != 200) {
				throw new RuntimeException("更新失敗 error code : " + code);
			}
			System.out.println("****************************");
			System.out.println("*********更新成功*************");
			System.out.println("****************************");
		} finally {
			post.releaseConnection();
		}
	}

	void delete(String targetUri) throws Exception {
		if (StringUtils.isBlank(targetUri)) {
			throw new RuntimeException("uri error");
		}
		HttpDelete delete = new HttpDelete(serverUrl + "/rest/resource"
				+ targetUri);
		try {
			HttpResponse rsp = client.execute(delete);
			int code = rsp.getStatusLine().getStatusCode();
			if (code != 200) {
				throw new RuntimeException("刪除失敗 error code : " + code);
			}
			System.out.println("****************************");
			System.out.println("*********刪除成功*************");
			System.out.println("****************************");
		} finally {
			delete.releaseConnection();
		}
	}

	enum Type {
		Report, Folder
	}

	ResourceDescriptor build(String targetUri, Type type, String lable,
			String description) {
		final String name = takeName(targetUri);
		if (StringUtils.isBlank(name) || type == null) {
			throw new RuntimeException("uri error");
		}
		ResourceDescriptor rd = new ResourceDescriptor();
		switch (type) {
		case Report:
			rd.setWsType(ResourceDescriptor.TYPE_REPORTUNIT);
			break;
		case Folder:
			rd.setWsType(ResourceDescriptor.TYPE_FOLDER);
			break;
		}
		rd.setName(name);
		rd.setUriString(targetUri);
		rd.setLabel(StringUtils.defaultIfEmpty(lable, name));
		rd.setDescription(StringUtils.defaultIfEmpty(lable, description));
		return rd;
	}

	ResourceDescriptor build(String targetUri, Type type, String lable,
			String description, String dataSourceUri, String jrxmlUri,
			FileUnit fileUnit) throws Exception {

		ResourceDescriptor rd = build(targetUri, type, lable, description);
		List<ResourceDescriptor> children = rd.getChildren();

		if (dataSourceUri != null) {
			ResourceDescriptor dataSource = new ResourceDescriptor();
			dataSource.setWsType(ResourceDescriptor.TYPE_DATASOURCE);
			dataSource.setResourceProperty(
					ResourceDescriptor.PROP_FILERESOURCE_REFERENCE_URI,
					dataSourceUri);
			dataSource.setResourceProperty(
					ResourceDescriptor.PROP_FILERESOURCE_IS_REFERENCE, true);
			children.add(dataSource);
		}

		if (jrxmlUri != null) {
			String fileName = takeName(jrxmlUri);
			ResourceDescriptor jrxml = new ResourceDescriptor();
			jrxml.setName(fileName);
			jrxml.setLabel(fileName);
			jrxml.setWsType(ResourceDescriptor.TYPE_JRXML);
			jrxml.setUriString(jrxmlUri);
			jrxml.setHasData(true);
			jrxml.setResourceProperty(
					ResourceDescriptor.PROP_RU_IS_MAIN_REPORT, true);
			children.add(jrxml);
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    		docBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(
					new ByteArrayInputStream(fileUnit.getData()));
			XPathExpression expr = XPathFactory.newInstance().newXPath()
					.compile("/jasperReport/parameter");
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				String parameName = nodes.item(i).getAttributes()
						.getNamedItem("name").getNodeValue();
				ResourceDescriptor input = new ResourceDescriptor();
				input.setName(parameName);
				input.setLabel(parameName);
				input.setWsType(ResourceDescriptor.TYPE_INPUT_CONTROL);
				input.setResourceProperty(
						ResourceDescriptor.PROP_INPUTCONTROL_TYPE,
						TYPE_SINGLE_VALUE);

				ResourceDescriptor inputType = new ResourceDescriptor();
				inputType.setName(parameName);
				inputType.setLabel(parameName);
				inputType.setWsType(ResourceDescriptor.TYPE_DATA_TYPE);
				inputType.setResourceProperty(
						ResourceDescriptor.PROP_DATATYPE_TYPE,
						ResourceDescriptor.DT_TYPE_TEXT);

				input.getChildren().add(inputType);
				children.add(input);
			}
		}

		return rd;
	}

	DocumentBuilder getDocumentBuilder() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder domBuilder = null;
		try {
			dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			dbf.setFeature("http://xml.org/sax/features/external-general-entities",false);
			dbf.setFeature("http://xml.org/sax/features/external-parameter-entities",false);
			domBuilder = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return domBuilder;
	}

	String takeName(String uri) {
		uri = StringUtils.remove(uri, ' ');
		return StringUtils.substringAfterLast(uri, "/");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// URI 大小寫敏感
		Test t = new Test();
		/*{
			t.search("/reports/ooo");

			Map<String, String> map = new HashMap<>();
			map.put("name", "報");

			try (OutputStream os = new FileOutputStream("C:\\Users\\mow\\Desktop\\allenox.pdf")) {
				t.run("/reports/ooo/allenReport2", map, os);
			}
		}*/

		
		 //{ t.create("/reports/xxx", "測試", "測試"); }
		
		/*
		 * { ByteArrayOutputStream os = new ByteArrayOutputStream(); try
		 * (InputStream is = new
		 * FileInputStream("C:\\Users\\mow\\Desktop\\allen.jrxml")) { byte[]
		 * buff = new byte[1024]; int len; while ((len = is.read(buff)) != -1) {
		 * os.write(buff, 0, len); } } t.upload("/reports/ooo/allenReport2",
		 * "報表2", "報表2", "/datasources/TWC", new FileUnit("allen.jrxml",
		 * os.toByteArray())); }
		 */

		/*
		 * { t.download("/reports/ooo/allenReport_files/allen.jrxml"); } {
		 * t.update("/reports/ooo", "修改", "蔡蔡蔡蔡蔡");
		 * 
		 * ResourceDescriptor rd = t.detail("/reports/ooo");
		 * System.out.println(rd.getLabel()); } { ByteArrayOutputStream os = new
		 * ByteArrayOutputStream(); try (InputStream is = new
		 * FileInputStream("C:\\Users\\mow\\Desktop\\allen.jrxml")) { byte[]
		 * buff = new byte[1024]; int len; while ((len = is.read(buff)) != -1) {
		 * os.write(buff, 0, len); } } t.update("/reports/ooo/allenReport",
		 * "報表修改", "報表修改", null, new FileUnit("allen.jrxml", os.toByteArray()));
		 * } { t.delete("/reports/ooo"); }
		 */
		t.client.getConnectionManager().shutdown();
	}

	public static final class FileUnit {

		private String name;
		private byte[] data;
		
		public FileUnit(String name, byte[] data) {
			super();
			this.name = name.replace(" ", "");
			this.data = data;
		}

		public String getName() {
			return name;
		}

		public byte[] getData() {
			return data;
		}

	}
}
