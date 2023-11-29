package tw.gov.twc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import tw.gov.twc.mapper.AuthMapper;
import tw.gov.twc.service.JasperReportService;
import tw.gov.twc.spring.CommonKey;

/**
 * 提供報表呼叫API的控制器
 * 
 * @author Eric
 */
@RequestMapping("/operation")
@Controller
public class Operation {
	
	@Autowired
	private JasperReportService jasperReportService;
	@Autowired
	private AuthMapper authMapper;
	
	/**
	 * 實時前景運行報表
	 */
	@RequestMapping(value = "/run", method = RequestMethod.GET)
	public void run(
			@RequestParam(CommonKey.PARAM_PREFIX + "sys_id") String sysId,
			@RequestParam(CommonKey.PARAM_PREFIX + "folder_id") String folderId,
			@RequestParam(CommonKey.PARAM_PREFIX + "report_id") String reportId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!"Y".equalsIgnoreCase(authMapper.get_report_auth(sysId, folderId, reportId))) {
			throw new PermissionDeniedReportAccessException();
		}
		jasperReportService.run(
				request.getUserPrincipal().getName(),
				sysId, folderId, reportId, request.getQueryString(), response);
		response.getOutputStream().flush();
	}
	
	/**
	 * 實時背景運行報表
	 */
	@RequestMapping(value = "/async", method = RequestMethod.GET)
	public @ResponseBody String async(
			@RequestParam(CommonKey.PARAM_PREFIX + "sys_id") String sysId,
			@RequestParam(CommonKey.PARAM_PREFIX + "folder_id") String folderId,
			@RequestParam(CommonKey.PARAM_PREFIX + "report_id") String reportId,
			@RequestParam(CommonKey.PARAM_PREFIX + "output_name") String outputName,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!"Y".equalsIgnoreCase(authMapper.get_report_auth(sysId, folderId, reportId))) {
			throw new PermissionDeniedReportAccessException();
		}
		return jasperReportService.async(
				request.getUserPrincipal().getName(),
				sysId, folderId, reportId, request.getQueryString(), outputName);
	}
}
