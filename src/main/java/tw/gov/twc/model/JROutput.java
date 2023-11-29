package tw.gov.twc.model;

import tw.gov.twc.type.TwDate;

/**
 * 報表輸出紀錄
 * 
 * @author Eric
 */
public class JROutput {

	private String output_id;
	private String report_id;
	private String report_name;
	private String run_mode;
	private String parameter_text;
	private String create_user_id;
	private TwDate create_timestamp;
	private String jr_server;
	private String output_path;
	private String output_status;
	private TwDate start_timestamp;
	private TwDate end_timestamp;
	private Integer page_count;
	private TwDate expiration_date;
	
	/**
	 * 預設建構子
	 */
	public JROutput () {}

	/**
	 * 取得輸出ID
	 *
	 * @return output_id
	 */
	public String getOutput_id() {
		return output_id;
	}

	/**
	 * 設定輸出ID
	 * 
	 * @param output_id 
	 */
	public void setOutput_id(String output_id) {
		this.output_id = output_id;
	}

	/**
	 * 取得報表 ID
	 *
	 * @return report_id
	 */
	public String getReport_id() {
		return report_id;
	}

	/**
	 * 設定報表 ID
	 * 
	 * @param report_id 
	 */
	public void setReport_id(String report_id) {
		this.report_id = report_id;
	}

	/**
	 * 取得報表名稱
	 *
	 * @return report_name
	 */
	public String getReport_name() {
		return report_name;
	}

	/**
	 * 設定報表名稱
	 * 
	 * @param report_name 
	 */
	public void setReport_name(String report_name) {
		this.report_name = report_name;
	}

	/**
	 * 取得執行模式
	 *
	 * @return run_mode
	 */
	public String getRun_mode() {
		return run_mode;
	}

	/**
	 * 設定執行模式
	 * 
	 * @param run_mode 
	 */
	public void setRun_mode(String run_mode) {
		this.run_mode = run_mode;
	}

	/**
	 * 取得傳入參數
	 *
	 * @return parameter_text
	 */
	public String getParameter_text() {
		return parameter_text;
	}

	/**
	 * 設定傳入參數
	 * 
	 * @param parameter_text 
	 */
	public void setParameter_text(String parameter_text) {
		this.parameter_text = parameter_text;
	}

	/**
	 * 取得報表建立人員
	 *
	 * @return create_user_id
	 */
	public String getCreate_user_id() {
		return create_user_id;
	}

	/**
	 * 設定報表建立人員
	 * 
	 * @param create_user_id 
	 */
	public void setCreate_user_id(String create_user_id) {
		this.create_user_id = create_user_id;
	}

	/**
	 * 取得報表建立日期
	 *
	 * @return create_timestamp
	 */
	public TwDate getCreate_timestamp() {
		return create_timestamp;
	}

	/**
	 * 設定報表建立日期
	 * 
	 * @param create_timestamp 
	 */
	public void setCreate_timestamp(TwDate create_timestamp) {
		this.create_timestamp = create_timestamp;
	}

	/**
	 * 取得報表伺服器位置
	 *
	 * @return jr_server
	 */
	public String getJr_server() {
		return jr_server;
	}

	/**
	 * 設定報表伺服器位置
	 * 
	 * @param jr_server 
	 */
	public void setJr_server(String jr_server) {
		this.jr_server = jr_server;
	}

	/**
	 * 取得輸出檔案路徑
	 *
	 * @return output_path
	 */
	public String getOutput_path() {
		return output_path;
	}

	/**
	 * 設定輸出檔案路徑
	 * 
	 * @param output_path 
	 */
	public void setOutput_path(String output_path) {
		this.output_path = output_path;
	}

	/**
	 * 取得輸出狀態
	 *
	 * @return output_status
	 */
	public String getOutput_status() {
		return output_status;
	}

	/**
	 * 設定輸出狀態
	 * 
	 * @param output_status 
	 */
	public void setOutput_status(String output_status) {
		this.output_status = output_status;
	}

	/**
	 * 取得開始時間
	 *
	 * @return start_timestamp
	 */
	public TwDate getStart_timestamp() {
		return start_timestamp;
	}

	/**
	 * 設定開始時間
	 * 
	 * @param start_timestamp 
	 */
	public void setStart_timestamp(TwDate start_timestamp) {
		this.start_timestamp = start_timestamp;
	}

	/**
	 * 取得結束時間
	 *
	 * @return end_timestamp
	 */
	public TwDate getEnd_timestamp() {
		return end_timestamp;
	}

	/**
	 * 設定結束時間
	 * 
	 * @param end_timestamp 
	 */
	public void setEnd_timestamp(TwDate end_timestamp) {
		this.end_timestamp = end_timestamp;
	}

	/**
	 * 取得頁數
	 *
	 * @return page_count
	 */
	public Integer getPage_count() {
		return page_count;
	}

	/**
	 * 設定頁數
	 * 
	 * @param page_count 
	 */
	public void setPage_count(Integer page_count) {
		this.page_count = page_count;
	}

	/**
	 * 取得資料保存期限
	 *
	 * @return expiration_date
	 */
	public TwDate getExpiration_date() {
		return expiration_date;
	}

	/**
	 * 設定資料保存期限
	 * 
	 * @param expiration_date 
	 */
	public void setExpiration_date(TwDate expiration_date) {
		this.expiration_date = expiration_date;
	}

}
