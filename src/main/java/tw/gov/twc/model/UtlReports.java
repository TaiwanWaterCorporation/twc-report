package tw.gov.twc.model;

import tw.gov.twc.type.TwDate;

/**
 * 報表資訊資料表
 * 
 * @author Eric
 */
public class UtlReports {

	private String sys_id;
	private String folder_id;
	private String report_id;
	private String report_name;
	private String report_type;
	private int parameter_count;
	private String data_source;
	private String notify_mail;
	private int expiration_days;
	private String run_mode;
	private String create_user_id;
	private TwDate create_timestamp;
	private String modify_user_id;
	private TwDate modify_timestamp;
	private String func_id;
	private String is_public;
	
	/**
	 * 預設建構子
	 */
	public UtlReports () {}

	/**
	 * 取得系統 ID
	 *
	 * @return sys_id
	 */
	public String getSys_id() {
		return sys_id;
	}

	/**
	 * 設定系統 ID
	 * 
	 * @param sys_id 
	 */
	public void setSys_id(String sys_id) {
		this.sys_id = sys_id;
	}

	/**
	 * 取得報表伺服器目錄 ID
	 *
	 * @return folder_id
	 */
	public String getFolder_id() {
		return folder_id;
	}

	/**
	 * 設定報表伺服器目錄 ID
	 * 
	 * @param folder_id 
	 */
	public void setFolder_id(String folder_id) {
		this.folder_id = folder_id;
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
	 * 取得報表型態
	 *
	 * @return report_type
	 */
	public String getReport_type() {
		return report_type;
	}

	/**
	 * 設定報表型態
	 * 
	 * @param report_type 
	 */
	public void setReport_type(String report_type) {
		this.report_type = report_type;
	}

	/**
	 * 取得報表參數個數
	 *
	 * @return parameter_count
	 */
	public int getParameter_count() {
		return parameter_count;
	}

	/**
	 * 設定報表參數個數
	 * 
	 * @param parameter_count 
	 */
	public void setParameter_count(int parameter_count) {
		this.parameter_count = parameter_count;
	}

	/**
	 * 取得資料來源ID
	 *
	 * @return data_source
	 */
	public String getData_source() {
		return data_source;
	}

	/**
	 * 設定資料來源ID
	 * 
	 * @param data_source 
	 */
	public void setData_source(String data_source) {
		this.data_source = data_source;
	}

	/**
	 * 取得背景報表完成，通知人員
	 *
	 * @return notify_mail
	 */
	public String getNotify_mail() {
		return notify_mail;
	}

	/**
	 * 設定背景報表完成，通知人員
	 * 
	 * @param notify_mail 
	 */
	public void setNotify_mail(String notify_mail) {
		this.notify_mail = notify_mail;
	}

	/**
	 * 取得保留天數
	 *
	 * @return expiration_days
	 */
	public int getExpiration_days() {
		return expiration_days;
	}

	/**
	 * 設定保留天數
	 * 
	 * @param expiration_days 
	 */
	public void setExpiration_days(int expiration_days) {
		this.expiration_days = expiration_days;
	}

	/**
	 * 取得執行型態
	 *
	 * @return run_mode
	 */
	public String getRun_mode() {
		return run_mode;
	}

	/**
	 * 設定執行型態
	 * 
	 * @param run_mode 
	 */
	public void setRun_mode(String run_mode) {
		this.run_mode = run_mode;
	}

	/**
	 * 取得建立人員
	 *
	 * @return create_user_id
	 */
	public String getCreate_user_id() {
		return create_user_id;
	}

	/**
	 * 設定建立人員
	 * 
	 * @param create_user_id 
	 */
	public void setCreate_user_id(String create_user_id) {
		this.create_user_id = create_user_id;
	}

	/**
	 * 取得建立日期
	 *
	 * @return create_timestamp
	 */
	public TwDate getCreate_timestamp() {
		return create_timestamp;
	}

	/**
	 * 設定建立日期
	 * 
	 * @param create_timestamp 
	 */
	public void setCreate_timestamp(TwDate create_timestamp) {
		this.create_timestamp = create_timestamp;
	}

	/**
	 * 取得修改人員
	 *
	 * @return modify_user_id
	 */
	public String getModify_user_id() {
		return modify_user_id;
	}

	/**
	 * 設定修改人員
	 * 
	 * @param modify_user_id 
	 */
	public void setModify_user_id(String modify_user_id) {
		this.modify_user_id = modify_user_id;
	}

	/**
	 * 取得修改日期
	 *
	 * @return modify_timestamp
	 */
	public TwDate getModify_timestamp() {
		return modify_timestamp;
	}

	/**
	 * 設定修改日期
	 * 
	 * @param modify_timestamp 
	 */
	public void setModify_timestamp(TwDate modify_timestamp) {
		this.modify_timestamp = modify_timestamp;
	}

	/**
	 * 取得功能ID
	 *
	 * @return func_id
	 */
	public String getFunc_id() {
		return func_id;
	}

	/**
	 * 設定功能ID
	 * 
	 * @param func_id 
	 */
	public void setFunc_id(String func_id) {
		this.func_id = func_id;
	}

	/**
	 * 取得是否為公用報表
	 *
	 * @return is_public
	 */
	public String getIs_public() {
		return is_public;
	}

	/**
	 * 設定是否為公用報表
	 * 
	 * @param is_public 
	 */
	public void setIs_public(String is_public) {
		this.is_public = is_public;
	}

}
