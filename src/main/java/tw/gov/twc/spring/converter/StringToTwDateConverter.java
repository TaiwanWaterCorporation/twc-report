package tw.gov.twc.spring.converter;

import org.springframework.core.convert.converter.Converter;

import tw.gov.twc.type.TwDate;

/**
 * 文字到民國日期物件的轉換器
 * 
 * @author Eric
 */
public class StringToTwDateConverter implements Converter<String, TwDate> {

	/*
	 * (non-Javadoc)
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	@Override
	public TwDate convert(String source) {
		return source != null ? TwDate.form(source) : null;
	}

}
