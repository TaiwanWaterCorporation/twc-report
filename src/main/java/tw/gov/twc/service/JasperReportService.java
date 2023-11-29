package tw.gov.twc.service;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.lf5.util.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jaspersoft.jasperserver.api.engine.scheduling.domain.ReportJob;
import com.jaspersoft.jasperserver.api.engine.scheduling.domain.ReportJobRepositoryDestination;
import com.jaspersoft.jasperserver.api.engine.scheduling.domain.ReportJobSimpleTrigger;
import com.jaspersoft.jasperserver.api.engine.scheduling.domain.ReportJobSource;
import com.jaspersoft.jasperserver.api.engine.scheduling.domain.ReportJobTrigger;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.ws.xml.Unmarshaller;

import tw.gov.twc.mapper.JROutputMapper;
import tw.gov.twc.mapper.UtlReportsMapper;
import tw.gov.twc.model.JROutput;
import tw.gov.twc.model.UtlReports;
import tw.gov.twc.spring.CommonKey;
import tw.gov.twc.type.TwDate;

/**
 * 提供報表調用服務
 * 
 * @author Eric
 */
@Component
public class JasperReportService {
	
	private final DefaultHttpClient client;
	
	@Autowired
	private UtlReportsMapper utlReportsMapper;
	@Autowired
	private JROutputMapper jROutputMapper;
	@Value("${jasperserver.scheme}")
	private String scheme;
	@Value("${jasperserver.host}")
	private String host;
	@Value("${jasperserver.port}")
	private int port;
	@Value("${jasperserver.userName}")
	private String userName;
	@Value("${jasperserver.password}")
	private String password;
	@Value("${jasperserver.webRoot}")
	private String webRoot;
	@Value("#{'${ap.server.scheme}'+'://'+'${ap.server.domain}'}")
	private String serverDomain;
	
	/**
	 * 建構子
	 */
	public JasperReportService() {
		
		PoolingClientConnectionManager cm = new PoolingClientConnectionManager();
		cm.setMaxTotal(3);

		client = new DefaultHttpClient(cm);
	}
	
	/**
	 * 實時前景運行報表
	 * 
	 * @param userId 使用者ID
	 * @param sysId 系統ID
	 * @param folderId 目錄ID
	 * @param reportId 報表ID
	 * @param rowQueryString 傳入參數
	 * @param response HttpServletResponse
	 * @throws Exception
	 */
	public void run(
			String userId, String sysId, String folderId, String reportId, 
			String rowQueryString, HttpServletResponse response) throws Exception {
		UtlReports report = utlReportsMapper.find(sysId, folderId, reportId);
		if (report == null) {
			throw new ReportConfigNotFountException();
		}
		TwDate startDate = new TwDate();
		URIBuilder uriBuilder = 
				getBaseURIBuilder("/rest_v2/reports", getReportUnitURI(folderId, reportId) + "." + report.getReport_type());
		if (rowQueryString != null) {
			for (NameValuePair n : URLEncodedUtils.parse(rowQueryString, Charset.forName("UTF-8"))) {
				if (!n.getName().startsWith(CommonKey.PARAM_PREFIX)) {
					uriBuilder.addParameter(n.getName(), n.getValue());
				}
			}
		}
		HttpGet get = new HttpGet(uriBuilder.build());

		try {
			HttpResponse rsp = client.execute(get);
			int code = rsp.getStatusLine().getStatusCode();
			if (code != 200) {
				throw new RuntimeException("運行報表失敗 error code : " + code + " in " + uriBuilder.build());
			}
			String contentType = getContentType(report.getReport_type());
			if (contentType != null) {
				response.setContentType(contentType);
			}
			
			if(report.getReport_name().indexOf("${TIMESTAMP}") != -1) {
				TwDate tmp = new TwDate(new Timestamp(System.currentTimeMillis()));
				String a = report.getReport_name().replace("${TIMESTAMP}", "") + "_" +tmp.toString();
				response.setHeader("Content-Disposition", "inline; filename=" + URLEncoder.encode(a ,"UTF-8")  + "." + report.getReport_type());
			}else {
				response.setHeader("Content-Disposition", "inline; filename=" + report.getReport_id()  + "." + report.getReport_type());
			}
			
			response.setHeader("Access-Control-Allow-Origin", "https://wbs.water.gov.tw:8443");
			response.setHeader("Access-Control-Allow-Methods", "GET");
			response.setHeader("Access-Control-Max-Age", "3600");
			response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
            
			StreamUtils.copy(rsp.getEntity().getContent(), response.getOutputStream());
			jROutputMapper.insert(getBaseJROutput(report, rowQueryString,startDate));
		} finally {
			get.releaseConnection();
		}
	}

