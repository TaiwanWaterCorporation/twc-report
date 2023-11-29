package tw.gov.twc.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 取得ApplicationContext工具類
 * 
 * @author Eric
 */
@Component
public class ApplicationContextUtils implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;

	/**
	 * 取得ApplicationContext
	 * 
	 * @return ApplicationContext
	 */
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
//		ApplicationContextUtils.applicationContext = applicationContext;
		this.applicationContext = applicationContext;
	}

}
