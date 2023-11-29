package tw.gov.twc.spring.converter;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.converter.Converter;

import tw.gov.twc.type.TwDate;

/**
 * 民國日期物件到文字的轉換器
 * 
 * @author Eric
 */
public class TwDateToStringConverter implements Converter<TwDate, String> {

	/*
	 * (non-Javadoc)
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	@Override
	public String convert(TwDate source) {
		return source != null ? source.toOriginalTwDate() : StringUtils.EMPTY;
	}

}