	/**
	 * 實時背景運行報表
	 * 
	 * @param userId 使用者ID
	 * @param sysId 系統ID
	 * @param folderId 目錄ID
	 * @param reportId 報表ID
	 * @param rowQueryString 傳入參數
	 * @param outputName 輸出檔名
	 * @throws Exception
	 */
	public String async(
			String userId, String sysId, String folderId, String reportId, 
			String rowQueryString, String outputName) throws Exception {
		UtlReports report = utlReportsMapper.find(sysId, folderId, reportId);
		if (report == null) {
			throw new ReportConfigNotFountException();
		}
		TwDate startDate = new TwDate();
		URIBuilder uriBuilder = getBaseURIBuilder("/rest_v2/jobs/");
		ReportJobSimpleTrigger trigger = new ReportJobSimpleTrigger();
		trigger.setStartType(ReportJobTrigger.START_TYPE_NOW);
		trigger.setOccurrenceCount(1);
		ReportJobSource source = new ReportJobSource();
		source.setReportUnitURI(getReportUnitURI(folderId, reportId));
		if (rowQueryString != null) {
			Map<String, Object> params = new HashMap<String, Object>();
			for (NameValuePair n : URLEncodedUtils.parse(rowQueryString, Charset.forName("UTF-8"))) {
				if (!n.getName().startsWith(CommonKey.PARAM_PREFIX)) {
					params.put(n.getName(), n.getValue());
				}
			}
			source.setParameters(params);
		}
		ReportJobRepositoryDestination destination = new ReportJobRepositoryDestination();
		destination.setFolderURI("/Output/" + sysId);
		destination.setOverwriteFiles(true);
		ReportJob job = new ReportJob();
		job.setLabel(outputName);
		job.setBaseOutputFilename(outputName + "." + report.getReport_type());
		job.addOutputFormat(formatFileType(report.getReport_type()));
		job.setTrigger(trigger);
		job.setSource(source);
		job.setUsername(userName);
		job.setContentRepositoryDestination(destination);
		
		StringWriter sw = new StringWriter();
		JAXBContext.newInstance(ReportJob.class).createMarshaller().marshal(job, sw);
		HttpPut put = new HttpPut(uriBuilder.build());
		StringEntity entity = new StringEntity(sw.toString(), ContentType.create("application/xml", "UTF-8"));
		put.setEntity(entity);
		try {
			HttpResponse rsp = client.execute(put);
			int code = rsp.getStatusLine().getStatusCode();
			if (code != 200) {
				throw new RuntimeException("排程失敗 error code : " + code + "\n" + EntityUtils.toString(rsp.getEntity()));
			}
			JROutput jROutput = getBaseJROutput(report, rowQueryString, startDate);
			jROutput.setOutput_path(destination.getFolderURI() + "/" + job.getBaseOutputFilename());
			jROutputMapper.insert(jROutput);
			return EntityUtils.toString(rsp.getEntity());
		} finally {
			put.releaseConnection();
		}
	}
	
