package tw.gov.twc.mapper;

import tw.gov.twc.model.JROutput;

/**
 * 報表輸出紀錄[JR_OUTPUT]映射介面
 * 
 * @author Eric
 */
public interface JROutputMapper {

	/**
	 * 新增報表輸出紀錄
	 */
	public void insert(JROutput jROutput);
	
}
