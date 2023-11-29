package tw.gov.twc.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 報表授權[PKG_MENU.GET_REPORT_AUTH]映射介面
 * 
 * @author Eric
 */
public interface AuthMapper {

	/**
	 * 驗證是否授權可運行此報表
	 */
	@Select("select pkg_menu.get_report_auth(#{sys_id}, #{folder_id}, #{report_id}) from dual")
	public String get_report_auth(
			@Param("sys_id") String sys_id, 
			@Param("folder_id") String folder_id,
			@Param("report_id") String report_id);
}