	/**
	 * 查詢目錄
	 * 
	 * @param targetUri 目標URI
	 * @throws Exception
	 */
	public List<ResourceDescriptor> search(String targetUri) throws Exception {
		URIBuilder uriBuilder = getBaseURIBuilder("/rest/resources/reports/", targetUri);
		uriBuilder.addParameter("type", "reportUnit");
		HttpGet get = new HttpGet(uriBuilder.build());
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
			System.out.println(rsp.getEntity().getContent());
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

	/**
	 * 查詢明細
	 * 
	 * @param targetUri 目標URI
	 * @throws Exception
	 */
	public ResourceDescriptor detail(String targetUri) throws Exception {
		URIBuilder uriBuilder = new URIBuilder()
		.setScheme("http").setHost("192.168.22.107").setPort(8600)//.setUserInfo("jasperadmin", "jasperadmin")
		.setPath("/jasperserver/rest/resource" + targetUri);
		HttpGet get = new HttpGet(uriBuilder.build());
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
	
	private URIBuilder getBaseURIBuilder(String ... paths) {
		StringBuilder sb = new StringBuilder().append("/").append(webRoot);
		for (String s : paths) {
			boolean before = sb.substring(sb.length() - 1).equals("/");
			boolean after = s.startsWith("/");
			if (before && after) {
				sb.append(s.substring(1));
			} else if (!before && !after) {
				sb.append("/").append(s);
			} else {
				sb.append(s);
			}
		}
		return new URIBuilder()
		.setScheme(scheme).setHost(host).setPort(port).setUserInfo(userName, password)
		.setPath(sb.toString());
	}
	
	private JROutput getBaseJROutput(UtlReports report, String rowQueryString, TwDate startDate) {
		JROutput output = new JROutput();
		output.setReport_id(report.getReport_id());
		output.setReport_name(report.getReport_name());
		output.setRun_mode(report.getRun_mode());
		if (rowQueryString != null) {
			try {
				output.setParameter_text(URLDecoder.decode(rowQueryString, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
			}
		}
		output.setCreate_timestamp(startDate);
		output.setJr_server(host + ":" + port);
		output.setStart_timestamp(startDate);
		output.setEnd_timestamp(new TwDate());
		output.setExpiration_date(new TwDate().addDay(report.getExpiration_days()));
		return output;
	}
	
	private String getContentType(String fileType) {
		if ("HTML".equalsIgnoreCase(fileType)) {
			return "text/html";
		} else if ("PDF".equalsIgnoreCase(fileType)) {
			return "application/pdf";
		} else if ("XLSX".equalsIgnoreCase(fileType)) {
			return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		} else if ("XLS".equalsIgnoreCase(fileType)) {
			return "application/vnd.ms-excel";
		} else if ("CSV".equalsIgnoreCase(fileType)) {
			return "application/vnd.ms-excel";
		} else if ("RTF".equalsIgnoreCase(fileType)) {
			return "application/rtf";
		} else if ("ODT".equalsIgnoreCase(fileType)) {
			return "application/vnd.oasis.opendocument.text";
		} else if ("ODS".equalsIgnoreCase(fileType)) {
			return "application/vnd.oasis.opendocument.spreadsheet";
		} else if ("DOCX".equalsIgnoreCase(fileType)) {
			return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		}
		return null;
	}
	
	private byte formatFileType(String fileType) {
		if ("HTML".equalsIgnoreCase(fileType)) {
			return 2;
		} else if ("PDF".equalsIgnoreCase(fileType)) {
			return 1;
		} else if ("XLSX".equalsIgnoreCase(fileType)) {
			return 10;
		} else if ("XLS".equalsIgnoreCase(fileType)) {
			return 3;
		} else if ("CSV".equalsIgnoreCase(fileType)) {
			return 5;
		} else if ("RTF".equalsIgnoreCase(fileType)) {
			return 4;
		} else if ("ODT".equalsIgnoreCase(fileType)) {
			return 6;
		} else if ("ODS".equalsIgnoreCase(fileType)) {
			return 9;
		} else if ("DOCX".equalsIgnoreCase(fileType)) {
			return 8;
		}
		return 0;
	}
	
	private String getReportUnitURI(String folderId, String reportId) {
		return new StringBuilder()
		.append("/reports/")
		.append(folderId)
		.append("/")
		.append(reportId)
		.toString();
	}
	
}
