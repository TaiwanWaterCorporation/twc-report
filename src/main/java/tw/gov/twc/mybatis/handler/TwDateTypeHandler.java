package tw.gov.twc.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeReference;

import tw.gov.twc.type.TwDate;

/**
 * 提供mybatis對民國日期物件的處理
 * 
 * @author Eric
 */
public class TwDateTypeHandler extends TypeReference<TwDate> implements TypeHandler<TwDate> {
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.ibatis.type.TypeHandler#setParameter(java.sql.PreparedStatement, int, java.lang.Object, org.apache.ibatis.type.JdbcType)
	 */
	@Override
	public void setParameter(PreparedStatement ps, int i, TwDate parameter,
			JdbcType jdbcType) throws SQLException {
		JdbcType columnType = jdbcType == null ? JdbcType.VARCHAR : jdbcType;
		if (parameter == null) {
            ps.setNull(i, columnType.TYPE_CODE);
        } else {
        	switch (columnType) {
				case DATE:
					ps.setDate(i, parameter.toSQLDate());
					break;
				case TIMESTAMP:
					ps.setTimestamp(i, parameter.toTimestamp());
					break;
				case VARCHAR:
					ps.setString(i, parameter.toAD());
					break;
				default:
					throw new SQLException("TwDate not support " + columnType);
			}
        }
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.ibatis.type.TypeHandler#getResult(java.sql.ResultSet, java.lang.String)
	 */
	@Override
	public TwDate getResult(ResultSet rs, String columnName)
			throws SQLException {
		return getResult(rs, findColumnIndex(rs.getMetaData(), columnName));
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.ibatis.type.TypeHandler#getResult(java.sql.ResultSet, int)
	 */
	@Override
	public TwDate getResult(ResultSet rs, int columnIndex) throws SQLException {
		JdbcType columnType = getJdbcType(rs.getMetaData(), columnIndex);
		switch (columnType) {
			case DATE:
				return TwDate.form(rs.getDate(columnIndex));
			case TIMESTAMP:
				return TwDate.form(rs.getTimestamp(columnIndex));
			case VARCHAR:
				return TwDate.formAD(rs.getString(columnIndex));
			default:
				throw new SQLException("TwDate not support " + columnType);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.ibatis.type.TypeHandler#getResult(java.sql.CallableStatement, int)
	 */
	@Override
	public TwDate getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		JdbcType columnType = getJdbcType(cs.getMetaData(), columnIndex);
		switch (columnType) {
			case DATE:
				return TwDate.form(cs.getDate(columnIndex));
			case TIMESTAMP:
				return TwDate.form(cs.getTimestamp(columnIndex));
			case VARCHAR:
				return TwDate.formAD(cs.getString(columnIndex));
			default:
				throw new SQLException("TwDate not support " + columnType);
		}
	}
	
	private int findColumnIndex (ResultSetMetaData metaData, String columnName) throws SQLException {
		for (int i = 1, l = metaData.getColumnCount() ; i <= l ; i++) {
			if (StringUtils.equals(metaData.getColumnName(i), columnName)) {
				return i;
			}
		}
		throw new SQLException("can not find column index");
	}

	private JdbcType getJdbcType (ResultSetMetaData metaData, int column) throws SQLException {
		return JdbcType.forCode(metaData.getColumnType(column));
	}
}
