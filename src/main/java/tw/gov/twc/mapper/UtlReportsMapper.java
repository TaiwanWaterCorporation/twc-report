package tw.gov.twc.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import tw.gov.twc.model.UtlReports;

/**
 * 報表資訊資料表[UTL_REPORTS]映射介面
 * 
 * @author Eric
 */
public interface UtlReportsMapper {

	/**
	 * 取得報表資訊資料
	 */
	@Select("select * from utl_reports where sys_id = #{sys_id,jdbcType=VARCHAR} and folder_id = #{folder_id,jdbcType=VARCHAR} and report_id = #{report_id,jdbcType=VARCHAR}")
	public UtlReports find(
			@Param("sys_id") String sys_id, 
			@Param("folder_id") String folder_id, 
			@Param("report_id") String report_id);
}
